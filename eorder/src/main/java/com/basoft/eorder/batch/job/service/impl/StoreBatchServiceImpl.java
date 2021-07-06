package com.basoft.eorder.batch.job.service.impl;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.batch.job.service.IStoreBatchService;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.order.StoreDailySettlement;
import com.basoft.eorder.domain.model.order.StoreDailySettlementBA;
import com.basoft.eorder.domain.model.order.StoreSettlement;
import com.basoft.eorder.domain.model.order.StoreSettlementBA;
import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;
import com.basoft.eorder.interfaces.query.OrderQuery;
import com.basoft.eorder.interfaces.query.ReviewDTO;
import com.basoft.eorder.interfaces.query.ReviewQuery;
import com.basoft.eorder.interfaces.query.SettleDTO;
import com.basoft.eorder.util.UidGenerator;
import com.beust.jcommander.internal.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StoreBatchServiceImpl implements IStoreBatchService {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderQuery orderQuery;

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewQuery reviewQuery;

    /*****************************************I.商户计费费率信息-START*********************************************/
    /**
     * 处理并同步商户指定年月的计费信息
     * <p>
     * 1、商户计费信息，支持随意设置，但是当前月设置的计费信息次月生效，所以当前月修改设置的计费信息记录独立于当前月计费信息存储
     * （1）当前月计费信息存储于store表中。
     * （2）当前月修改设置的计费信息（即次月的计费信息）存储于store_charge_info_record表中。多次修改只有最后一条是useYn为1，finalYn为1，
     * 之前修改的useYn为0，finalYn为0
     * <p>
     * 2、AdminCMS显示的商户计费信息为当前月的计费信息和次月生效的计费信息。
     * 说明：如果次月没有单独设置计费信息（取自store_charge_info_record表），则说明对次月计费信息没有进行修改，则次月计费信息
     * 延续当前月计费信息
     * <p>
     * 3、处理内容（方案一）：
     * 鉴于上述描述，次月【此处我们设定次月为2019年9月，下同。】的月最初，需要将次月即9月的计费信息（8月内修改的）
     * 从store_charge_info_record表同步到store表中，在同步前需要定格store表中的计费信息。
     * <p>
     * 处理逻辑：
     * （1）将store表中的计费信息定格到store_charge_info_record表中，即确定8月份的有效计费信息。
     * 查询store_charge_info_record表是否存在年月为201908，useYn为2，finalYn为1的记录。没有则将store表计费信息插入
     * 到store_charge_info_record表中，同时年月设置为201908，useYn为3，finalYn为1。
     * （2）查询次月的所有计费信息，并过滤出可用的，且最后一次修改的计费信息（useYn为1，finalYn为1）。
     * 存在有效计费信息修改记录则更新至store表，没有有效计费信息修改记录则不进行更新，商户延续原有计费信息
     *
     * ！！!该方案为实际采用的，并进行了下面的编码。@@@该方案在代理商月度结算重写了：添加了方案二的逻辑。
     *  <p>
     *  4、处理内容（方案二）：
     *  简单方案：9月初，查询store_charge_info_record表中是否存在8月份修改的并且9月份要生效的计费信息（useYn为1,finalYn为1）。
     *  （1）有，则将9月份生效的计费信息同步到Store表，同时修改store_charge_info_record表中8月份修改的并且9月份生效的计费信息至
     *  useYn为2，final为1，完毕。
     *  （2）没有，则将主表store表中的计费信息拷贝到store_charge_info_record表中，月份设置为9月份切useYn为3，final为1。
     *  该方案简洁明了。
     * @param store     商户
     * @param lastYear  上个月所在年
     * @param lastMonth 上个月
     * @param year      当前月所在年
     * @param month     当前月
     */
    @Transactional
    public void dealStoreChargeInfoPerMonth(Store store, int lastYear, int lastMonth, int year, int month) throws Exception {
        /********************************************处理逻辑(1)-START************************************************/
        // 从store_charge_info_record表中获取201908的计费信息
        List<StoreChargeInfoRecord> lastChargeInfos = storeRepository.getStoreChargeInfoRecord(store.id(), lastYear, lastMonth);
        // 存在201908的计费信息记录
        if (lastChargeInfos != null && lastChargeInfos.size() > 0) {
            // 过滤UseYn为2 FinalYn为1的记录且过滤UseYn为3 FinalYn为1的记录，防止定时任务重复跑后UseYn为3 FinalYn为1的被重复添加
            List<StoreChargeInfoRecord> lastMonthRecords = lastChargeInfos.stream()
                    .filter(storeChargeInfoRecord ->
                            ((storeChargeInfoRecord.getUseYn() == 2 || storeChargeInfoRecord.getUseYn() == 3) && storeChargeInfoRecord.getFinalYn() == 1)
                    )
                    .collect(Collectors.toList());

            //存在UseYn为2，FinalYn为1的记录
            //或 存在UseYn为3，FinalYn为1的记录
            // ！！！此时也必然不存在UseYn为1 FinalYn为1的记录，因为在201908月初该定时任务会将存在的UseYn为1 FinalYn为1的记录修改为UseYn为2 FinalYn为1
            if (lastMonthRecords != null && lastMonthRecords.size() > 0) {
                // 8月份存在有效的用于结算的计费信息
                // NOTHING TO DO
            }
            //不存在UseYn为2 FinalYn为1的记录，即8月份不存在有效的用于结算的计费信息
            //也不存在UseYn为3 FinalYn为1的记录，即8月份不存在有效的用于结算的计费信息
            // 此时需要将主计费信息，即store表中的计费信息同步到store_charge_info_record表中，并设定年月为201908
            // UseYn设定为3 FinalYn为1
            // ！！！该分支貌似永远不会走到
            else {
                storeRepository.saveStoreChargeInfo(buildLastChargeInfo(store, lastYear, lastMonth));
            }
        }
        // 不存在201908的计费信息记录，即8月份不存在有效的用于结算的计费信息
        // 此时需要将主计费信息，即store表中的计费信息同步到store_charge_info_record表中，并设定年月为201908
        // UseYn设定为3 FinalYn为1
        else {
            storeRepository.saveStoreChargeInfo(buildLastChargeInfo(store, lastYear, lastMonth));
        }
        /********************************************处理逻辑(1)-END************************************************/


        /********************************************处理逻辑(2)-START************************************************/
        // 从store_charge_info_record表中获取201909的计费信息
        // 为便于扩展，此处取出所有修改记录，不再查询时就过滤出有效的计费信息
        List<StoreChargeInfoRecord> curChargeInfos = storeRepository.getStoreChargeInfoRecord(store.id(), year, month);

        // 计费信息没有修改记录
        if (curChargeInfos == null || curChargeInfos.size() == 0) {
            // 该商户计费信息没有变化，延续过往计费方式
            // NOTHING TO DO
        }
        // 计费信息存在修改记录
        else {
            // 过滤出次月并且最后的有效版本（useYn为1且finalYn为1）
            List<StoreChargeInfoRecord> currentMonthRecords = curChargeInfos.stream()
                    .filter(storeChargeInfoRecord ->
                            (storeChargeInfoRecord.getUseYn() == 1 && storeChargeInfoRecord.getFinalYn() == 1)
                    )
                    .collect(Collectors.toList());

            // 过滤结果为空，计费信息设置异常
            if (currentMonthRecords == null || currentMonthRecords.size() == 0) {
                // 计费信息设置异常
                // NOTHING TO DO
            }
            // 过滤结果不为空
            else {
                // 取且仅取第一条过滤后的记录，当然currentMonthRecords列表中也应该仅有一条
                StoreChargeInfoRecord chargeInfo = currentMonthRecords.get(0);
                storeRepository.saveCurMonthMainChargeInfo(store.id(),
                        chargeInfo.getChargeType(),
                        chargeInfo.getChargeRate(),
                        chargeInfo.getChargeFee());

                // 定格次月即201909并且最后的有效版本（useYn为1且finalYn为1）为useYn为2且finalYn为1
                storeRepository.updateStoreCharge(
                        new StoreChargeInfoRecord.Builder()
                                .storeID(store.id())
                                .chargeYearMonth(getYearMonthStringNoLine(year, month))
                                .chargeYear(year)
                                .chargeMonth(month)
                                .useYn(2)
                                .finalYn(1).build()
                );
            }
        }
        /********************************************处理逻辑(2)-START************************************************/
    }
    /**
     * 优化逻辑，只处理定时任务执行这个月（无法兼顾存量数据），不用考虑结算月的计费信息：
     * 1、上月改了
     * 计费信息修改记录表的计费信息（useYn=2 finalYn=1）同步到Store表，定格上月改的为useYn=2 finalYn=1
     * 2、上月没改
     * 将Store表计费信息同步到修改记录表useYn=3 finalYn=1
     */

    /**
     * 构造计费信息
     *
     * @return
     */
    private StoreChargeInfoRecord buildLastChargeInfo(Store store, int lastYear, int lastMonth) {
        return new StoreChargeInfoRecord.Builder()
                .storeID(store.id())
                .chargeYearMonth(getYearMonthStringNoLine(lastYear, lastMonth))
                .chargeYear(lastYear)
                .chargeMonth(lastMonth)
                .chargeType(store.chargeType())
                .chargeRate(store.chargeRate())
                .chargeFee(store.chargeFee())
                .useYn(3)
                .finalYn(1).build();
    }
    /*****************************************I.商户计费费率信息-END*********************************************/






    /*****************************************II.PG交易类商户月度结算-START*********************************************/
    /**
     * PG交易类商户月度结算
     *
     * @param pgStore
     * @param lastYear
     * @param lastMonth
     * @param year 暂时无用
     * @param month 暂时无用
     */
    @Transactional
    public void pgStoreMonthSettle(Store pgStore, int lastYear, int lastMonth, int year,
                                 int month, String monthStartTime, String monthEndTime) throws Exception {
        /*****************************************一、查询商户结算信息-START*******************************************/
        /**
         * 查询结算信息
         *
         * 查询结算月的计费信息，假定2019年08月的计费信息
         */
        StoreChargeInfoRecord lastMonthChargeInfo = storeRepository.getStoreMonthChargeInfo(pgStore.id(), lastYear, lastMonth);
        if (lastMonthChargeInfo == null) {
            return;
        }
//        log.info("【PG交易类商户月度结算】【{}】商户结算依据信息：{}", pgStore.id(), lastMonthChargeInfo);
        /*****************************************一、查询商户结算信息-END*******************************************/


        /*****************************************二、查询商户结算月有效订单总金额-START*******************************************/
        /**
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         * （3）商户结算月有效交易订单定义如下：
         *  有效交易订单=非退款订单 + 非结算月退款订单
         */
        // 思路二：查询出结算月有效交易订单（有效交易订单=非退款订单 + 非结算月退款订单）总金额C
        Map<String, Object> storeEffectiveOrderAmountSum = orderQuery.getPGStoreEffectiveOrderAmountSum(pgStore.id(),
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        Long orderCount = (Long) storeEffectiveOrderAmountSum.get("orderCount");
        BigDecimal orderTotalAmount = (BigDecimal) storeEffectiveOrderAmountSum.get("orderTotalAmount");
//        log.info("【PG交易类商户月度结算】【{}】有效订单数量和总金额信息如下：订单数量为{},订单总金额为{}", pgStore.id(), orderCount, orderTotalAmount);
        /*****************************************二、查询商户结算月有效订单总金额-END*******************************************/


        /*****************************************三、计算商户结算月的平台服务费-START*******************************************/
        /**
         * 思路一：查询出结算月所有订单（包含退款订单）总金额A；查询出结算月日期所有退款订单（结算月退款订单+前推12个月退款订单）总金额B。
         * A-B得到的金额就是计算平台服务费的金额。
         *
         * 思路二：查询出结算月有效交易订单（有效交易订单=非退款订单 + 非结算月退款订单）总金额C，查询出自结算月前推12个月退款订单总金额D。
         * C-D得到的金额就是计算平台服务费的金额。
         * 第一步中查询的订单金额即是C
         *
         * 解读非结算月退款订单：
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         * （3）非结算月退款订单就是2019.08.01 00:00:00-2019.08.31 23:59:59生成的订单并且退款了，但是退款日期为2019年09月内的日期，
         *  即2019年09月01日0点起至月度结算任务运行期间退的订单。
         *
         * #############采用思路二计算商户结算月的平台服务费
         */

        /*#########################思路一START#################################################################*/
        /**
         * 服务费结算思路一
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         *
         * 结算月2019.08结算总金额为以下三项之和
         * （1）+加
         *  2019年08月月内（即2019-08-01 00:00:00至2019-08-31 23:59:59）所有交易订单总金额，
         *  状态包括所有状态（无论是否退款，以及退款日期是啥，都不管）
         * （2）-减
         *  2019年08月内（即2019-08-01 00:00:00至2019-08-31 23:59:59）交易订单中退款的
         *  ，并且退款日期为2019年08月内（即2019-08-01 00:00:00至2019-08-31 23:59:59）
         * （3）-减
         *  2019年08月往前推12个月内在2019年08月内（即2019-08-01 00:00:00至2019-08-31 23:59:59）退款的订单总额
         */

        /**
         * 查询商户结算月有效的待结算订单总金额（含退款订单）
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         */
        /*Map<String, Object> storeOrderAllAmountSum = orderQuery.getStoreOrderAllAmountSum(store.id(), monthStartTime, monthEndTime);
        int allOrderCount = (Integer) storeOrderAllAmountSum.get("orderCount");
        BigDecimal allOrderTotalAmount = (BigDecimal) storeOrderAllAmountSum.get("orderTotalAmount");
        log.info("【商户月度结算】待结算订单总金额信息如下：订单数量为{},订单总金额为{}", allOrderCount, allOrderTotalAmount);*/


        /**
         * 查询指定商户当前结算月和当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
         *
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         * （3）结算月往前12个月：2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
         *            2019.01，2018.12，2018.11，2018.10，2018.09，2018.08
         * （4）退款日期为结算月内的日期：退款日期为2019.08.01-2019.08.31
         * （5）特别说明：结算月的退款成功订单还可能存在2019.09.01-2019.09.05结算时间点，此
         *  部分订单将会在2019.09结算月进行计算哦，也就是将在2019.10.05号进行结算。
         */
        /*Map<String, Object> storeRefundOrderAmountSum = orderQuery.getPGStoreRefundOrderAmountSum(store.id(),
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        int refundOrderCount = (Integer) storeRefundOrderAmountSum.get("refundOrderCount");
        BigDecimal refundOrderTotalAmount = (BigDecimal) storeRefundOrderAmountSum.get("refundOrderTotalAmount");
        log.info("【商户月度结算】有效退款订单总金额信息如下：退款订单数量为{},退款订单总金额为{}", refundOrderCount, refundOrderTotalAmount);*/
        /*#########################思路一END#################################################################*/


        /*#########################思路二START#################################################################*/
        /**
         * 查询指定商户当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
         * 强调：不含结算月内的任何退款订单。
         *
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         * （3）结算月往前12个月：2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
         *            2019.01，2018.12，2018.11，2018.10，2018.09，2018.08
         * （4）退款日期为结算月内的日期：退款日期为2019.08.01-2019.08.31
         *
         *  查询2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
         *  2019.01，2018.12，2018.11，2018.10，2018.09，2018.08这12个月内的退款订单，并且退款日期为2019.08.01-2019.08.31
         */
        Map<String, Object> storeRefundOrderAmountSum = orderQuery.getPGStoreRefundBeforeOrderAmountSum(pgStore.id(),
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        Long refundOrderCount = (Long) storeRefundOrderAmountSum.get("refundOrderCount");
        BigDecimal refundOrderTotalAmount = (BigDecimal) storeRefundOrderAmountSum.get("refundOrderTotalAmount");
//        log.info("【PG交易类商户月度结算】【{}】之前12个月内有效退款订单数量和退款总金额信息如下：退款订单数量为{},退款总金额为{}", pgStore.id(), refundOrderCount, refundOrderTotalAmount);

        // 计算商户结算月平台服务费
        BigDecimal plMinFee = new BigDecimal(lastMonthChargeInfo.getChargeFee());
        Map<String, BigDecimal> plFees = calculatePlFee(lastMonthChargeInfo, orderTotalAmount, refundOrderTotalAmount);
        BigDecimal plServiceFee = plFees.get("SF");
        BigDecimal plFinalFee = plFees.get("FF");
//        log.info("【PG交易类商户月度结算】【{}】商户平台结算服务费和最终服务费分别为：平台结算服务费SF为{},平台最终服务费FF为{}", pgStore.id(), plServiceFee, plFinalFee);
        /*#########################思路二END#################################################################*/
        /*****************************************三、计算结算月的平台服务费-END*******************************************/

        /*****************************************四、查询PG结算结果-START*******************************************/
        String pgDate = "noPGData";
        BigDecimal pgSum = new BigDecimal(0);
        BigDecimal pgServiceFee = new BigDecimal(0);
        try {
            Map param = Maps.newHashMap();
            param.put("storeId", pgStore.id());
            param.put("startTime", monthStartTime);
            param.put("endTime", monthEndTime);
            SettleDTO settleDTO = orderQuery.getPGSettleByMap(param);
//            log.info("【PG交易类商户月度结算】【" + pgStore.id() +"】获取到的PG结算信息::{}", settleDTO);

            // PG结算起止日期
            pgDate = (settleDTO.getPgFrDt() == null ? "" : settleDTO.getPgFrDt())
                    + "~" + (settleDTO.getPgToDt() == null ? "" : settleDTO.getPgToDt());

            // PG结算金额
            String pgAmount = settleDTO.getPgAmount();
            if (pgAmount == null || "".equals(pgAmount.trim())) {
                pgSum = new BigDecimal(0);
            } else {
                try {
                    pgSum = new BigDecimal(pgAmount);
                } catch (Exception e) {
                    pgSum = new BigDecimal(0);
//                    log.error("【PG交易类商户月度结算】【" + pgStore.id() + "】PG结算金额转换异常：" + e.getMessage(), e);
                }
            }

            // PG结算手续费
            String pgFee = settleDTO.getPgFee();
            if (pgFee == null || "".equals(pgFee.trim())) {
                pgServiceFee = new BigDecimal(0);
            } else {
                try {
                    pgServiceFee = new BigDecimal(pgFee);
                } catch (Exception e) {
                    pgServiceFee = new BigDecimal(0);
//                    log.error("【PG交易类商户月度结算】【" + pgStore.id() + "】PG结算手续费转换异常：" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
//            log.error("【PG交易类商户月度结算】【{}】获取PG结算信息异常：" + e.getMessage(), e);
        }
        /*****************************************四、查询PG结算结果-END*******************************************/

        /*****************************************五、存储商户结算月结算结果-START*******************************************/
        // 构造结算信息
        // StoreSettlement storeSettlement = buildStoreSettlement(lastMonthChargeInfo, storeOrderAmountSum, storeRefundOrderAmountSum);
        StoreSettlement storeSettlement = new StoreSettlement.Builder()
                .sid(uidGenerator.generate(BusinessTypeEnum.STORE_SETTLEMENT))
                .storeID(pgStore.id())
                .settleYearMonth(getYearMonthStringNoLine(lastYear, lastMonth))
                .settleYear(lastYear)
                .settleMonth(lastMonth)
                .startDT(monthStartTime.substring(0,10))
                .endDT(monthEndTime.substring(0,10))
                .settleType(lastMonthChargeInfo.getChargeType())
                .settleRate(lastMonthChargeInfo.getChargeRate())
                .settleFee(lastMonthChargeInfo.getChargeFee())
                .orderCount(orderCount)
                .settleSum(orderTotalAmount)
                .pgDate(pgDate)
                .pgSum(pgSum)
                .pgServiceFee(pgServiceFee)
                //.plDate() // SQL中写入
                .plMinFee(plMinFee)
                .plServiceFee(plServiceFee)
                .plFinalFee(plFinalFee)
                .build();

        // 删除商户结算月已存在的结算结果
        orderRepository.deleteStoreSettlement(pgStore.id(), lastYear, lastMonth);

        // 插入更新商户结算月的结算结果
        orderRepository.saveStoreSettlement(storeSettlement);
        /*****************************************五、存储商户结算月结算结果-END*******************************************/
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
     * 计算PG交易类或BA交易类商户结算月平台服务费
     * 说明：假定结算月的收费类型为CT
     * （1）CT为百分比
     * 商户结算月平台服务费SF=有效订单总金额*CT-商户结算月有效退款总金额*CT
     * 商户最终服务费FF = SF
     * <p>
     * （2）CT为百分比或最小服务费
     * 商户结算月平台服务费SF=有效订单总金额*CT-商户结算月有效退款总金额*CT
     * 最小服务费MF
     * 商户最终服务费FF为SF和MF中较大的值
     * <p>
     * （3）CT为按月
     * 商户结算月平台服务费SF = 固定值month_fee
     * 商户最终服务费FF = 固定值month_fee
     *
     * 特别说明：：：上述第一种类型和第二种类型的算式【商户结算月有效退款总金额*CT】中CT为结算月的百分比，
     * 这就存在一定的误差，描述如下：比如2019.09月结算2019.08月的订单，但是2019.08月内还对2019.07月的订单产生了退单。实际上返还
     * 2019.07月的服务费应该按照2019.07月的结算标准即百分比计算，实际上是按照2019.08月的结算标准即百分比计算的。
     * 如果2019.07月结算标准和2019.08月的计算标准一样还好，如果不一样，则返还时就存在误差，误差如下：
     * 商户吃亏-2019.07为5%，2019.08为%3  或者  平台吃亏-2019.07为3%，2019.08为%5。
     * //to1do 如果完全规避这种误差需要查询出201907月的结算标准，然后按照这个标准进行返还。目前暂且不实现。
     *
     * @param lastMonthChargeInfo    商户结算标准
     * @param orderTotalAmount       商户结算月有效订单总金额
     * @param refundOrderTotalAmount 商户结算月有效退款总金额（结算月前12个月的退款，退款日期为结算月日期）
     * @return
     */
    private Map<String, BigDecimal> calculatePlFee(StoreChargeInfoRecord lastMonthChargeInfo, BigDecimal orderTotalAmount, BigDecimal refundOrderTotalAmount) {
        Map<String, BigDecimal> plFees = Maps.newHashMap();
        int storeChargeType = lastMonthChargeInfo.getChargeType();
        // 百分比
        if (CommonConstants.STORE_CHARGE_TYPE_RATE == storeChargeType) {
            BigDecimal sf = orderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100)))
                    .subtract(refundOrderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100))));
            plFees.put("SF", sf);
            plFees.put("FF", sf);
            return plFees;
        }
        // 百分比或最小服务费
        else if (CommonConstants.STORE_CHARGE_TYPE_RATE_MIN_FEE == storeChargeType) {
            BigDecimal sf = orderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100)))
                    .subtract(refundOrderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100))));
            BigDecimal mf = new BigDecimal(lastMonthChargeInfo.getChargeFee());
            plFees.put("SF", sf);
            // sf大于mf
            if (sf.compareTo(mf) == 1) {
                plFees.put("FF", sf);
            }
            // sf等于mf
            else if (sf.compareTo(mf) == 0) {
                plFees.put("FF", mf);
            }
            // sf小于mf
            else if (sf.compareTo(mf) == -1) {
                plFees.put("FF", mf);
            }
            return plFees;
        }
        // 按月
        else if (CommonConstants.STORE_CHARGE_TYPE_MONTHLY == storeChargeType) {
            plFees.put("SF", new BigDecimal(lastMonthChargeInfo.getChargeFee()));
            plFees.put("FF", new BigDecimal(lastMonthChargeInfo.getChargeFee()));
            return plFees;
        } else {
            plFees.put("SF", new BigDecimal(0));
            plFees.put("FF", new BigDecimal(0));
            return plFees;
        }
    }

    /**
     * PG交易类商户月度结算(手工)
     * 同定时任务结算逻辑完全相同，@see storeMonthSettle
     *
     * @param store
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     * @param monthStartTime
     * @param monthEndTime
     * @param target
     */
    public void pgStoreMonthSettleManual(Store store, int lastYear, int lastMonth, int year,
                                       int month, String monthStartTime, String monthEndTime, String target) throws Exception {
        /*****************************************一、查询商户结算信息-START*******************************************/
        StoreChargeInfoRecord lastMonthChargeInfo = storeRepository.getStoreMonthChargeInfo(store.id(), lastYear, lastMonth);
        if (lastMonthChargeInfo == null) {
            return;
        }
//        log.info("【MANUAL】【PG交易类商户月度结算】【{}】商户结算依据信息：{}", store.id(), lastMonthChargeInfo);
        /*****************************************一、查询商户结算信息-END*******************************************/


        /*****************************************二、查询商户结算月有效订单总金额-START*******************************************/
        Map<String, Object> storeEffectiveOrderAmountSum = orderQuery.getPGStoreEffectiveOrderAmountSum(store.id(),
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        Long orderCount = (Long) storeEffectiveOrderAmountSum.get("orderCount");
        BigDecimal orderTotalAmount = (BigDecimal) storeEffectiveOrderAmountSum.get("orderTotalAmount");
//        log.info("【MANUAL】【PG交易类商户月度结算】【{}】有效订单数量和总金额信息如下：订单数量为{},订单总金额为{}", store.id(), orderCount, orderTotalAmount);
        /*****************************************二、查询商户结算月有效订单总金额-END*******************************************/


        /*****************************************三、计算商户结算月的平台服务费-START*******************************************/
        Map<String, Object> storeRefundOrderAmountSum = orderQuery.getPGStoreRefundBeforeOrderAmountSum(store.id(),
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        Long refundOrderCount = (Long) storeRefundOrderAmountSum.get("refundOrderCount");
        BigDecimal refundOrderTotalAmount = (BigDecimal) storeRefundOrderAmountSum.get("refundOrderTotalAmount");
//        log.info("【MANUAL】【PG交易类商户月度结算】【{}】之前12个月内有效退款订单数量和退款总金额信息如下：退款订单数量为{},退款总金额为{}", store.id(), refundOrderCount, refundOrderTotalAmount);

        // 计算商户结算月平台服务费
        BigDecimal plMinFee = new BigDecimal(lastMonthChargeInfo.getChargeFee());
        Map<String, BigDecimal> plFees = calculatePlFee(lastMonthChargeInfo, orderTotalAmount, refundOrderTotalAmount);
        BigDecimal plServiceFee = plFees.get("SF");
        BigDecimal plFinalFee = plFees.get("FF");
//        log.info("【MANUAL】【商户月度结算】【{}】商户平台结算服务费和最终服务费分别为：平台结算服务费SF为{},平台最终服务费FF为{}", store.id(), plServiceFee, plFinalFee);
        /*#########################思路二END#################################################################*/
        /*****************************************三、计算结算月的平台服务费-END*******************************************/

        /*****************************************四、查询PG结算结果-START*******************************************/
        String pgDate = "noPGData";
        BigDecimal pgSum = new BigDecimal(0);
        BigDecimal pgServiceFee = new BigDecimal(0);
        try {
            Map param = Maps.newHashMap();
            param.put("storeId", store.id());
            param.put("startTime", monthStartTime);
            param.put("endTime", monthEndTime);
            SettleDTO settleDTO = orderQuery.getPGSettleByMap(param);
//            log.info("【MANUAL】【商户月度结算】【" + store.id() +"】获取到的PG结算信息::{}", settleDTO);

            // PG结算起止日期
            pgDate = (settleDTO.getPgFrDt() == null ? "" : settleDTO.getPgFrDt())
                    + "~" + (settleDTO.getPgToDt() == null ? "" : settleDTO.getPgToDt());

            // PG结算金额
            String pgAmount = settleDTO.getPgAmount();
            if (pgAmount == null || "".equals(pgAmount.trim())) {
                pgSum = new BigDecimal(0);
            } else {
                try {
                    pgSum = new BigDecimal(pgAmount);
                } catch (Exception e) {
                    pgSum = new BigDecimal(0);
//                    log.error("【MANUAL】【商户月度结算】【" + store.id() + "】PG结算金额转换异常：" + e.getMessage(), e);
                }
            }

            // PG结算手续费
            String pgFee = settleDTO.getPgFee();
            if (pgFee == null || "".equals(pgFee.trim())) {
                pgServiceFee = new BigDecimal(0);
            } else {
                try {
                    pgServiceFee = new BigDecimal(pgFee);
                } catch (Exception e) {
                    pgServiceFee = new BigDecimal(0);
//                    log.error("【MANUAL】【商户月度结算】【" + store.id() + "】PG结算手续费转换异常：" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
//            log.error("【MANUAL】【商户月度结算】【{}】获取PG结算信息异常：" + e.getMessage(), e);
        }
        /*****************************************四、查询PG结算结果-END*******************************************/

        /*****************************************五、存储商户结算月结算结果-START*******************************************/
        // 构造结算信息
        // StoreSettlement storeSettlement = buildStoreSettlement(lastMonthChargeInfo, storeOrderAmountSum, storeRefundOrderAmountSum);
        StoreSettlement storeSettlement = new StoreSettlement.Builder()
                .sid(uidGenerator.generate(BusinessTypeEnum.STORE_SETTLEMENT))
                .storeID(store.id())
                .settleYearMonth(getYearMonthStringNoLine(lastYear, lastMonth))
                .settleYear(lastYear)
                .settleMonth(lastMonth)
                .startDT(monthStartTime.substring(0,10))
                .endDT(monthEndTime.substring(0,10))
                .settleType(lastMonthChargeInfo.getChargeType())
                .settleRate(lastMonthChargeInfo.getChargeRate())
                .settleFee(lastMonthChargeInfo.getChargeFee())
                .orderCount(orderCount)
                .settleSum(orderTotalAmount)
                .pgDate(pgDate)
                .pgSum(pgSum)
                .pgServiceFee(pgServiceFee)
                //.plDate() // SQL中写入
                .plMinFee(plMinFee)
                .plServiceFee(plServiceFee)
                .plFinalFee(plFinalFee)
                .build();

        if(CommonConstants.STORE_SETTLE_MANUAL_ZHENGSHI.equals(target)){
            // 删除商户结算月已存在的结算结果
            orderRepository.deleteStoreSettlement(store.id(), lastYear, lastMonth);

            // 插入更新商户结算月的结算结果
            orderRepository.saveStoreSettlement(storeSettlement);
        } else if(CommonConstants.STORE_SETTLE_MANUAL_CESHI.equals(target)){
            orderRepository.saveStoreSettlementManual(store.id(), lastYear, lastMonth, storeSettlement);
        }
        /*****************************************五、存储商户结算月结算结果-END*******************************************/
    }

    /**
     * PG交易类商户每日结算，今日结算昨日。
     *
     * @param pgStore
     * @param yesterday
     * @param dayStartTime
     * @param dayEndTime
     * @param today
     */
    public void pgStoreDailySettle(Store pgStore, String yesterday, String dayStartTime, String dayEndTime, String today) {
        /*****************************************一、查询商户当前月结算信息-START*******************************************/
        /**
         * 查询结算信息
         *
         * 查询当前月的商户计费信息，即存储于Store表。
         */
        StoreChargeInfoRecord currMonthChargeInfo = new StoreChargeInfoRecord.Builder()
                .storeID(pgStore.id())
                .chargeType(pgStore.chargeType())
                .chargeRate(pgStore.chargeRate())
                .chargeFee(pgStore.chargeFee())
                .build();
//        log.info("【PG交易类商户每日结算】【{}】商户结算依据信息：{}", pgStore.id(), currMonthChargeInfo);
        /*****************************************一、查询商户当前月结算信息-END*******************************************/


        /*****************************************二、查询商户昨日有效订单总金额-START*******************************************/
        /**
         * 商户昨日有效交易订单定义如下：
         *  有效交易订单=非退款订单 + 非昨日退款订单
         */
        Map<String, Object> storeDailyEffectiveOrderAmountSum = orderQuery.getPGStoreDailyEffectiveOrderAmountSum(pgStore.id(),
                dayStartTime, dayEndTime, yesterday);
        Long dailyOrderCount = (Long) storeDailyEffectiveOrderAmountSum.get("dailyOrderCount");
        BigDecimal dailyOrderTotalAmount = (BigDecimal) storeDailyEffectiveOrderAmountSum.get("dailyOrderTotalAmount");
//        log.info("【PG交易类商户每日结算】【{}】有效订单数量和总金额信息如下：订单数量为{},订单总金额为{}", pgStore.id(), dailyOrderCount, dailyOrderTotalAmount);
        /*****************************************二、查询商户昨日有效订单总金额-END*******************************************/


        /*****************************************三、计算商户每日的平台服务费-START*******************************************/
        /**
         * 前言：
         * 说明下面描述中的结算日即昨日。
         *
         * 思路一：
         * 1、查询出结算日所有订单（包含退款订单）总金额A。
         * 退款订单的含义特别强调：包含结算日交易结算日退款订单和结算日交易今日退款订单，因为定时任务是在凌晨2点跑的，
         * 0点到2点之间是存在退款的可能性的；
         * 2、查询出结算日进行退款的所有订单（结算日交易且结算日退款订单+前推360天交易且结算日退款订单）总金额B。
         * 3、A-B得到的金额就是计算平台服务费的金额。
         * 此时结算日交易今日退款的订单是算在总金额中的。
         *
         * 思路二：
         * 查询出结算日有效交易订单（有效交易订单=非退款订单 + 非结算日退款订单）总金额C，查询出自结算月前推12个月退款订单总金额D。
         * C-D得到的金额就是计算平台服务费的金额。
         * 第一步中查询的订单金额即是C
         *
         * 解读非结算日退款订单：
         * 假定如下：
         * （1）当前日为2019.09.05，即进行结算工作的日期。
         * （2）结算日为2019.09.04，即对2019年09月04日的订单进行结算。
         * （3）非结算日退款订单就是2019.09.04 00:00:00-2019.09.04 23:59:59生成的订单并且退款了，但是退款日期为2019年09月05日，
         *  即2019年09月05日0点起至每日结算任务运行期间退的订单。
         *
         * #############采用思路二计算商户每日的平台服务费
         */

        /*#########################思路二START#################################################################*/
        /**
         * 查询指定商户当前结算月往前360天内的退款成功订单的总金额，并且退款日期为结算日。
         * 强调：不含结算日内的任何退款订单。
         *
         * 假定如下：
         * （1）当前日为2019.09.05，即进行结算工作的日期。
         * （2）结算日为2019.09.04，即对2019年09月04日的订单进行结算。
         * （3）结算月往前360天
         * （4）退款日期为结算日内的日期：退款日期为2019.09.04
         */
        Map<String, Object> storeDailyRefundOrderAmountSum = orderQuery.getPGStoreDailyRefundBeforeOrderAmountSum(pgStore.id(),
                dayStartTime, dayEndTime, yesterday);
        Long dailyRefundOrderCount = (Long) storeDailyRefundOrderAmountSum.get("dailyRefundOrderCount");
        BigDecimal dailyRefundOrderTotalAmount = (BigDecimal) storeDailyRefundOrderAmountSum.get("dailyRefundOrderTotalAmount");
//        log.info("【PG交易类商户每日结算】【{}】之前360天内有效退款订单数量和退款总金额信息如下：退款订单数量为{},退款总金额为{}", pgStore.id(), dailyRefundOrderCount, dailyRefundOrderTotalAmount);

        // 计算商户结算月平台服务费
        BigDecimal plMinFee = new BigDecimal(currMonthChargeInfo.getChargeFee());
        Map<String, BigDecimal> plFees = calculateDailyPlFee(currMonthChargeInfo, dailyOrderTotalAmount, dailyRefundOrderTotalAmount);
        BigDecimal plServiceFee = plFees.get("SF");
        BigDecimal plFinalFee = plFees.get("FF");
//        log.info("【PG交易类商户每日结算】【{}】商户平台结算服务费和最终服务费分别为：平台结算服务费SF为{},平台最终服务费FF为{}", pgStore.id(), plServiceFee, plFinalFee);
        /*#########################思路二END#################################################################*/
        /*****************************************三、计算结算月的平台服务费-END*******************************************/

        /*****************************************四、查询PG结算结果-START*******************************************/
        String pgDate = "noPGData";
        BigDecimal pgSum = new BigDecimal(0);
        BigDecimal pgServiceFee = new BigDecimal(0);
        try {
            Map param = Maps.newHashMap();
            param.put("storeId", pgStore.id());
            param.put("startTime", dayStartTime);
            param.put("endTime", dayEndTime);
            SettleDTO settleDTO = orderQuery.getPGDailySettleByMap(param);
//            log.info("【PG交易类商户每日结算】【" + pgStore.id() +"】获取到的PG结算信息::{}", settleDTO);

            // PG结算起止日期
            pgDate = (settleDTO.getPgFrDt() == null ? "" : settleDTO.getPgFrDt())
                    + "~" + (settleDTO.getPgToDt() == null ? "" : settleDTO.getPgToDt());

            // PG结算金额
            String pgAmount = settleDTO.getPgAmount();
            if (pgAmount == null || "".equals(pgAmount.trim())) {
                pgSum = new BigDecimal(0);
            } else {
                try {
                    pgSum = new BigDecimal(pgAmount);
                } catch (Exception e) {
                    pgSum = new BigDecimal(0);
//                    log.error("【PG交易类商户每日结算】【" + pgStore.id() + "】PG结算金额转换异常：" + e.getMessage(), e);
                }
            }

            // PG结算手续费
            String pgFee = settleDTO.getPgFee();
            if (pgFee == null || "".equals(pgFee.trim())) {
                pgServiceFee = new BigDecimal(0);
            } else {
                try {
                    pgServiceFee = new BigDecimal(pgFee);
                } catch (Exception e) {
                    pgServiceFee = new BigDecimal(0);
//                    log.error("【PG交易类商户每日结算】【" + pgStore.id() + "】PG结算手续费转换异常：" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
//            log.error("【PG交易类商户每日结算】【{}】获取PG结算信息异常：" + e.getMessage(), e);
        }
        /*****************************************四、查询PG结算结果-END*******************************************/

        /*****************************************五、存储商户结算月结算结果-START*******************************************/
        // 构造结算信息
        StoreDailySettlement storeDailySettlement = new StoreDailySettlement.Builder()
                .sid(uidGenerator.generate(BusinessTypeEnum.STORE_DAILY_SETTLEMENT))
                .storeID(pgStore.id())
                .settleDate(yesterday)
                .settleType(currMonthChargeInfo.getChargeType())
                .settleRate(currMonthChargeInfo.getChargeRate())
                .settleFee(currMonthChargeInfo.getChargeFee())
                .orderCount(dailyOrderCount)
                .settleSum(dailyOrderTotalAmount)
                .pgDate(pgDate)
                .pgSum(pgSum)
                .pgServiceFee(pgServiceFee)
                //.plDate() // SQL中写入
                .plMinFee(plMinFee)
                .plServiceFee(plServiceFee)
                .plFinalFee(plFinalFee)
                .build();

        // 插入更新商户结算日的结算结果
        orderRepository.saveStoreDailySettlement(storeDailySettlement);
        /*****************************************五、存储商户结算月结算结果-END*******************************************/
    }

    /**
     * 计算商户每日结算平台服务费
     *
     * 说明：假定结算日的商户收费类型为CT
     *
     * （1）CT为百分比
     * 商户每日结算平台服务费SF=有效订单总金额*CT-商户结算日有效退款总金额*CT
     * 商户最终服务费FF = SF
     * <p>
     *
     * （2）CT为百分比或最小服务费
     * 商户结算日平台服务费SF=有效订单总金额*CT-商户结算日有效退款总金额*CT
     * 商户每日结算不考虑最小服务费MF和平台服务费SF大小关系。商户最终服务费FF显示为平台服务费SF
     * 商户最终服务费FF = SF
     * <p>
     *
     * （3）CT为按月
     * 商户结算月平台服务费SF = 固定值month_fee
     * 商户最终服务费FF = 固定值month_fee
     *
     * @param lastMonthChargeInfo    商户结算标准
     * @param orderTotalAmount       商户结算日有效订单总金额
     * @param refundOrderTotalAmount 商户结算日有效退款总金额（结算月前360天内的退款，退款日期为结算日）
     * @return
     */
    private Map<String, BigDecimal> calculateDailyPlFee(StoreChargeInfoRecord lastMonthChargeInfo, BigDecimal orderTotalAmount, BigDecimal refundOrderTotalAmount) {
        Map<String, BigDecimal> plFees = Maps.newHashMap();
        int storeChargeType = lastMonthChargeInfo.getChargeType();
        // 百分比
        if (CommonConstants.STORE_CHARGE_TYPE_RATE == storeChargeType) {
            BigDecimal sf = orderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100)))
                    .subtract(refundOrderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100))));
            plFees.put("SF", sf);
            plFees.put("FF", sf);
            return plFees;
        }
        // 百分比或最小服务费
        else if (CommonConstants.STORE_CHARGE_TYPE_RATE_MIN_FEE == storeChargeType) {
            BigDecimal sf = orderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100)))
                    .subtract(refundOrderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100))));
            BigDecimal mf = new BigDecimal(lastMonthChargeInfo.getChargeFee());
            plFees.put("SF", sf);

            /*
            // sf大于mf
            if (sf.compareTo(mf) == 1) {
                plFees.put("FF", sf);
            }
            // sf等于mf
            else if (sf.compareTo(mf) == 0) {
                plFees.put("FF", mf);
            }
            // sf小于mf
            else if (sf.compareTo(mf) == -1) {
                plFees.put("FF", mf);
            }
            */
            // 每日结算的最终服务费取百分比计算出来的服务费
            plFees.put("FF", sf);

            return plFees;
        }
        // 按月-商户每日结算显示服务费和最终服务费
        else if (CommonConstants.STORE_CHARGE_TYPE_MONTHLY == storeChargeType) {
            plFees.put("SF", new BigDecimal(lastMonthChargeInfo.getChargeFee()));
            plFees.put("FF", new BigDecimal(lastMonthChargeInfo.getChargeFee()));
            return plFees;
        } else {
            plFees.put("SF", new BigDecimal(0));
            plFees.put("FF", new BigDecimal(0));
            return plFees;
        }
    }
    /*****************************************II.PG交易类商户月度结算-END*********************************************/





    /*****************************************III.BA交易类商户月度结算-START*********************************************/
    /**
     * BA交易类商户月度结算(定时任务)
     *
     * 结算订单完成的订单，即状态为9的订单。
     *
     * @param baStore
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     * @param monthStartTime
     * @param monthEndTime
     */
    public void baStoreMonthSettle(Store baStore, int lastYear, int lastMonth, int year,
                                   int month, String monthStartTime, String monthEndTime) throws Exception {
        /*****************************************一、查询商户结算信息-START*******************************************/
        /**
         * 查询结算信息
         *
         * 查询结算月的计费信息，假定2019年08月的计费信息
         */
        StoreChargeInfoRecord lastMonthChargeInfo = storeRepository.getStoreMonthChargeInfo(baStore.id(), lastYear, lastMonth);
        if (lastMonthChargeInfo == null) {
            return;
        }
//        log.info("【BA交易类商户月度结算】【{}】商户结算依据信息：{}", baStore.id(), lastMonthChargeInfo);
        /*****************************************一、查询商户结算信息-END*******************************************/


        /*****************************************二、查询商户结算月有效订单总金额-START*******************************************/
        /**
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         * （3）商户结算月有效交易订单定义如下：
         *  有效交易订单=非退款订单（订单为9的非退款订单） + 非结算月退款订单
         */
        // 思路二：查询出结算月有效交易订单（有效交易订单=非退款订单（订单为9的非退款订单） + 非结算月退款订单）总金额C
        Map<String, Object> storeEffectiveOrderAmountSum = orderQuery.getBAStoreEffectiveOrderAmountSum(baStore.id(),
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        Long orderCount = (Long) storeEffectiveOrderAmountSum.get("orderCount");
        BigDecimal orderTotalAmount = (BigDecimal) storeEffectiveOrderAmountSum.get("orderTotalAmount");
//        log.info("【BA交易类商户月度结算】【{}】有效订单数量和总金额信息如下：订单数量为{},订单总金额为{}", baStore.id(), orderCount, orderTotalAmount);
        /*****************************************二、查询商户结算月有效订单总金额-END*******************************************/


        /*****************************************三、计算商户结算月的平台服务费-START*******************************************/
        /**
         * 思路一：查询出结算月所有完成订单（状态为9）和退款订单总金额A；
         * 查询出结算月内所有退款订单（结算月交易且结算月退款订单+前推12个月交易且结算月退款订单）总金额B。
         * A-B得到的金额就是计算平台服务费的金额。
         *
         * 思路二：查询出结算月有效交易订单（有效交易订单=非退款完成订单（状态为9） + 结算月交易但非结算月退款的退款订单）总金额C，查询出自结算月前推12个月退款订单总金额D。
         * C-D得到的金额就是计算平台服务费的金额。
         * 第一步中查询的订单金额即是C
         *
         * 解读非结算月退款订单：
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         * （3）非结算月退款订单就是2019.08.01 00:00:00-2019.08.31 23:59:59生成的订单并且退款了，但是退款日期为2019年09月内的日期，
         *  即2019年09月01日0点起至月度结算任务运行期间退的订单。
         *
         * #############采用思路二计算商户结算月的平台服务费
         */

        /*#########################思路二START#################################################################*/
        /**
         * 查询指定商户当前结算月往前12个月内交易但退款成功订单的总金额，并且退款日期为结算月内的日期。
         * 强调：不含结算月内交易的任何退款订单。
         *
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         * （3）结算月往前12个月：2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
         *            2019.01，2018.12，2018.11，2018.10，2018.09，2018.08
         * （4）退款日期为结算月内的日期：退款日期为2019.08.01-2019.08.31
         *
         *  查询2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
         *  2019.01，2018.12，2018.11，2018.10，2018.09，2018.08这12个月内的退款订单，并且退款日期为2019.08.01-2019.08.31
         */
        Map<String, Object> storeRefundOrderAmountSum = orderQuery.getBAStoreRefundBeforeOrderAmountSum(baStore.id(),
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        Long refundOrderCount = (Long) storeRefundOrderAmountSum.get("refundOrderCount");
        BigDecimal refundOrderTotalAmount = (BigDecimal) storeRefundOrderAmountSum.get("refundOrderTotalAmount");
//        log.info("【BA交易类商户月度结算】【{}】之前12个月内有效退款订单数量和退款总金额信息如下：退款订单数量为{},退款总金额为{}", baStore.id(), refundOrderCount, refundOrderTotalAmount);

        // 计算商户结算月平台服务费
        BigDecimal plMinFee = new BigDecimal(lastMonthChargeInfo.getChargeFee());
        Map<String, BigDecimal> plFees = calculatePlFee(lastMonthChargeInfo, orderTotalAmount, refundOrderTotalAmount);
        BigDecimal plServiceFee = plFees.get("SF");
        BigDecimal plFinalFee = plFees.get("FF");
//        log.info("【BA交易类商户月度结算】【{}】商户平台结算服务费和最终服务费分别为：平台结算服务费SF为{},平台最终服务费FF为{}", baStore.id(), plServiceFee, plFinalFee);
        /*#########################思路二END#################################################################*/
        /*****************************************三、计算结算月的平台服务费-END*******************************************/

        /*****************************************四、查询PG结算结果-START*******************************************/
        String pgDate = "noPGData";
        BigDecimal pgSum = new BigDecimal(0);
        BigDecimal pgServiceFee = new BigDecimal(0);
        try {
            /*特别说明start

            此处的PG结算信息：不仅仅包含完成订单（订单状态为9），也包括其他状态的订单，
            即该商户该结算月全量的PG结算信息，而服务费结算只涉及退款订单和完成订单。所以此处的PG结算信息不适合展现在
            BA交易类商户的服务费结算页面
            TODO 如何过滤出BA交易类商户的参与月度服务费结算订单，即过滤出状态为9的订单，以及退款订单
             */
            Map param = Maps.newHashMap();
            param.put("storeId", baStore.id());
            param.put("startTime", monthStartTime);
            param.put("endTime", monthEndTime);
            SettleDTO settleDTO = orderQuery.getPGSettleByMap(param);
//            log.info("【BA交易类商户月度结算】【" + baStore.id() +"】获取到的PG结算信息::{}", settleDTO);
            /*特别说明end*/

            // PG结算起止日期
            pgDate = (settleDTO.getPgFrDt() == null ? "" : settleDTO.getPgFrDt())
                    + "~" + (settleDTO.getPgToDt() == null ? "" : settleDTO.getPgToDt());

            // PG结算金额
            String pgAmount = settleDTO.getPgAmount();
            if (pgAmount == null || "".equals(pgAmount.trim())) {
                pgSum = new BigDecimal(0);
            } else {
                try {
                    pgSum = new BigDecimal(pgAmount);
                } catch (Exception e) {
                    pgSum = new BigDecimal(0);
//                    log.error("【BA交易类商户月度结算】【" + baStore.id() + "】PG结算金额转换异常：" + e.getMessage(), e);
                }
            }

            // PG结算手续费
            String pgFee = settleDTO.getPgFee();
            if (pgFee == null || "".equals(pgFee.trim())) {
                pgServiceFee = new BigDecimal(0);
            } else {
                try {
                    pgServiceFee = new BigDecimal(pgFee);
                } catch (Exception e) {
                    pgServiceFee = new BigDecimal(0);
//                    log.error("【BA交易类商户月度结算】【" + baStore.id() + "】PG结算手续费转换异常：" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
//            log.error("【BA交易类商户月度结算】【{}】获取PG结算信息异常：" + e.getMessage(), e);
        }
        /*****************************************四、查询PG结算结果-END*******************************************/

        /*****************************************五、存储商户结算月结算结果-START*******************************************/
        // 构造结算信息
        StoreSettlementBA storeSettlementBA = StoreSettlementBA.builder()
                .sid(uidGenerator.generate(BusinessTypeEnum.BA_STORE_SETTLEMENT))
                .storeID(baStore.id())
                .settleYearMonth(getYearMonthStringNoLine(lastYear, lastMonth))
                .settleYear(lastYear)
                .settleMonth(lastMonth)
                .startDT(monthStartTime.substring(0,10))
                .endDT(monthEndTime.substring(0,10))
                .settleType(lastMonthChargeInfo.getChargeType())
                .settleRate(lastMonthChargeInfo.getChargeRate())
                .settleFee(lastMonthChargeInfo.getChargeFee())
                .orderCount(orderCount)
                .settleSum(orderTotalAmount)
                .pgDate(pgDate)
                .pgSum(pgSum)
                .pgServiceFee(pgServiceFee)
                //.plDate() // SQL中写入
                .plMinFee(plMinFee)
                .plServiceFee(plServiceFee)
                .plFinalFee(plFinalFee)
                // BA交易金结算金额=订单总金额-平台最终服务费-PG结算服务费
                //.cashSettleSum(orderTotalAmount.subtract(plFinalFee).subtract(pgServiceFee))
                .cashSettleSum(orderTotalAmount.subtract(plFinalFee).subtract(orderTotalAmount.multiply(new BigDecimal(0.02))))
                .build();

        // 删除商户结算月已存在的结算结果
        orderRepository.deleteBAStoreSettlement(baStore.id(), lastYear, lastMonth);

        // 插入更新商户结算月的结算结果
        orderRepository.saveBAStoreSettlement(storeSettlementBA);
        /*****************************************五、存储商户结算月结算结果-END*******************************************/
    }

    /**
     * BA交易类商户每日结算
     *
     * @param baStore
     * @param yesterday
     * @param dayStartTime
     * @param dayEndTime
     * @param today
     */
    public void baStoreDailySettle(Store baStore, String yesterday, String dayStartTime, String dayEndTime, String today) {
        /*****************************************一、查询商户当前月结算信息-START*******************************************/
        /**
         * 查询结算信息
         *
         * 查询当前月的商户计费信息，即存储于Store表。
         */
        StoreChargeInfoRecord currMonthChargeInfo = new StoreChargeInfoRecord.Builder()
                .storeID(baStore.id())
                .chargeType(baStore.chargeType())
                .chargeRate(baStore.chargeRate())
                .chargeFee(baStore.chargeFee())
                .build();
//        log.info("【BA交易类商户每日结算】【{}】商户结算依据信息：{}", baStore.id(), currMonthChargeInfo);
        /*****************************************一、查询商户当前月结算信息-END*******************************************/


        /*****************************************二、查询商户昨日有效订单总金额-START*******************************************/
        /**
         * 商户昨日有效交易订单定义如下：
         *  有效交易订单=非退款订单 + 非昨日退款订单
         */
        Map<String, Object> storeDailyEffectiveOrderAmountSum = orderQuery.getBAStoreDailyEffectiveOrderAmountSum(baStore.id(),
                dayStartTime, dayEndTime, yesterday);
        Long dailyOrderCount = (Long) storeDailyEffectiveOrderAmountSum.get("dailyOrderCount");
        BigDecimal dailyOrderTotalAmount = (BigDecimal) storeDailyEffectiveOrderAmountSum.get("dailyOrderTotalAmount");
//        log.info("【BA交易类商户每日结算】【{}】有效订单数量和总金额信息如下：订单数量为{},订单总金额为{}", baStore.id(), dailyOrderCount, dailyOrderTotalAmount);
        /*****************************************二、查询商户昨日有效订单总金额-END*******************************************/


        /*****************************************三、计算商户每日的平台服务费-START*******************************************/
        /**
         * 前言：
         * 说明下面描述中的结算日即昨日。
         *
         * 思路一：
         * 1、查询出结算日所有订单（包含退款订单）总金额A。
         * 退款订单的含义特别强调：包含结算日交易结算日退款订单和结算日交易今日退款订单，因为定时任务是在凌晨2点跑的，
         * 0点到2点之间是存在退款的可能性的；
         * 2、查询出结算日进行退款的所有订单（结算日交易且结算日退款订单+前推360天交易且结算日退款订单）总金额B。
         * 3、A-B得到的金额就是计算平台服务费的金额。
         * 此时结算日交易今日退款的订单是算在总金额中的。
         *
         * 思路二：
         * 查询出结算日有效交易订单（有效交易订单=非退款订单 + 非结算日退款订单）总金额C，查询出自结算月前推12个月退款订单总金额D。
         * C-D得到的金额就是计算平台服务费的金额。
         * 第一步中查询的订单金额即是C
         *
         * 解读非结算日退款订单：
         * 假定如下：
         * （1）当前日为2019.09.05，即进行结算工作的日期。
         * （2）结算日为2019.09.04，即对2019年09月04日的订单进行结算。
         * （3）非结算日退款订单就是2019.09.04 00:00:00-2019.09.04 23:59:59生成的订单并且退款了，但是退款日期为2019年09月05日，
         *  即2019年09月05日0点起至每日结算任务运行期间退的订单。
         *
         * #############采用思路二计算商户每日的平台服务费
         */

        /*#########################思路二START#################################################################*/
        /**
         * 查询指定商户当前结算月往前360天内的退款成功订单的总金额，并且退款日期为结算日。
         * 强调：不含结算日内的任何退款订单。
         *
         * 假定如下：
         * （1）当前日为2019.09.05，即进行结算工作的日期。
         * （2）结算日为2019.09.04，即对2019年09月04日的订单进行结算。
         * （3）结算月往前360天
         * （4）退款日期为结算日内的日期：退款日期为2019.09.04
         */
        Map<String, Object> storeDailyRefundOrderAmountSum = orderQuery.getBAStoreDailyRefundBeforeOrderAmountSum(baStore.id(),
                dayStartTime, dayEndTime, yesterday);
        Long dailyRefundOrderCount = (Long) storeDailyRefundOrderAmountSum.get("dailyRefundOrderCount");
        BigDecimal dailyRefundOrderTotalAmount = (BigDecimal) storeDailyRefundOrderAmountSum.get("dailyRefundOrderTotalAmount");
//        log.info("【BA交易类商户每日结算】【{}】之前360天内有效退款订单数量和退款总金额信息如下：退款订单数量为{},退款总金额为{}", baStore.id(), dailyRefundOrderCount, dailyRefundOrderTotalAmount);

        // 计算商户结算月平台服务费
        BigDecimal plMinFee = new BigDecimal(currMonthChargeInfo.getChargeFee());
        Map<String, BigDecimal> plFees = calculateDailyPlFee(currMonthChargeInfo, dailyOrderTotalAmount, dailyRefundOrderTotalAmount);
        BigDecimal plServiceFee = plFees.get("SF");
        BigDecimal plFinalFee = plFees.get("FF");
//        log.info("【BA交易类商户每日结算】【{}】商户平台结算服务费和最终服务费分别为：平台结算服务费SF为{},平台最终服务费FF为{}", baStore.id(), plServiceFee, plFinalFee);
        /*#########################思路二END#################################################################*/
        /*****************************************三、计算结算月的平台服务费-END*******************************************/

        /*****************************************四、查询PG结算结果-START*******************************************/
        String pgDate = "noPGData";
        BigDecimal pgSum = new BigDecimal(0);
        BigDecimal pgServiceFee = new BigDecimal(0);
        try {
            Map param = Maps.newHashMap();
            param.put("storeId", baStore.id());
            param.put("startTime", dayStartTime);
            param.put("endTime", dayEndTime);
            SettleDTO settleDTO = orderQuery.getPGDailySettleByMap(param);
//            log.info("【BA交易类商户每日结算】【" + baStore.id() +"】获取到的PG结算信息::{}", settleDTO);

            // PG结算起止日期
            pgDate = (settleDTO.getPgFrDt() == null ? "" : settleDTO.getPgFrDt())
                    + "~" + (settleDTO.getPgToDt() == null ? "" : settleDTO.getPgToDt());

            // PG结算金额
            String pgAmount = settleDTO.getPgAmount();
            if (pgAmount == null || "".equals(pgAmount.trim())) {
                pgSum = new BigDecimal(0);
            } else {
                try {
                    pgSum = new BigDecimal(pgAmount);
                } catch (Exception e) {
                    pgSum = new BigDecimal(0);
//                    log.error("【BA交易类商户每日结算】【" + baStore.id() + "】PG结算金额转换异常：" + e.getMessage(), e);
                }
            }

            // PG结算手续费
            String pgFee = settleDTO.getPgFee();
            if (pgFee == null || "".equals(pgFee.trim())) {
                pgServiceFee = new BigDecimal(0);
            } else {
                try {
                    pgServiceFee = new BigDecimal(pgFee);
                } catch (Exception e) {
                    pgServiceFee = new BigDecimal(0);
//                    log.error("【BA交易类商户每日结算】【" + baStore.id() + "】PG结算手续费转换异常：" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
//            log.error("【BA交易类商户每日结算】【{}】获取PG结算信息异常：" + e.getMessage(), e);
        }
        /*****************************************四、查询PG结算结果-END*******************************************/

        /*****************************************五、存储商户结算月结算结果-START*******************************************/
        // 构造结算信息
        StoreDailySettlementBA storeDailySettlementBA = StoreDailySettlementBA.builder()
                .sid(uidGenerator.generate(BusinessTypeEnum.STORE_DAILY_SETTLEMENT))
                .storeID(baStore.id())
                .settleDate(yesterday)
                .settleType(currMonthChargeInfo.getChargeType())
                .settleRate(currMonthChargeInfo.getChargeRate())
                .settleFee(currMonthChargeInfo.getChargeFee())
                .orderCount(dailyOrderCount)
                .settleSum(dailyOrderTotalAmount)
                .pgDate(pgDate)
                .pgSum(pgSum)
                .pgServiceFee(pgServiceFee)
                //.plDate() // SQL中写入
                .plMinFee(plMinFee)
                .plServiceFee(plServiceFee)
                .plFinalFee(plFinalFee)
                // BA交易金结算金额=订单总金额-平台最终服务费-PG结算服务费
                //.cashSettleSum(dailyOrderTotalAmount.subtract(plFinalFee).subtract(pgServiceFee))
                .cashSettleSum(dailyOrderTotalAmount.subtract(plFinalFee).subtract(dailyOrderTotalAmount.multiply(new BigDecimal(0.02))))
                .build();

        // 插入更新商户结算日的结算结果
        orderRepository.saveBAStoreDailySettlement(storeDailySettlementBA);
        /*****************************************五、存储商户结算月结算结果-END*******************************************/
    }
    /*****************************************III.BA交易类商户月度结算-END*********************************************/





    /************************************IV.BA交易类商户(到手价类酒店)月度和每日结算-START***************************************/
    /**
     * BA交易类商户月度结算(定时任务) 4 HOTEL
     *
     * @param baStore
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     * @param monthStartTime
     * @param monthEndTime
     */
    /**
     * 计算商户结算月的平台服务费
     *
     * 思路一：
     * 1、查询出结算月所有完成订单（状态为9）和退款订单总金额A；
     * 2、查询出结算月内所有退款订单（结算月交易且结算月退款订单+前推12个月交易且结算月退款订单）总金额B。
     * 3、A-B得到的金额就是计算平台服务费的金额。
     *
     * 思路二：
     * 1、查询出结算月有效交易订单（有效交易订单=非退款完成订单（状态为9） + 结算月交易但非结算月退款的退款订单）总金额C；
     * 2、查询出自结算月前推12个月退款订单总金额D。
     * 3、C-D得到的金额就是计算平台服务费的金额。
     *
     * 第一步中查询的订单金额即是C
     *
     * 解读非结算月退款订单：
     * 假定如下：
     * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
     * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
     * （3）非结算月退款订单就是2019.08.01 00:00:00-2019.08.31 23:59:59生成的订单并且退款了，但是退款日期为2019年09月内的日期，
     *  即2019年09月01日0点起至月度结算任务运行期间退的订单。
     *
     * #############采用思路二计算商户结算月的平台服务费
     */
    public void baStoreMonthSettle4Hotel(Store baStore, int lastYear, int lastMonth, int year,
                                                int month, String monthStartTime, String monthEndTime) throws Exception {
        /*****************************************一、查询DAOSHOU HOTEL商户结算信息-START*******************************************/
        /**
         * 查询结算信息
         *
         * 查询结算月的计费信息，假定2019年08月的计费信息
         */
        StoreChargeInfoRecord lastMonthChargeInfo = storeRepository.getStoreMonthChargeInfo(baStore.id(), lastYear, lastMonth);
        if (lastMonthChargeInfo == null) {
            return;
        }
//        log.info("【BA交易类到手价酒店商户月度结算】【{}】商户结算依据信息：{}", baStore.id(), lastMonthChargeInfo);
        /*****************************************一、查询DAOSHOU HOTEL商户结算信息-END*******************************************/


        /*****************************************二、查询商户结算月有效订单总金额-START*******************************************/
        /**
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         * （3）商户结算月有效交易订单定义如下：
         *  有效交易订单=非退款订单（订单为9的非退款订单） + 非结算月退款订单
         */
        // 思路二：查询出结算月有效交易订单（有效交易订单=非退款订单（订单为9的非退款订单） + 非结算月退款订单）总金额C
        Map<String, Object> storeEffectiveOrderAmountSum = orderQuery.getBAStoreEffectiveOrderAmountSum4Hotel(baStore.id(),
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        Long orderCountNoraml = (Long) storeEffectiveOrderAmountSum.get("orderCountNormal");

        Long orderCountDaoshou = (Long) storeEffectiveOrderAmountSum.get("orderCountDaoshou");



        BigDecimal orderTotalAmountNormal = (BigDecimal) storeEffectiveOrderAmountSum.get("orderTotalAmountNormal");

        BigDecimal orderTotalAmountDaoshou = (BigDecimal) storeEffectiveOrderAmountSum.get("orderTotalAmountDaoshou");
        BigDecimal orderTotalAmountDaoshouSettle = (BigDecimal) storeEffectiveOrderAmountSum.get("orderTotalAmountDaoshouSettle");
//        log.info("【BA交易类酒店商户月度结算】【{}】有效订单数量和总金额信息如下：订单数量为Normal{} Daoshou{},订单总金额为Normal{} Daoshou{} DaoshouSettle{}", baStore.id(), orderCountNoraml, orderCountDaoshou, orderTotalAmountNormal, orderTotalAmountDaoshou, orderTotalAmountDaoshouSettle);
        /*****************************************二、查询商户结算月有效订单总金额-END*******************************************/

        /*****************************************三、查询出自结算月前推12个月退款订单总金额D-START*******************************************/
        /**
         * 查询指定商户当前结算月往前12个月内交易但退款成功订单的总金额，并且退款日期为结算月内的日期。
         * 强调：不含结算月内交易的任何退款订单。
         *
         * 假定如下：
         * （1）当前日为2019.09.05，即当前月为2019.09，即进行结算工作的日期和月份。
         * （2）结算月为2019.08，即对2019年08月份的订单进行结算。
         * （3）结算月往前12个月：2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
         *            2019.01，2018.12，2018.11，2018.10，2018.09，2018.08
         * （4）退款日期为结算月内的日期：退款日期为2019.08.01-2019.08.31
         *
         *  查询2019.07，2019.06，2019.06，2019.04，2019.03，2019.02，
         *  2019.01，2018.12，2018.11，2018.10，2018.09，2018.08这12个月内的退款订单，并且退款日期为2019.08.01-2019.08.31
         */
        /*#########################思路二START#################################################################*/
        Map<String, Object> storeRefundOrderAmountSum = orderQuery.getBAStoreRefundBeforeOrderAmountSum4Hotel(baStore.id(),
                monthStartTime, monthEndTime, getYearMonthString(lastYear, lastMonth));
        // 订单数量-正常退款订单-不带即时费率
        Long refundOrderCountNormal = (Long) storeRefundOrderAmountSum.get("refundOrderCountNormal");
        // 订单数量-正常退款订单-带即时费率
        Long refundOrderCountWithRateNormal = (Long) storeRefundOrderAmountSum.get("refundOrderCountWithRateNormal");

        // 订单数量-到手价退款订单
        Long refundOrderCountDaoshou = (Long) storeRefundOrderAmountSum.get("refundOrderCountDaoshou");



        // 订单金额-正常退款订单-不带即时费率
        BigDecimal refundOrderTotalAmountNormal = (BigDecimal) storeRefundOrderAmountSum.get("refundOrderTotalAmountNormal");

        // 订单金额-正常退款订单-带即时费率
        BigDecimal refundOrderTotalAmountWithRateNormal = (BigDecimal) storeRefundOrderAmountSum.get("refundOrderTotalAmountWithRateNormal");
        BigDecimal refundOrderServiceFeeWithRateNormal = (BigDecimal) storeRefundOrderAmountSum.get("refundOrderServiceFeeWithRateNormal");

        // 订单金额-到手价退款订单
        BigDecimal refundOrderTotalAmountDaoshou = (BigDecimal) storeRefundOrderAmountSum.get("refundOrderTotalAmountDaoshou");
        BigDecimal refundOrderTotalAmountDaoshouSettle = (BigDecimal) storeRefundOrderAmountSum.get("refundOrderTotalAmountDaoshouSettle");

//        log.info("【BA交易类酒店商户月度结算】【{}】之前12个月内有效退款订单数量和退款总金额信息如下：退款订单数量为Noraml{} NoramlWithRate{} Daoshou{},退款总金额为Noraml{} NoramlWithRate{} NoramlServiceFeeWithRate{} Daoshou{} DaoshouSettle{}",
//                baStore.id(), refundOrderCountNormal, refundOrderCountWithRateNormal, refundOrderCountDaoshou, refundOrderTotalAmountNormal, refundOrderTotalAmountWithRateNormal, refundOrderServiceFeeWithRateNormal, refundOrderTotalAmountDaoshou, refundOrderTotalAmountDaoshouSettle);
        /*#########################思路二END#################################################################*/
        /*****************************************三、查询出自结算月前推12个月退款订单总金额D-END*******************************************/


        /*****************************************四、计算商户结算月的正常订单的平台服务费-START*******************************************/
        // 计算商户结算月平台服务费
        BigDecimal plMinFee = new BigDecimal(lastMonthChargeInfo.getChargeFee());
        Map<String, BigDecimal> plFees = calculatePlFee4Hotel(lastMonthChargeInfo, orderTotalAmountNormal,
                refundOrderTotalAmountNormal, refundOrderServiceFeeWithRateNormal);
        BigDecimal plServiceFee = plFees.get("SF");
        BigDecimal plFinalFee = plFees.get("FF");
//        log.info("【BA交易类商户月度结算】【{}】商户平台结算服务费和最终服务费分别为：平台结算服务费SF为{},平台最终服务费FF为{}", baStore.id(), plServiceFee, plFinalFee);
        /*****************************************四、计算商户结算月的正常订单的平台服务费-END*******************************************/

        /*****************************************四、查询PG结算结果-START*******************************************/
        String pgDate = "noPGData";
        BigDecimal pgSum = new BigDecimal(0);
        BigDecimal pgServiceFee = new BigDecimal(0);
        try {
            /*特别说明start

            此处的PG结算信息：不仅仅包含完成订单（订单状态为9），也包括其他状态的订单，
            即该商户该结算月全量的PG结算信息，而服务费结算只涉及退款订单和完成订单。所以此处的PG结算信息不适合展现在
            BA交易类商户的服务费结算页面
            TODO 如何过滤出BA交易类商户的参与月度服务费结算订单，即过滤出状态为9的订单，以及退款订单
             */
            Map param = Maps.newHashMap();
            param.put("storeId", baStore.id());
            param.put("startTime", monthStartTime);
            param.put("endTime", monthEndTime);
            SettleDTO settleDTO = orderQuery.getPGSettleByMap(param);
//            log.info("【BA交易类商户月度结算】【" + baStore.id() +"】获取到的PG结算信息::{}", settleDTO);
            /*特别说明end*/

            // PG结算起止日期
            pgDate = (settleDTO.getPgFrDt() == null ? "" : settleDTO.getPgFrDt())
                    + "~" + (settleDTO.getPgToDt() == null ? "" : settleDTO.getPgToDt());

            // PG结算金额
            String pgAmount = settleDTO.getPgAmount();
            if (pgAmount == null || "".equals(pgAmount.trim())) {
                pgSum = new BigDecimal(0);
            } else {
                try {
                    pgSum = new BigDecimal(pgAmount);
                } catch (Exception e) {
                    pgSum = new BigDecimal(0);
//                    log.error("【BA交易类商户月度结算】【" + baStore.id() + "】PG结算金额转换异常：" + e.getMessage(), e);
                }
            }

            // PG结算手续费
            String pgFee = settleDTO.getPgFee();
            if (pgFee == null || "".equals(pgFee.trim())) {
                pgServiceFee = new BigDecimal(0);
            } else {
                try {
                    pgServiceFee = new BigDecimal(pgFee);
                } catch (Exception e) {
                    pgServiceFee = new BigDecimal(0);
//                    log.error("【BA交易类商户月度结算】【" + baStore.id() + "】PG结算手续费转换异常：" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
//            log.error("【BA交易类商户月度结算】【{}】获取PG结算信息异常：" + e.getMessage(), e);
        }
        /*****************************************四、查询PG结算结果-END*******************************************/

        /*****************************************五、存储商户结算月结算结果-START*******************************************/
        // 构造结算信息
        StoreSettlementBA storeSettlementBA = StoreSettlementBA.builder()
                .sid(uidGenerator.generate(BusinessTypeEnum.BA_STORE_SETTLEMENT))
                .storeID(baStore.id())
                .settleYearMonth(getYearMonthStringNoLine(lastYear, lastMonth))
                .settleYear(lastYear)
                .settleMonth(lastMonth)
                .startDT(monthStartTime.substring(0,10))
                .endDT(monthEndTime.substring(0,10))
                .settleType(lastMonthChargeInfo.getChargeType())
                .settleRate(lastMonthChargeInfo.getChargeRate())
                .settleFee(lastMonthChargeInfo.getChargeFee())

                //.orderCount(orderCountNoraml + refundOrderCountNormal + refundOrderCountWithRateNormal)// 正常订单数量=有效正常订单数量+退款订单数量（即时费率+非即时费率）
                .orderCount(orderCountNoraml)// 正常订单数量=有效正常订单数量。不含退款订单数量（即时费率+非即时费率）
                .settleSum(orderTotalAmountNormal)// 正常订单总金额 = 正常订单总金额。并没有减去前退12个月退款的正常订单金额

                .pgDate(pgDate)
                .pgSum(pgSum)
                .pgServiceFee(pgServiceFee)
                //.plDate() // SQL中写入
                .plMinFee(plMinFee)
                .plServiceFee(plServiceFee)
                .plFinalFee(plFinalFee)

                .orderCountDaoshou(orderCountDaoshou)// 到手价订单数量
                .orderPlatAmountSum(orderTotalAmountDaoshou)// 到手价订单平台总金额
                .orderDaoshouAmountSum(orderTotalAmountDaoshouSettle)// 到手价订单到手总金额
                .orderCountDaoshouRefund(refundOrderCountDaoshou)// 退款的到手价订单数量
                .orderPlatAmountSumRefund(refundOrderTotalAmountDaoshou)// 退款的到手价订单平台总金额
                .orderDaoshouAmountSumRefund(refundOrderTotalAmountDaoshouSettle)// 退款的到手价订单到手总金额

                // BA交易金结算金额:结算给商户的金额，扣除pg服务费，平台服务费，以及退款造成的款项，计算如下：
                // BA交易金结算金额-正常订单=正常订单总金额-正常订单的平台最终服务费-正常订单的PG结算服务费(正常订单总金额*0.02)
                // BA交易金结算金额-到手价订单=到手价订单到手总金额-退款的到手价订单到手总金额
                .cashSettleSum(orderTotalAmountNormal.subtract(plFinalFee).subtract(orderTotalAmountNormal.multiply(new BigDecimal(0.02)))
                .add(orderTotalAmountDaoshouSettle.subtract(refundOrderTotalAmountDaoshouSettle)))
                .build();

        // 删除商户结算月已存在的结算结果
        orderRepository.deleteBAStoreSettlement(baStore.id(), lastYear, lastMonth);

        // 插入更新商户结算月的结算结果
        orderRepository.saveBAStoreSettlement(storeSettlementBA);
        /*****************************************五、存储商户结算月结算结果-END*******************************************/
    }



    /**
     * BA交易类商户每日结算  4 HOTEL
     *
     * @param baStore
     * @param yesterday
     * @param dayStartTime
     * @param dayEndTime
     * @param today
     */
    /**
     * 前言：
     * 说明下面描述中的结算日即昨日。
     *
     * 思路一：
     * 1、查询出结算日所有订单（包含退款订单）总金额A。
     * 退款订单的含义特别强调：包含结算日交易结算日退款订单和结算日交易今日退款订单，因为定时任务是在凌晨2点跑的，
     * 0点到2点之间是存在退款的可能性的；
     * 2、查询出结算日进行退款的所有订单（结算日交易且结算日退款订单+前推360天交易且结算日退款订单）总金额B。
     * 3、A-B得到的金额就是计算平台服务费的金额。
     * 此时结算日交易今日退款的订单是算在总金额中的。
     *
     * 思路二：
     * 查询出结算日有效交易订单（有效交易订单=非退款订单 + 非结算日退款订单）总金额C，查询出自结算月前推12个月退款订单总金额D。
     * C-D得到的金额就是计算平台服务费的金额。
     * 第一步中查询的订单金额即是C
     *
     * 解读非结算日退款订单：
     * 假定如下：
     * （1）当前日为2019.09.05，即进行结算工作的日期。
     * （2）结算日为2019.09.04，即对2019年09月04日的订单进行结算。
     * （3）非结算日退款订单就是2019.09.04 00:00:00-2019.09.04 23:59:59生成的订单并且退款了，但是退款日期为2019年09月05日，
     *  即2019年09月05日0点起至每日结算任务运行期间退的订单。
     *
     * #############采用思路二计算商户每日的平台服务费
     */
    public void baStoreDailySettle4Hotel(Store baStore, String yesterday, String dayStartTime, String dayEndTime, String today) {
        /*****************************************一、查询商户当前月结算信息-START*******************************************/
        /**
         * 查询结算信息
         *
         * 查询当前月的商户计费信息，即存储于Store表。
         */
        StoreChargeInfoRecord currMonthChargeInfo = new StoreChargeInfoRecord.Builder()
                .storeID(baStore.id())
                .chargeType(baStore.chargeType())
                .chargeRate(baStore.chargeRate())
                .chargeFee(baStore.chargeFee())
                .build();
//        log.info("【BA交易类商户每日结算】【{}】商户结算依据信息：{}", baStore.id(), currMonthChargeInfo);
        /*****************************************一、查询商户当前月结算信息-END*******************************************/


        /*****************************************二、查询商户昨日有效订单总金额-START*******************************************/
        /**
         * 商户昨日有效交易订单定义如下：
         *  有效交易订单=非退款订单 + 非昨日退款订单
         */
        Map<String, Object> storeDailyEffectiveOrderAmountSum = orderQuery.getBAStoreDailyEffectiveOrderAmountSum4Hotel(baStore.id(),
                dayStartTime, dayEndTime, yesterday);
        //Long dailyOrderCount = (Long) storeDailyEffectiveOrderAmountSum.get("dailyOrderCount");
        //BigDecimal dailyOrderTotalAmount = (BigDecimal) storeDailyEffectiveOrderAmountSum.get("dailyOrderTotalAmount");
        //log.info("【BA交易类商户每日结算】【{}】有效订单数量和总金额信息如下：订单数量为{},订单总金额为{}", baStore.id(), dailyOrderCount, dailyOrderTotalAmount);

        Long dailyOrderCountNoraml = (Long) storeDailyEffectiveOrderAmountSum.get("dailyOrderCountNormal");


        Long dailyOrderCountDaoshou = (Long) storeDailyEffectiveOrderAmountSum.get("dailyOrderCountDaoshou");




        BigDecimal dailyOrderTotalAmountNormal = (BigDecimal) storeDailyEffectiveOrderAmountSum.get("dailyOrderTotalAmountNormal");


        BigDecimal dailyOrderTotalAmountDaoshou = (BigDecimal) storeDailyEffectiveOrderAmountSum.get("dailyOrderTotalAmountDaoshou");
        BigDecimal dailyOrderTotalAmountDaoshouSettle = (BigDecimal) storeDailyEffectiveOrderAmountSum.get("dailyOrderTotalAmountDaoshouSettle");
//        log.info("BA交易类商户每日结算】【{}】有效订单数量和总金额信息如下：订单数量为Normal {} Daoshou {},订单总金额为Normal {} Daoshou {} DaoshouSettle {}",
//                baStore.id(), dailyOrderCountNoraml, dailyOrderCountDaoshou, dailyOrderTotalAmountNormal, dailyOrderTotalAmountDaoshou, dailyOrderTotalAmountDaoshouSettle);
        /*****************************************二、查询商户昨日有效订单总金额-END*******************************************/





        /*****************************************三、查询出自结算日前推360天内退款订单总金额D-START*******************************************/
        /*#########################思路二START#################################################################*/
        /**
         * 查询指定商户当前结算月往前360天内的退款成功订单的总金额，并且退款日期为结算日。
         * 强调：不含结算日内的任何退款订单。
         *
         * 假定如下：
         * （1）当前日为2019.09.05，即进行结算工作的日期。
         * （2）结算日为2019.09.04，即对2019年09月04日的订单进行结算。
         * （3）结算月往前360天
         * （4）退款日期为结算日内的日期：退款日期为2019.09.04
         */
        Map<String, Object> storeDailyRefundOrderAmountSum = orderQuery.getBAStoreDailyRefundBeforeOrderAmountSum4Hotel(baStore.id(),
                dayStartTime, dayEndTime, yesterday);

        // 订单数量-正常退款订单-不带即时费率
        Long dailyRefundOrderCountNormal = (Long) storeDailyRefundOrderAmountSum.get("dailyRefundOrderCountNormal");
        // 订单数量-正常退款订单-带即时费率
        Long dailyRefundOrderCountWithRateNormal = (Long) storeDailyRefundOrderAmountSum.get("dailyRefundOrderCountWithRateNormal");

        // 订单数量-到手价退款订单
        Long dailyRefundOrderCountDaoshou = (Long) storeDailyRefundOrderAmountSum.get("dailyRefundOrderCountDaoshou");



        // 订单金额-正常退款订单-不带即时费率
        BigDecimal dailyRefundOrderTotalAmountNormal = (BigDecimal) storeDailyRefundOrderAmountSum.get("dailyRefundOrderTotalAmountNormal");

        // 订单金额-正常退款订单-带即时费率
        BigDecimal dailyRefundOrderTotalAmountWithRateNormal = (BigDecimal) storeDailyRefundOrderAmountSum.get("dailyRefundOrderTotalAmountWithRateNormal");
        BigDecimal dailyRefundOrderServiceFeeWithRateNormal = (BigDecimal) storeDailyRefundOrderAmountSum.get("dailyRefundOrderServiceFeeWithRateNormal");



        // 订单金额-到手价退款订单
        BigDecimal dailyRefundOrderTotalAmountDaoshou = (BigDecimal) storeDailyRefundOrderAmountSum.get("dailyRefundOrderTotalAmountDaoshou");
        BigDecimal dailyRefundOrderTotalAmountDaoshouSettle = (BigDecimal) storeDailyRefundOrderAmountSum.get("dailyRefundOrderTotalAmountDaoshouSettle");

//        log.info("【BA交易类商户每日结算】【{}】之前360天内有效退款订单数量和退款总金额信息如下：退款订单数量为Noraml{} NoramlWithRate{} Daoshou{},退款总金额为Noraml{} NoramlWithRate{} NoramlServiceFeeWithRate{} Daoshou{} DaoshouSettle{}",
//                baStore.id(), dailyRefundOrderCountNormal, dailyRefundOrderCountWithRateNormal, dailyRefundOrderCountDaoshou,
//                dailyRefundOrderTotalAmountNormal, dailyRefundOrderTotalAmountWithRateNormal, dailyRefundOrderServiceFeeWithRateNormal, dailyRefundOrderTotalAmountDaoshou, dailyRefundOrderTotalAmountDaoshouSettle);
        /*#########################思路二END#################################################################*/
        /*****************************************三、查询出自结算日前推360天内退款订单总金额D-END*******************************************/




        /*****************************************四、计算商户每日的平台服务费-START*******************************************/
        /*#########################思路二START#################################################################*/
        // 计算商户结算月平台服务费
        BigDecimal plMinFee = new BigDecimal(currMonthChargeInfo.getChargeFee());
        Map<String, BigDecimal> plFees = calculateDailyPlFee4Hotel(currMonthChargeInfo, dailyOrderTotalAmountNormal,
                dailyRefundOrderTotalAmountNormal, dailyRefundOrderServiceFeeWithRateNormal);
        BigDecimal plServiceFee = plFees.get("SF");
        BigDecimal plFinalFee = plFees.get("FF");
//        log.info("【BA交易类商户每日结算】【{}】商户平台结算服务费和最终服务费分别为：平台结算服务费SF为{},平台最终服务费FF为{}", baStore.id(), plServiceFee, plFinalFee);
        /*#########################思路二END#################################################################*/
        /*****************************************四、计算结算月的平台服务费-END*******************************************/

        /*****************************************五、查询PG结算结果-START*******************************************/
        String pgDate = "noPGData";
        BigDecimal pgSum = new BigDecimal(0);
        BigDecimal pgServiceFee = new BigDecimal(0);
        try {
            Map param = Maps.newHashMap();
            param.put("storeId", baStore.id());
            param.put("startTime", dayStartTime);
            param.put("endTime", dayEndTime);
            SettleDTO settleDTO = orderQuery.getPGDailySettleByMap(param);
//            log.info("【BA交易类商户每日结算】【" + baStore.id() +"】获取到的PG结算信息::{}", settleDTO);

            // PG结算起止日期
            pgDate = (settleDTO.getPgFrDt() == null ? "" : settleDTO.getPgFrDt())
                    + "~" + (settleDTO.getPgToDt() == null ? "" : settleDTO.getPgToDt());

            // PG结算金额
            String pgAmount = settleDTO.getPgAmount();
            if (pgAmount == null || "".equals(pgAmount.trim())) {
                pgSum = new BigDecimal(0);
            } else {
                try {
                    pgSum = new BigDecimal(pgAmount);
                } catch (Exception e) {
                    pgSum = new BigDecimal(0);
//                    log.error("【BA交易类商户每日结算】【" + baStore.id() + "】PG结算金额转换异常：" + e.getMessage(), e);
                }
            }

            // PG结算手续费
            String pgFee = settleDTO.getPgFee();
            if (pgFee == null || "".equals(pgFee.trim())) {
                pgServiceFee = new BigDecimal(0);
            } else {
                try {
                    pgServiceFee = new BigDecimal(pgFee);
                } catch (Exception e) {
                    pgServiceFee = new BigDecimal(0);
//                    log.error("【BA交易类商户每日结算】【" + baStore.id() + "】PG结算手续费转换异常：" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
//            log.error("【BA交易类商户每日结算】【{}】获取PG结算信息异常：" + e.getMessage(), e);
        }
        /*****************************************五、查询PG结算结果-END*******************************************/

        /*****************************************六、存储商户结算月结算结果-START*******************************************/
        // 构造结算信息
        StoreDailySettlementBA storeDailySettlementBA = StoreDailySettlementBA.builder()
                .sid(uidGenerator.generate(BusinessTypeEnum.STORE_DAILY_SETTLEMENT))
                .storeID(baStore.id())
                .settleDate(yesterday)
                .settleType(currMonthChargeInfo.getChargeType())
                .settleRate(currMonthChargeInfo.getChargeRate())
                .settleFee(currMonthChargeInfo.getChargeFee())
                .orderCount(dailyOrderCountNoraml)
                .settleSum(dailyOrderTotalAmountNormal)
                .pgDate(pgDate)
                .pgSum(pgSum)
                .pgServiceFee(pgServiceFee)
                //.plDate() // SQL中写入
                .plMinFee(plMinFee)
                .plServiceFee(plServiceFee)
                .plFinalFee(plFinalFee)


                .orderCountDaoshou(dailyOrderCountDaoshou)// 到手价订单数量
                .orderPlatAmountSum(dailyOrderTotalAmountDaoshou)// 到手价订单平台总金额
                .orderDaoshouAmountSum(dailyOrderTotalAmountDaoshouSettle)// 到手价订单到手总金额
                .orderCountDaoshouRefund(dailyRefundOrderCountDaoshou)// 退款的到手价订单数量
                .orderPlatAmountSumRefund(dailyRefundOrderTotalAmountDaoshou)// 退款的到手价订单平台总金额
                .orderDaoshouAmountSumRefund(dailyRefundOrderTotalAmountDaoshouSettle)// 退款的到手价订单到手总金额

                // BA交易金结算金额:结算给商户的金额，扣除pg服务费，平台服务费，以及退款造成的款项，计算如下：
                // BA交易金结算金额-正常订单=正常订单总金额-正常订单的平台最终服务费-正常订单的PG结算服务费(正常订单总金额*0.02)
                // BA交易金结算金额-到手价订单=到手价订单到手总金额-退款的到手价订单到手总金额
                .cashSettleSum(
                        dailyOrderTotalAmountNormal // 正常订单总金额
                                .subtract(plFinalFee) // 扣除平台服务费
                                .subtract(dailyOrderTotalAmountNormal.multiply(new BigDecimal(0.02))) // 扣除平台服务费
                                .add(dailyOrderTotalAmountDaoshouSettle.subtract(dailyRefundOrderTotalAmountDaoshouSettle)) //BA交易金结算金额-到手价订单=到手价订单到手总金额-退款的到手价订单到手总金额
                )
                .build();

        // 插入更新商户结算日的结算结果
        orderRepository.saveBAStoreDailySettlement(storeDailySettlementBA);
        /*****************************************六、存储商户结算月结算结果-END*******************************************/
    }



    /**
     * 计算BA交易类酒店商户结算月平台服务费
     * 说明：假定结算月的收费类型为CT
     * （1）CT为百分比
     * 商户结算月平台服务费SF=有效订单总金额*CT-商户结算月有效退款总金额（非即时费率腿狂订单总金额）*CT-即时费率退款订单 结算得到的 平台服务费
     * 商户最终服务费FF = SF
     * <p>
     * （2）CT为百分比或最小服务费
     * 商户结算月平台服务费SF=有效订单总金额*CT-商户结算月有效退款总金额（非即时费率腿狂订单总金额）*CT-即时费率退款订单 结算得到的 平台服务费
     * 最小服务费MF
     * 商户最终服务费FF为SF和MF中较大的值
     * <p>
     * （3）CT为按月
     * 商户结算月平台服务费SF = 固定值month_fee
     * 商户最终服务费FF = 固定值month_fee
     *
     * 特别说明：：：上述第一种类型和第二种类型的算式【商户结算月有效退款总金额*CT-这个退款总金额为没有即时费率的退款订单的总金额】中CT为结算月的百分比，
     * 这就存在一定的误差，描述如下：比如2019.09月结算2019.08月的订单，但是2019.08月内还对2019.07月的订单产生了退单。实际上返还
     * 2019.07月的服务费应该按照2019.07月的结算标准即百分比计算，实际上是按照2019.08月的结算标准即百分比计算的。
     * 如果2019.07月结算标准和2019.08月的计算标准一样还好，如果不一样，则返还时就存在误差，误差如下：
     * 商户吃亏-2019.07为5%，2019.08为%3  或者  平台吃亏-2019.07为3%，2019.08为%5。
     * //to1do 如果完全规避这种误差需要查询出201907月的结算标准，然后按照这个标准进行返还。目前暂且不实现。
     *
     * @param lastMonthChargeInfo    商户结算标准
     * @param orderTotalAmount       商户结算月有效订单总金额
     * @param refundOrderTotalAmount 商户结算月有效退款总金额（结算月前12个月的退款，退款日期为结算月日期）-不含即时费率的订单，采用结算标准结算
     * @param refundOrderServiceFeeWithRateNormal
     * @return
     */
    private Map<String, BigDecimal> calculatePlFee4Hotel(StoreChargeInfoRecord lastMonthChargeInfo,
                                                         BigDecimal orderTotalAmount,
                                                         BigDecimal refundOrderTotalAmount,
                                                         BigDecimal refundOrderServiceFeeWithRateNormal) {
        Map<String, BigDecimal> plFees = Maps.newHashMap();
        int storeChargeType = lastMonthChargeInfo.getChargeType();
        // 百分比
        if (CommonConstants.STORE_CHARGE_TYPE_RATE == storeChargeType) {
            BigDecimal sf = orderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100)))
                    .subtract(refundOrderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100))))
                    .subtract(refundOrderServiceFeeWithRateNormal);
            plFees.put("SF", sf);
            plFees.put("FF", sf);
            return plFees;
        }
        // 百分比或最小服务费
        else if (CommonConstants.STORE_CHARGE_TYPE_RATE_MIN_FEE == storeChargeType) {
            BigDecimal sf = orderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100)))
                    .subtract(refundOrderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100))))
                    .subtract(refundOrderServiceFeeWithRateNormal);
            BigDecimal mf = new BigDecimal(lastMonthChargeInfo.getChargeFee());
            plFees.put("SF", sf);
            // sf大于mf
            if (sf.compareTo(mf) == 1) {
                plFees.put("FF", sf);
            }
            // sf等于mf
            else if (sf.compareTo(mf) == 0) {
                plFees.put("FF", mf);
            }
            // sf小于mf
            else if (sf.compareTo(mf) == -1) {
                plFees.put("FF", mf);
            }
            return plFees;
        }
        // 按月
        else if (CommonConstants.STORE_CHARGE_TYPE_MONTHLY == storeChargeType) {
            plFees.put("SF", new BigDecimal(lastMonthChargeInfo.getChargeFee()));
            plFees.put("FF", new BigDecimal(lastMonthChargeInfo.getChargeFee()));
            return plFees;
        } else {
            plFees.put("SF", new BigDecimal(0));
            plFees.put("FF", new BigDecimal(0));
            return plFees;
        }
    }


    /**
     * 计算BA交易类酒店商户每日结算平台服务费
     *
     * 说明：假定结算日的商户收费类型为CT
     *
     * （1）CT为百分比
     * 商户每日结算平台服务费SF=有效订单总金额*CT-商户结算日有效退款总金额（非即时费率腿狂订单总金额）*CT-即时费率退款订单 结算得到的 平台服务费
     * 商户最终服务费FF = SF
     * <p>
     *
     * （2）CT为百分比或最小服务费
     * 商户结算日平台服务费SF=有效订单总金额*CT-商户结算日有效退款总金额*CT（非即时费率腿狂订单总金额）-即时费率退款订单 结算得到的 平台服务费
     * 商户每日结算不考虑最小服务费MF和平台服务费SF大小关系。商户最终服务费FF显示为平台服务费SF
     * 商户最终服务费FF = SF
     * <p>
     *
     * （3）CT为按月
     * 商户结算月平台服务费SF = 固定值month_fee
     * 商户最终服务费FF = 固定值month_fee
     *
     * @param lastMonthChargeInfo    商户结算标准
     * @param orderTotalAmount       商户结算日有效订单总金额
     * @param refundOrderTotalAmount 商户结算日有效退款总金额（结算月前360天内的退款，退款日期为结算日）
     * @param refundOrderServiceFeeWithRate
     * @return
     */
    private Map<String, BigDecimal> calculateDailyPlFee4Hotel(StoreChargeInfoRecord lastMonthChargeInfo,
                                                              BigDecimal orderTotalAmount,
                                                              BigDecimal refundOrderTotalAmount,
                                                              BigDecimal refundOrderServiceFeeWithRate) {
        Map<String, BigDecimal> plFees = Maps.newHashMap();
        int storeChargeType = lastMonthChargeInfo.getChargeType();
        // 百分比
        if (CommonConstants.STORE_CHARGE_TYPE_RATE == storeChargeType) {
            BigDecimal sf = orderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100)))
                    .subtract(refundOrderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100))))
                    .subtract(refundOrderServiceFeeWithRate);
            plFees.put("SF", sf);
            plFees.put("FF", sf);
            return plFees;
        }
        // 百分比或最小服务费
        else if (CommonConstants.STORE_CHARGE_TYPE_RATE_MIN_FEE == storeChargeType) {
            BigDecimal sf = orderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100)))
                    .subtract(refundOrderTotalAmount.multiply(new BigDecimal(lastMonthChargeInfo.getChargeRate()).divide(new BigDecimal(100))))
                    .subtract(refundOrderServiceFeeWithRate);
            //BigDecimal mf = new BigDecimal(lastMonthChargeInfo.getChargeFee());
            plFees.put("SF", sf);

            /*
            // sf大于mf
            if (sf.compareTo(mf) == 1) {
                plFees.put("FF", sf);
            }
            // sf等于mf
            else if (sf.compareTo(mf) == 0) {
                plFees.put("FF", mf);
            }
            // sf小于mf
            else if (sf.compareTo(mf) == -1) {
                plFees.put("FF", mf);
            }
            */
            // 每日结算的最终服务费取百分比计算出来的服务费
            plFees.put("FF", sf);

            return plFees;
        }
        // 按月-商户每日结算显示服务费和最终服务费
        else if (CommonConstants.STORE_CHARGE_TYPE_MONTHLY == storeChargeType) {
            plFees.put("SF", new BigDecimal(lastMonthChargeInfo.getChargeFee()));
            plFees.put("FF", new BigDecimal(lastMonthChargeInfo.getChargeFee()));
            return plFees;
        } else {
            plFees.put("SF", new BigDecimal(0));
            plFees.put("FF", new BigDecimal(0));
            return plFees;
        }
    }
    /************************************IV.BA交易类商户(到手价类酒店)月度和每日结算-END***************************************/





    @Override
    public void saveStoreDailyGrade(Store store, String dayEndTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("storeId", store.id());
        param.put("endTime",dayEndTime);
        List<ReviewDTO> reviewList = reviewQuery.getReviewList(param);
        int count = reviewQuery.getReviewCount(param);//评论数
        if(reviewList.size()>0){
            Double avgRevClass =  reviewList.stream().filter(r->r.getRevClass()>0).mapToDouble(ReviewDTO::getRevClass).average().getAsDouble();//平均分
            storeRepository.saveStoreDailyGrade(store,avgRevClass,count);
        }else{
            storeRepository.saveStoreDailyGrade(store,(double)ReviewDTO.REVCLASS_ZERO,count);
        }
    }
}