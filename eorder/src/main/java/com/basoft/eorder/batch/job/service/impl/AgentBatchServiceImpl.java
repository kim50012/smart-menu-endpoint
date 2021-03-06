package com.basoft.eorder.batch.job.service.impl;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.batch.job.model.agent.AgentDailySettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlementDetail;
import com.basoft.eorder.batch.job.model.agent.AgentSettlementOrder;
import com.basoft.eorder.batch.job.model.agent.AgentStoreMap;
import com.basoft.eorder.batch.job.service.IAgentBatchService;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.AgentRepository;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.interfaces.query.OrderQuery;
import com.basoft.eorder.interfaces.query.agent.AgentQuery;
import com.basoft.eorder.util.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgentBatchServiceImpl implements IAgentBatchService {
    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private AgentQuery agentQuery;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private OrderQuery orderQuery;

    /**
     * SA代理商月度结算(定时任务)
     *
     * @param agent
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     * @param monthStartTime
     * @param monthEndTime
     */
    public void storeAgentMonthSettle(Agent agent, int lastYear, int lastMonth, int year,
                                      int month, String monthStartTime, String monthEndTime) {
        /*****************************************一、查询SA代理商代理商户的结算费率-START*******************************************/
        List<AgentStoreMap> agentStoreMapList = agentQuery.getAgentStoreInfo(agent);
        if(agentStoreMapList == null || agentStoreMapList.size() == 0){
            // 如果代理商不存在代理商户，结束
            return;
        }
//        log.info("【SA代理商月度结算】SA代理商代理的商户数量为：" + agentStoreMapList.size());
        Map<Long, AgentStoreMap> agentStoreMaps = agentStoreMapList.stream().collect(Collectors.toMap(AgentStoreMap::getStoreId, agentStoreMap -> agentStoreMap));
        /*****************************************一、查询SA代理商代理商户的结算费率-END*******************************************/


        /*****************************************二、生成代理商的结算信息-START*******************************************/
        // 查询代理商的结算信息
        AgentSettlement agentSettlement = agentQuery.queryAgentSettlement(agent, getYearMonthStringNoLine(lastYear, lastMonth));
        // 新增结算信息
        final Long settleId;
        if (agentSettlement == null) {
            // 构建代理商的结算信息
            settleId = uidGenerator.generate(BusinessTypeEnum.AGENT_SETTLEMENT);
            AgentSettlement newAgentSettlement = AgentSettlement.builder().
                    sid(settleId)
                    .agtId(agent.getAgtId())
                    .agtCode(agent.getAgtCode())
                    .settleYearMonth(getYearMonthStringNoLine(lastYear, lastMonth))
                    .settleYear(lastYear)
                    .settleMonth(lastMonth)
                    .startDT(monthStartTime.substring(0, 10))
                    .endDT(monthEndTime.substring(0, 10))
                    .orderCount(0L)
                    .settleSum(new BigDecimal(0))
                    .agtFee(new BigDecimal(0))
                    .agtVatFee(new BigDecimal(0))
                    //.plDate() // SQL中写入
                    .build();

            // 插入结算信息
            int insertCount = agentRepository.insertAgentSettlement(newAgentSettlement);

            agentSettlement = newAgentSettlement;
        }
        // nothing to do
        else {
            settleId = agentSettlement.getSid();
        }
        /*****************************************二、生成代理商的结算信息-END*******************************************/


        /*****************************************三、查询并转存该代理商的订单（结算订单）-START*******************************************/
        /**
         * 特别说明，思路同商户月度结算的订单统计思路二
         * 1、查询出结算月有效交易订单（有效交易订单=非退款订单 + 非结算月退款订单）
         * 20200107改造：由于订单加上了完成状态，代理商结算需要限定为结算完成状态的订单，需要对结算订单查询逻辑进行改造
         * 2、查询出结算月内退款但交易日期在之前12个月内的订单
         * 3、查询出结算月交易并且结算月退款的订单并且进行
         */
        // 1、查询出结算月有效交易订单（有效交易订单=非退款订单 + 非结算月退款订单）
        //List<AgentSettlementOrder> agentOrderList = orderQuery.querySagentOrderList(agent,
        //        monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        // 1、20200107改造：查询出结算月有效交易订单（有效交易订单=完成订单 + 结算月交易但非结算月退款订单）
        List<AgentSettlementOrder> agentOrderList = orderQuery.querySagentOrderList1(agent,
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));

        // 2、查询出结算月内退款但交易日期在之前12个月内的订单
        List<AgentSettlementOrder> agentRefundBeforeOrderList = orderQuery.querySagentRefundBeforeOrderList(agent,
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));

        // 3、查询出结算月交易并且结算月退款的订单并且进行
        List<AgentSettlementOrder> agentRefundOrderList = orderQuery.querySagentRefundOrderList(agent,
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));

        /*List<AgentSettlementOrder> agentAllOrderList = orderQuery.queryAgentAllOrderList(agent,
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));*/

        // 4、结算订单加工和保存-start
        // 4-1、结算月有效订单加工（结算、计算vatFee等）
        settleOrderList(agentOrderList, agentStoreMaps, settleId, false);

        // 4-2、结算月退款的之前月份订单加工
        settleOrderList(agentRefundBeforeOrderList, agentStoreMaps, settleId, true);

        // 4-3、结算月交易并且结算月退款的订单加工
        settleOrderList(agentRefundOrderList, agentStoreMaps, settleId, false);

        // 4-4、复制一份 结算月交易并且结算月退款的订单
        /*List<AgentSettlementOrder> copyAgentRefundOrderList = new ArrayList<AgentSettlementOrder>();
        CollectionUtils.addAll(copyAgentRefundOrderList, new Object[agentRefundOrderList.size()]);
        Collections.copy(copyAgentRefundOrderList, agentRefundOrderList);*/ //无法完成列表的深拷贝
        List<AgentSettlementOrder> copyAgentRefundOrderList = new ArrayList<AgentSettlementOrder>();
        for(AgentSettlementOrder agentSettlementOrder : agentRefundOrderList){
            AgentSettlementOrder newAso = new AgentSettlementOrder();
            try {
                BeanUtils.copyProperties(newAso, agentSettlementOrder);
            } catch (IllegalAccessException | InvocationTargetException e) {
//                log.error("【SA代理商月度结算】复制一份::::结算月交易并且结算月退款的订单出现异常：" + e.getMessage(), e);
            }
            copyAgentRefundOrderList.add(newAso);
        }

        // 4-5、列表变负
        copyAgentRefundOrderList.stream().forEach(agentSettlementOrder -> {
            agentSettlementOrder.setOrderAmount(agentSettlementOrder.getOrderAmount().negate());
            agentSettlementOrder.setPlFee(agentSettlementOrder.getPlFee().negate());
            agentSettlementOrder.setAgtFee(agentSettlementOrder.getAgtFee().negate());
            agentSettlementOrder.setVatFee(agentSettlementOrder.getVatFee().negate());
            agentSettlementOrder.setIsRefund(1);
        });

        // 4-6、批量新增结算订单
        agentOrderList.addAll(agentRefundBeforeOrderList);
        agentOrderList.addAll(agentRefundOrderList);
        agentOrderList.addAll(copyAgentRefundOrderList);
        int orderCount = agentOrderList.size();
        if(orderCount == 0){
            // 如果结算订单数量为0，下面的步骤不需要进行计算了，结算任务结束
            return;
        }
        int count = agentRepository.batchInsertAgentOrder(settleId, agentOrderList);
//        log.info("【SA代理商月度结算】SA代理商结算订单数量为：{}", count);
        // 4、结算订单加工和保存-end
        /*****************************************三、查询并转存该代理商的订单（结算订单）-END*******************************************/


        /*****************************************四、更新结算信息（订单数量、结算金额、代理商结算金额、VAT金额）-START*******************************************/
        // 1、订单数量
        // 订单数量
        int orderFinalCount = orderCount;

        // 2、结算订单金额、代理商结算总金额、VAT金额
        Map<String, Object> agentSettleInfo = agentQuery.getAgentSettleInfo(settleId);
        BigDecimal orderTotalAmount = (BigDecimal) agentSettleInfo.get("orderTotalAmount");
        BigDecimal agtTotalFee = (BigDecimal) agentSettleInfo.get("agtTotalFee");
        BigDecimal vatTotalFee = (BigDecimal) agentSettleInfo.get("vatTotalFee");

        // 3、更新代理商结算信息（订单数量、结算金额、代理商结算金额、VAT金额）
        agentSettlement.setOrderCount(Long.valueOf(orderFinalCount));
        agentSettlement.setSettleSum(orderTotalAmount);
        agentSettlement.setAgtFee(agtTotalFee);
        agentSettlement.setAgtVatFee(vatTotalFee);
        int upCount = agentRepository.updateAgentSettlement(agentSettlement);
        /*****************************************四、更新结算信息（订单数量、结算金额、代理商结算金额、VAT金额）-END*******************************************/


        /*****************************************五、写入代理商结算明细-START*******************************************/
        List<AgentSettlementDetail> agentSettlementDetails = agentQuery.getAgentSettlementDetailList(settleId,
                lastYear, lastMonth, getYearMonthStringNoLine(lastYear, lastMonth));
        agentSettlementDetails.forEach(agentSettlementDetail -> {
            // SA代理商结算费率-start
            AgentStoreMap agentStoreMap = agentStoreMaps.get(agentSettlementDetail.getStoreId());
            // 设置过
            if (agentStoreMap != null) {
                agentSettlementDetail.setAgtRate(agentStoreMap.getAgtChargeRate());
                agentSettlementDetail.setAgtPercent(agentStoreMap.getAgtChargePercent());
            }
            // 未设置过
            else {
                /*agentSettlementDetail.setAgtRate(new BigDecimal(CommonConstants.AGENT_DEFAULT_RATE));
                agentSettlementDetail.setAgtPercent(CommonConstants.AGENT_DEFAULT_PERSENT);*/

                // SA代理商的结算费率为商户结算费率乘以默认费率
                agentSettlementDetail.setAgtRate(
                        new BigDecimal(agentSettlementDetail.getPlRate())
                                .multiply(new BigDecimal(CommonConstants.AGENT_DEFAULT_RATE))
                                .divide(new BigDecimal(100))
                                .setScale(4, BigDecimal.ROUND_UP)
                );
                agentSettlementDetail.setAgtPercent(CommonConstants.AGENT_DEFAULT_PERSENT);
            }
            // SA代理商结算费率-end
        });
        // 批量新增结算明细
        int dCount = agentRepository.batchInsertAgentOrderDetail(settleId, agentSettlementDetails);
//        log.info("【SA代理商月度结算】SA代理商结算的明细数量为：{}", dCount);
        /*****************************************五、写入代理商结算明细-END*******************************************/
    }

    /**
     * 订单加工
     * 说明：SA、CA代理商月度结算和每日结算都采用该订单加工方法，慎重修改。
     *
     * @param agentOrderList
     * @param agentStoreMaps
     * @param settleId
     * @param isNegative
     */
    private void settleOrderList(List<AgentSettlementOrder> agentOrderList,
                                 Map<Long, AgentStoreMap> agentStoreMaps,
                                 Long settleId,
                                 boolean isNegative) {
        // 判空
        if(agentOrderList == null || agentOrderList.size() == 0){
            return;
        }

        // 重新构建agentStoreMaps用于拉姆他表达式
        final Map<Long, AgentStoreMap> reAgentStoreMaps = agentStoreMaps;

        // 循环处理列表
        agentOrderList.stream().forEach(agentSettlementOrder -> {
            // 代理商结算编号
            agentSettlementOrder.setSid(settleId);

            // 平台计算费用暂时不计算
            // agentSettlementOrder.setPlFee(new BigDecimal(0));
            agentSettlementOrder.setPlFee(agentSettlementOrder.getOrderAmount()
                            .multiply(agentSettlementOrder.getStorePlatRate())
                            .setScale(2, BigDecimal.ROUND_UP));


            // 代理商费用结算-start
            // 代理商默认费率
            BigDecimal agtChargeRate = new BigDecimal(CommonConstants.AGENT_DEFAULT_RATE);
            AgentStoreMap agentStoreMap = reAgentStoreMaps.get(agentSettlementOrder.getStoreId());
            if (agentStoreMap != null) {
                // 代理商商户的设定费率
                agtChargeRate = agentStoreMap.getAgtChargeRate();
            } else {
                // 代理商商户的结算费率乘以代理商默认费率得出代理商结算费率
                agtChargeRate = agentSettlementOrder.getStorePlatRate().multiply(agtChargeRate).setScale(4, BigDecimal.ROUND_UP);
            }

            // 退款变负数
            if (isNegative) {
                // 结算费用
                BigDecimal agtFee = agentSettlementOrder.getOrderAmount().multiply(agtChargeRate).setScale(2, BigDecimal.ROUND_UP);
                BigDecimal vatFee = agtFee.multiply(new BigDecimal(CommonConstants.AGENT_VAT_RATE)).setScale(2, BigDecimal.ROUND_UP);
                // 代理商结算金额变负数
                agentSettlementOrder.setAgtFee(agtFee.negate());
                // VAT金额变负数
                agentSettlementOrder.setVatFee(vatFee.negate());
                // 订单金额变负数
                agentSettlementOrder.setOrderAmount(agentSettlementOrder.getOrderAmount().negate());
            } else {
                // 结算费用
                BigDecimal agtFee = agentSettlementOrder.getOrderAmount().multiply(agtChargeRate).setScale(2, BigDecimal.ROUND_UP);
                agentSettlementOrder.setAgtFee(agtFee);

                BigDecimal vatFee = agtFee.multiply(new BigDecimal(CommonConstants.AGENT_VAT_RATE)).setScale(2, BigDecimal.ROUND_UP);
                agentSettlementOrder.setVatFee(vatFee);
            }
        });
    }

    /**
     * 组装年月字符串,带分隔横线
     *
     * @param year
     * @param month
     * @return
     */
    private String getYearMonthString(int year, int month) {
        String monthStr = String.valueOf(month).length() == 1 ? "0" + month : String.valueOf(month);
        return year + "-" + monthStr;
    }

    /**
     * 组装年月字符串,不带分隔横线
     *
     * @param year
     * @param month
     * @return
     */
    private String getYearMonthStringNoLine(int year, int month) {
        String monthStr = String.valueOf(month).length() == 1 ? "0" + month : String.valueOf(month);
        return year + "" + monthStr;
    }

    /**
     * CA代理商月度结算(定时任务)
     *
     * @param agent
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     * @param monthStartTime
     * @param monthEndTime
     */
    public void customerAgentMonthSettle(Agent agent, int lastYear, int lastMonth, int year, int month,
                                         String monthStartTime, String monthEndTime) {
        /*****************************************一、查询CA代理商代理商户的结算费率-START*******************************************/
        List<AgentStoreMap> agentStoreMapList = agentQuery.getAgentStoreInfo(agent);
        Map<Long, AgentStoreMap> agentStoreMaps = new HashMap<Long, AgentStoreMap>();
        if(agentStoreMapList == null || agentStoreMapList.size() == 0){
            // CA代理商不存在关联商户
//            log.info("【CA代理商月度结算】CA代理商关联的商户数量为：0");
        } else {
//            log.info("【CA代理商月度结算】CA代理商关联的商户数量为：" + agentStoreMapList.size());
            agentStoreMaps = agentStoreMapList.stream().collect(Collectors.toMap(AgentStoreMap::getStoreId, agentStoreMap -> agentStoreMap));
        }
        /*****************************************一、查询SA代理商代理商户的结算费率-END*******************************************/


        /*****************************************二、生成代CA理商的结算信息-START*******************************************/
        // 查询CA代理商的结算信息
        AgentSettlement agentSettlement = agentQuery.queryAgentSettlement(agent, getYearMonthStringNoLine(lastYear, lastMonth));

        // 新增结算信息
        final Long settleId;
        if (agentSettlement == null) {
            // 构建代理商的结算信息
            settleId = uidGenerator.generate(BusinessTypeEnum.AGENT_SETTLEMENT);
            AgentSettlement newAgentSettlement = AgentSettlement.builder().
                    sid(settleId)
                    .agtId(agent.getAgtId())
                    .agtCode(agent.getAgtCode())
                    .settleYearMonth(getYearMonthStringNoLine(lastYear, lastMonth))
                    .settleYear(lastYear)
                    .settleMonth(lastMonth)
                    .startDT(monthStartTime.substring(0, 10))
                    .endDT(monthEndTime.substring(0, 10))
                    .orderCount(0L)
                    .settleSum(new BigDecimal(0))
                    .agtFee(new BigDecimal(0))
                    .agtVatFee(new BigDecimal(0))
                    //.plDate() // SQL中写入
                    .build();

            // 插入结算信息
            int insertCount = agentRepository.insertAgentSettlement(newAgentSettlement);

            agentSettlement = newAgentSettlement;
        }
        // nothing to do
        else {
            settleId = agentSettlement.getSid();
        }
        /*****************************************二、生成CA代理商的结算信息-END*******************************************/


        /*****************************************三、查询并转存该CA代理商的订单（结算订单）-START*******************************************/
        /**
         * 特别说明，思路同商户月度结算的订单统计思路二
         * 1、查询出结算月有效交易订单（有效交易订单=非退款订单 + 非结算月退款订单）
         * 20200107改造：由于订单加上了完成状态，代理商结算需要限定为结算完成状态的订单，需要对结算订单查询逻辑进行改造
         * 2、查询出结算月内退款但交易日期在之前12个月内的订单
         * 3、查询出结算月交易并且结算月退款的订单并且进行
         */
        // 1、查询出结算月有效交易订单（有效交易订单=非退款订单 + 非结算月退款订单）
        //List<AgentSettlementOrder> agentOrderList = orderQuery.queryCagentOrderList(agent,
        //        monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        // 1、20200107改造：查询出结算月有效交易订单（有效交易订单=完成订单 + 结算月交易但非结算月退款订单）
        List<AgentSettlementOrder> agentOrderList = orderQuery.queryCagentOrderList1(agent,
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));

        // 2、查询出结算月内退款但交易日期在之前12个月内的订单
        List<AgentSettlementOrder> agentRefundBeforeOrderList = orderQuery.queryCagentRefundBeforeOrderList(agent,
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));

        // 3、查询出结算月交易并且结算月退款的订单
        List<AgentSettlementOrder> agentRefundOrderList = orderQuery.queryCagentRefundOrderList(agent,
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));

        // 4、结算订单加工和保存-start
        // 4-1、结算月有效订单加工（结算、计算vatFee等）
        settleOrderList(agentOrderList, agentStoreMaps, settleId, false);

        // 4-2、结算月退款的之前月份订单加工
        settleOrderList(agentRefundBeforeOrderList, agentStoreMaps, settleId, true);

        // 4-3、结算月交易并且结算月退款的订单加工
        settleOrderList(agentRefundOrderList, agentStoreMaps, settleId, false);

        // 4-4、复制一份 结算月交易并且结算月退款的订单
        List<AgentSettlementOrder> copyAgentRefundOrderList = new ArrayList<AgentSettlementOrder>();
        for(AgentSettlementOrder agentSettlementOrder : agentRefundOrderList){
            AgentSettlementOrder newAso = new AgentSettlementOrder();
            try {
                BeanUtils.copyProperties(newAso, agentSettlementOrder);
            } catch (IllegalAccessException | InvocationTargetException e) {
//                log.error("【CA代理商月度结算】复制一份::::结算月交易并且结算月退款的订单出现异常：" + e.getMessage(), e);
            }
            copyAgentRefundOrderList.add(newAso);
        }

        // 列表变负
        copyAgentRefundOrderList.stream().forEach(agentSettlementOrder -> {
            agentSettlementOrder.setOrderAmount(agentSettlementOrder.getOrderAmount().negate());
            agentSettlementOrder.setPlFee(agentSettlementOrder.getPlFee().negate());
            agentSettlementOrder.setAgtFee(agentSettlementOrder.getAgtFee().negate());
            agentSettlementOrder.setVatFee(agentSettlementOrder.getVatFee().negate());
            agentSettlementOrder.setIsRefund(1);
        });

        // 4-5、批量新增结算订单
        agentOrderList.addAll(agentRefundBeforeOrderList);
        agentOrderList.addAll(agentRefundOrderList);
        agentOrderList.addAll(copyAgentRefundOrderList);
        int orderCount = agentOrderList.size();
        if(orderCount == 0){
            // 如果结算订单数量为0，下面的步骤不需要进行计算了，结算任务结束
            return;
        }
        int count = agentRepository.batchInsertAgentOrder(settleId, agentOrderList);
//        log.info("【CA代理商月度结算】CA代理商结算订单数量为：{}", count);
        // 4、结算订单加工和保存-end
        /*****************************************三、查询并转存该CA代理商的订单（结算订单）-END*******************************************/


        /*****************************************五、更新结算信息（订单数量、结算金额、代理商结算金额、VAT金额）-START*******************************************/
        // 1、订单数量
        // 订单数量
        int orderFinalCount = orderCount;

        // 2、结算订单金额、代理商结算总金额、VAT金额
        Map<String, Object> agentSettleInfo = agentQuery.getAgentSettleInfo(settleId);
        BigDecimal orderTotalAmount = (BigDecimal) agentSettleInfo.get("orderTotalAmount");
        BigDecimal agtTotalFee = (BigDecimal) agentSettleInfo.get("agtTotalFee");
        BigDecimal vatTotalFee = (BigDecimal) agentSettleInfo.get("vatTotalFee");

        // 3、更新代理商结算信息（订单数量、结算金额、代理商结算金额、VAT金额）
        agentSettlement.setOrderCount(Long.valueOf(orderFinalCount));
        agentSettlement.setSettleSum(orderTotalAmount);
        agentSettlement.setAgtFee(agtTotalFee);
        agentSettlement.setAgtVatFee(vatTotalFee);
        int upCount = agentRepository.updateAgentSettlement(agentSettlement);
        /*****************************************四、更新结算信息（订单数量、结算金额、代理商结算金额、VAT金额）-END*******************************************/


        /*****************************************四、写入代理商结算明细-START*******************************************/
        List<AgentSettlementDetail> agentSettlementDetails = agentQuery.getAgentSettlementDetailList(settleId,
                lastYear, lastMonth, getYearMonthStringNoLine(lastYear, lastMonth));
        // 重新构建agentStoreMaps用于拉姆他表达式
        final Map<Long, AgentStoreMap> reAgentStoreMaps = agentStoreMaps;
        agentSettlementDetails.forEach(agentSettlementDetail -> {
            // CA代理商结算费率计算-start
            AgentStoreMap agentStoreMap = reAgentStoreMaps.get(agentSettlementDetail.getStoreId());
            // 设置过
            if (agentStoreMap != null) {
                agentSettlementDetail.setAgtRate(agentStoreMap.getAgtChargeRate());
                agentSettlementDetail.setAgtPercent(agentStoreMap.getAgtChargePercent());
            }
            // 未设置过
            else {
                // CA代理商的结算费率为商户结算费率乘以默认费率
                agentSettlementDetail.setAgtRate(
                        new BigDecimal(agentSettlementDetail.getPlRate())
                        .multiply(new BigDecimal(CommonConstants.AGENT_DEFAULT_RATE))
                        .divide(new BigDecimal(100))
                        .setScale(4,BigDecimal.ROUND_UP)
                );
                agentSettlementDetail.setAgtPercent(CommonConstants.AGENT_DEFAULT_PERSENT);
            }
            // CA代理商结算费率计算-end
        });
        // 批量新增结算明细
        int dCount = agentRepository.batchInsertAgentOrderDetail(settleId, agentSettlementDetails);
//        log.info("【CA代理商月度结算】CA代理商结算的明细数量为：{}", dCount);
        /*****************************************五、写入代理商结算明细-END*******************************************/
    }

    /**@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@上代理商月度结算，下代理商每日结算@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

    /**
     * SA代理商每日结算(定时任务)
     *
     * @param agent
     * @param yesterday
     * @param dayStartTime
     * @param dayEndTime
     * @param today
     */
    public void storeAgentDailySettle(Agent agent, String yesterday, String dayStartTime, String dayEndTime, String today) {
        /*****************************************一、查询SA代理商代理商户的结算费率-START*******************************************/
        List<AgentStoreMap> agentStoreMapList = agentQuery.getAgentStoreInfo(agent);
        if(agentStoreMapList == null || agentStoreMapList.size() == 0){
            // 如果代理商不存在代理商户，结束
            return;
        }
//        log.info("【SA代理商每日结算】SA代理商代理的商户数量为：" + agentStoreMapList.size());
        Map<Long, AgentStoreMap> agentStoreMaps = agentStoreMapList.stream().collect(Collectors.toMap(AgentStoreMap::getStoreId, agentStoreMap -> agentStoreMap));
        /*****************************************一、查询SA代理商代理商户的结算费率-END*******************************************/


        /*****************************************二、生成代理商的结算信息-START*******************************************/
        // 查询代理商的结算信息
        AgentDailySettlement agentDailySettlement = agentQuery.queryAgentDailySettlement(agent, yesterday);
        // 新增结算信息
        final Long settleId;
        if (agentDailySettlement == null) {
            // 构建代理商的结算信息
            settleId = uidGenerator.generate(BusinessTypeEnum.AGENT_SETTLEMENT);
            AgentDailySettlement newAgentDailySettlement = AgentDailySettlement.builder().
                    sid(settleId)
                    .agtId(agent.getAgtId())
                    .agtCode(agent.getAgtCode())
                    .settleDate(yesterday)
                    .orderCount(0L)
                    .settleSum(new BigDecimal(0))
                    .agtFee(new BigDecimal(0))
                    .agtVatFee(new BigDecimal(0))
                    //.plDate() // SQL中写入
                    .build();

            // 插入结算信息
            int insertCount = agentRepository.insertAgentDailySettlement(newAgentDailySettlement);

            agentDailySettlement = newAgentDailySettlement;
        }
        // nothing to do
        else {
            settleId = agentDailySettlement.getSid();
        }
        /*****************************************二、生成代理商的结算信息-END*******************************************/



        /*****************************************三、查询并转存该代理商的订单（结算订单）-START*******************************************/
        /**
         * 特别说明，思路同商户月度结算的订单统计思路二
         * 1、查询出结算日有效交易订单（有效交易订单=非退款订单 + 非结算日退款订单）
         * 20200107改造：由于订单加上了完成状态，代理商结算需要限定为结算完成状态的订单，需要对结算订单查询逻辑进行改造
         * 2、查询出结算日内退款但交易日期在之前360天内的订单
         * 3、查询出结算日交易并且结算日退款的订单
         */
        // 1、查询出结算日有效交易订单（有效交易订单=非退款订单 + 非结算日退款订单）
        //List<AgentSettlementOrder> agentDailyOrderList = orderQuery.querySagentDailyOrderList(agent,
        //        dayStartTime, dayEndTime, yesterday);
        // 1、20200107改造：查询出结算月有效交易订单（有效交易订单=完成订单 + 结算月交易但非结算月退款订单）
        List<AgentSettlementOrder> agentDailyOrderList = orderQuery.querySagentDailyOrderList1(agent,
                dayStartTime, dayEndTime, yesterday);

        // 2、查询出结算日内退款但交易日期在之前12个月内的订单
        List<AgentSettlementOrder> agentDailyRefundBeforeOrderList = orderQuery
                .querySagentDailyRefundBeforeOrderList(agent, dayStartTime, dayEndTime, yesterday);

        // 3、查询出结算日交易并且结算日退款的订单
        List<AgentSettlementOrder> agentDailyRefundOrderList = orderQuery.querySagentDailyRefundOrderList(agent,
                dayStartTime, dayEndTime, yesterday);

        // 4、结算订单加工和保存-start
        // 4-1、结算月有效订单加工（结算、计算vatFee等）
        settleOrderList(agentDailyOrderList, agentStoreMaps, settleId, false);

        // 4-2、结算月退款的之前月份订单加工
        settleOrderList(agentDailyRefundBeforeOrderList, agentStoreMaps, settleId, true);

        // 4-3、结算月交易并且结算月退款的订单加工
        settleOrderList(agentDailyRefundOrderList, agentStoreMaps, settleId, false);

        // 4-4、复制一份 结算月交易并且结算月退款的订单
        List<AgentSettlementOrder> copyAgentDailyRefundOrderList = new ArrayList<AgentSettlementOrder>();
        for (AgentSettlementOrder agentSettlementOrder : agentDailyRefundOrderList) {
            AgentSettlementOrder newAso = new AgentSettlementOrder();
            try {
                BeanUtils.copyProperties(newAso, agentSettlementOrder);
            } catch (IllegalAccessException | InvocationTargetException e) {
//                log.error("【SA代理商每日结算】复制一份::::结算日交易并且结算日退款的订单时出现异常：" + e.getMessage(), e);
            }
            copyAgentDailyRefundOrderList.add(newAso);
        }

        // 4-5、列表变负
        copyAgentDailyRefundOrderList.stream().forEach(agentSettlementOrder -> {
            agentSettlementOrder.setOrderAmount(agentSettlementOrder.getOrderAmount().negate());
            agentSettlementOrder.setPlFee(agentSettlementOrder.getPlFee().negate());
            agentSettlementOrder.setAgtFee(agentSettlementOrder.getAgtFee().negate());
            agentSettlementOrder.setVatFee(agentSettlementOrder.getVatFee().negate());
            agentSettlementOrder.setIsRefund(1);
        });

        // 4-6、批量新增结算订单
        agentDailyOrderList.addAll(agentDailyRefundBeforeOrderList);
        agentDailyOrderList.addAll(agentDailyRefundOrderList);
        agentDailyOrderList.addAll(copyAgentDailyRefundOrderList);
        int orderCount = agentDailyOrderList.size();
        if(orderCount == 0){
            // 如果结算订单数量为0，下面的步骤不需要进行计算了，结算任务结束
            return;
        }
        int count = agentRepository.batchInsertAgentOrder(settleId, agentDailyOrderList);
//        log.info("【SA代理商每日结算】SA代理商结算订单数量为：{}", count);
        // 4、结算订单加工和保存-end
        /*****************************************三、查询并转存该代理商的订单（结算订单）-END*******************************************/


        /*****************************************四、更新结算信息（订单数量、结算金额、代理商结算金额、VAT金额）-START*******************************************/
        // 1、订单数量
        // 订单数量
        int orderFinalCount = orderCount;

        // 2、结算订单金额、代理商结算总金额、VAT金额
        Map<String, Object> agentSettleInfo = agentQuery.getAgentSettleInfo(settleId);
        BigDecimal orderTotalAmount = (BigDecimal) agentSettleInfo.get("orderTotalAmount");
        BigDecimal agtTotalFee = (BigDecimal) agentSettleInfo.get("agtTotalFee");
        BigDecimal vatTotalFee = (BigDecimal) agentSettleInfo.get("vatTotalFee");

        // 3、更新代理商结算信息（订单数量、结算金额、代理商结算金额、VAT金额）
        agentDailySettlement.setOrderCount(Long.valueOf(orderFinalCount));
        agentDailySettlement.setSettleSum(orderTotalAmount);
        agentDailySettlement.setAgtFee(agtTotalFee);
        agentDailySettlement.setAgtVatFee(vatTotalFee);
        int upCount = agentRepository.updateAgentDailySettlement(agentDailySettlement);
        /*****************************************四、更新结算信息（订单数量、结算金额、代理商结算金额、VAT金额）-END*******************************************/


        /*****************************************五、写入代理商结算明细-START*******************************************/
        List<AgentSettlementDetail> agentDailySettlementDetails = agentQuery.getAgentDailySettlementDetailList(settleId);
        agentDailySettlementDetails.forEach(agentSettlementDetail -> {
            // SA代理商结算费率-start
            AgentStoreMap agentStoreMap = agentStoreMaps.get(agentSettlementDetail.getStoreId());
            // 设置过
            if (agentStoreMap != null) {
                agentSettlementDetail.setAgtRate(agentStoreMap.getAgtChargeRate());
                agentSettlementDetail.setAgtPercent(agentStoreMap.getAgtChargePercent());
            }
            // 未设置过
            else {
                // SA代理商的结算费率为商户结算费率乘以默认费率
                agentSettlementDetail.setAgtRate(
                        new BigDecimal(agentSettlementDetail.getPlRate())
                                .multiply(new BigDecimal(CommonConstants.AGENT_DEFAULT_RATE))
                                .divide(new BigDecimal(100))
                                .setScale(4, BigDecimal.ROUND_UP)
                );
                agentSettlementDetail.setAgtPercent(CommonConstants.AGENT_DEFAULT_PERSENT);
            }
            // SA代理商结算费率-end
        });
        // 批量新增结算明细
        int dCount = agentRepository.batchInsertAgentOrderDetail(settleId, agentDailySettlementDetails);
//        log.info("【SA代理每日结算】SA代理商结算的明细数量为：{}", dCount);
        /*****************************************五、写入代理商结算明细-END*******************************************/
    }

    /**
     * CA代理商每日结算(定时任务)
     *
     * @param agent
     * @param yesterday
     * @param dayStartTime
     * @param dayEndTime
     * @param today
     */
    public void customerAgentDailySettle(Agent agent, String yesterday, String dayStartTime, String dayEndTime, String today) {
        /*****************************************一、查询CA代理商代理商户的结算费率-START*******************************************/
        List<AgentStoreMap> agentStoreMapList = agentQuery.getAgentStoreInfo(agent);
        Map<Long, AgentStoreMap> agentStoreMaps = new HashMap<Long, AgentStoreMap>();
        if(agentStoreMapList == null || agentStoreMapList.size() == 0){
            // CA代理商不存在关联商户
//            log.info("【CA代理商每日结算】CA代理商关联的商户数量为：0");
        } else {
//            log.info("【CA代理商每日结算】CA代理商关联的商户数量为：" + agentStoreMapList.size());
            agentStoreMaps = agentStoreMapList.stream().collect(Collectors.toMap(AgentStoreMap::getStoreId, agentStoreMap -> agentStoreMap));
        }
        /*****************************************一、查询SA代理商代理商户的结算费率-END*******************************************/


        /*****************************************二、生成代CA理商的结算信息-START*******************************************/
        // 查询CA代理商的每日结算信息
        AgentDailySettlement agentDailySettlement = agentQuery.queryAgentDailySettlement(agent, yesterday);

        // 新增结算信息
        final Long settleId;
        if (agentDailySettlement == null) {
            // 构建代理商的结算信息
            settleId = uidGenerator.generate(BusinessTypeEnum.AGENT_SETTLEMENT);
            AgentDailySettlement newAgentDailySettlement = AgentDailySettlement.builder().
                    sid(settleId)
                    .agtId(agent.getAgtId())
                    .agtCode(agent.getAgtCode())
                    .settleDate(yesterday)
                    .orderCount(0L)
                    .settleSum(new BigDecimal(0))
                    .agtFee(new BigDecimal(0))
                    .agtVatFee(new BigDecimal(0))
                    //.plDate() // SQL中写入
                    .build();

            // 插入结算信息
            int insertCount = agentRepository.insertAgentDailySettlement(newAgentDailySettlement);

            agentDailySettlement = newAgentDailySettlement;
        }
        // nothing to do
        else {
            settleId = agentDailySettlement.getSid();
        }
        /*****************************************二、生成CA代理商的结算信息-END*******************************************/



        /*****************************************三、查询并转存该CA代理商的订单（结算订单）-START*******************************************/
        /**
         * 特别说明，思路同商户月度结算的订单统计思路二
         * 1、查询出结算日有效交易订单（有效交易订单=非退款订单 + 非结算日退款订单）
         * 20200107改造：由于订单加上了完成状态，代理商结算需要限定为结算完成状态的订单，需要对结算订单查询逻辑进行改造
         * 2、查询出结算日内退款但交易日期在之前360天内的订单
         * 3、查询出结算日交易并且结算日退款的订单
         */
        // 1、查询出结算日有效交易订单（有效交易订单=非退款订单 + 非结算日退款订单）
        //List<AgentSettlementOrder> agentDailyOrderList = orderQuery.queryCagentDailyOrderList(agent,
        //        dayStartTime, dayEndTime, yesterday);
        // 1、20200107改造：查询出结算月有效交易订单（有效交易订单=完成订单 + 结算月交易但非结算月退款订单）
        List<AgentSettlementOrder> agentDailyOrderList = orderQuery.queryCagentDailyOrderList1(agent,
                dayStartTime, dayEndTime, yesterday);

        // 2、查询出结算日内退款但交易日期在之前360天内的订单
        List<AgentSettlementOrder> agentDailyRefundBeforeOrderList = orderQuery
                .queryCagentDailyRefundBeforeOrderList(agent, dayStartTime, dayEndTime, yesterday);

        // 3、查询出结算日交易并且结算日退款的订单
        List<AgentSettlementOrder> agentDailyRefundOrderList = orderQuery.queryCagentDailyRefundOrderList(agent,
                dayStartTime, dayEndTime, yesterday);

        // 4、结算订单加工和保存-start
        // 4-1、结算月有效订单加工（结算、计算vatFee等）
        settleOrderList(agentDailyOrderList, agentStoreMaps, settleId, false);

        // 4-2、结算月退款的之前月份订单加工
        settleOrderList(agentDailyRefundBeforeOrderList, agentStoreMaps, settleId, true);

        // 4-3、结算月交易并且结算月退款的订单加工
        settleOrderList(agentDailyRefundOrderList, agentStoreMaps, settleId, false);

        // 4-4、复制一份 结算月交易并且结算月退款的订单
        List<AgentSettlementOrder> copyAgentDailyRefundOrderList = new ArrayList<AgentSettlementOrder>();
        for(AgentSettlementOrder agentSettlementOrder : agentDailyRefundOrderList){
            AgentSettlementOrder newAso = new AgentSettlementOrder();
            try {
                BeanUtils.copyProperties(newAso, agentSettlementOrder);
            } catch (IllegalAccessException | InvocationTargetException e) {
//                log.error("【CA代理商每日结算】复制一份::::结算日交易并且结算日退款的订单出现异常：" + e.getMessage(), e);
            }
            copyAgentDailyRefundOrderList.add(newAso);
        }

        // 列表变负
        copyAgentDailyRefundOrderList.stream().forEach(agentSettlementOrder -> {
            agentSettlementOrder.setOrderAmount(agentSettlementOrder.getOrderAmount().negate());
            agentSettlementOrder.setPlFee(agentSettlementOrder.getPlFee().negate());
            agentSettlementOrder.setAgtFee(agentSettlementOrder.getAgtFee().negate());
            agentSettlementOrder.setVatFee(agentSettlementOrder.getVatFee().negate());
            agentSettlementOrder.setIsRefund(1);
        });

        // 4-5、批量新增结算订单
        agentDailyOrderList.addAll(agentDailyRefundBeforeOrderList);
        agentDailyOrderList.addAll(agentDailyRefundOrderList);
        agentDailyOrderList.addAll(copyAgentDailyRefundOrderList);
        int orderCount = agentDailyOrderList.size();
        if(orderCount == 0){
            // 如果结算订单数量为0，下面的步骤不需要进行计算了，结算任务结束
            return;
        }
        int count = agentRepository.batchInsertAgentOrder(settleId, agentDailyOrderList);
//        log.info("【CA代理商月度结算】CA代理商结算订单数量为：{}", count);
        // 4、结算订单加工和保存-end
        /*****************************************三、查询并转存该CA代理商的订单（结算订单）-END*******************************************/


        /*****************************************四、更新结算信息（订单数量、结算金额、代理商结算金额、VAT金额）-START*******************************************/
        // 1、订单数量
        // 订单数量
        int orderFinalCount = orderCount;

        // 2、结算订单金额、代理商结算总金额、VAT金额
        Map<String, Object> agentSettleInfo = agentQuery.getAgentSettleInfo(settleId);
        BigDecimal orderTotalAmount = (BigDecimal) agentSettleInfo.get("orderTotalAmount");
        BigDecimal agtTotalFee = (BigDecimal) agentSettleInfo.get("agtTotalFee");
        BigDecimal vatTotalFee = (BigDecimal) agentSettleInfo.get("vatTotalFee");

        // 3、更新代理商结算信息（订单数量、结算金额、代理商结算金额、VAT金额）
        agentDailySettlement.setOrderCount(Long.valueOf(orderFinalCount));
        agentDailySettlement.setSettleSum(orderTotalAmount);
        agentDailySettlement.setAgtFee(agtTotalFee);
        agentDailySettlement.setAgtVatFee(vatTotalFee);
        int upCount = agentRepository.updateAgentDailySettlement(agentDailySettlement);
        /*****************************************四、更新结算信息（订单数量、结算金额、代理商结算金额、VAT金额）-END*******************************************/


        /*****************************************五、写入代理商结算明细-START*******************************************/
        List<AgentSettlementDetail> agentSettlementDetails = agentQuery.getAgentDailySettlementDetailList(settleId);
        // 重新构建agentStoreMaps用于拉姆他表达式
        final Map<Long, AgentStoreMap> reAgentStoreMaps = agentStoreMaps;
        agentSettlementDetails.forEach(agentSettlementDetail -> {
            // CA代理商结算费率计算-start
            AgentStoreMap agentStoreMap = reAgentStoreMaps.get(agentSettlementDetail.getStoreId());
            // 设置过
            if (agentStoreMap != null) {
                agentSettlementDetail.setAgtRate(agentStoreMap.getAgtChargeRate());
                agentSettlementDetail.setAgtPercent(agentStoreMap.getAgtChargePercent());
            }
            // 未设置过
            else {
                // CA代理商的结算费率为商户结算费率乘以默认费率
                agentSettlementDetail.setAgtRate(
                        new BigDecimal(agentSettlementDetail.getPlRate())
                                .multiply(new BigDecimal(CommonConstants.AGENT_DEFAULT_RATE))
                                .divide(new BigDecimal(100))
                                .setScale(4,BigDecimal.ROUND_UP)
                );
                agentSettlementDetail.setAgtPercent(CommonConstants.AGENT_DEFAULT_PERSENT);
            }
            // CA代理商结算费率计算-end
        });
        // 批量新增结算明细
        int dCount = agentRepository.batchInsertAgentOrderDetail(settleId, agentSettlementDetails);
//        log.info("【CA代理商每日结算】CA代理商结算的明细数量为：{}", dCount);
        /*****************************************五、写入代理商结算明细-END*******************************************/
    }
}