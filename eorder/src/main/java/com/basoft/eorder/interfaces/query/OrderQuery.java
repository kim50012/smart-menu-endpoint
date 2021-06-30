package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.batch.job.model.agent.AgentSettlementOrder;
import com.basoft.eorder.batch.job.model.retail.RetailToDoRecoverTempOrder;
import com.basoft.eorder.domain.model.agent.Agent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderQuery {
    int getOrderCount(Map<String, Object> param);

    OrderDTO getOrderByMap(Map<String, Object> param);

    List<OrderDTO> getOrderListByMap(Map<String, Object> param);

    List<OrderDTO> orderCompleteList(Map<String, Object> param);

    List<OrderDTO> retailOrderCompleteList(Map<String, Object> param);

    int getOrderItemCount(Map<String, Object> param);

    List<OrderItemDTO> getOrderItemListByMap(Map<String, Object> param);


    int getUserOrderCount(Map<String, Object> param);

    List<UserOrderDTO> getUserOrderListByMap(Map<String, Object> param);


    //订单统计
    List<OrderSumStatsDTO> getOrderSumStatisList(Map<String, Object> param);

    //销售排行前10的店铺
    List<OrderSumRankStoreStatsDTO> getOrderSumRankStoreStatisList(Map<String, Object> param);

    //产品销量查询
    List<ProductSaleRankDTO> getProductSaleRankList(Map<String, Object> param);

    //分类销量查询
    List<CategorySaleRankDTO> getCategorySaleRankList(Map<String, Object> param);

    List<QtyAndDateDTO> getQtyAndDate(Map<String, Object> param);

    List<PayAmountAndDateDTO> getPayAmountAndDate(Map<String, Object> param);

    //当前日期总金额
    BigDecimal getSumPayAmountForDate(Map<String, Object> param);

    SettleDTO getSettleByMap(Map<String, Object> param);

    int getSettleCount(Map<String, Object> param);

    List<SettleDTO> getSettleListByMap(Map<String, Object> param);

    int getSettleDtlCount(Map<String, Object> param);

    List<SettleDTO> getSettleDtlListByMap(Map<String, Object> param);

    List<OrderDTO> getMyContactListByMap(Map<String, Object> param);

    int getMyContactCount(Map<String, Object> param);

    /**
     * 查询酒店类待恢复库存的临时订单（2个小时前下单未支付的订单）
     *
     * @return
     */
    List<Map<String, Object>> queryTempOrdersToRecover();

    /**
     * 根据订单ID查询酒店的临时订单
     *
     * @return
     */
    List<Map<String, Object>> queryTempOrderByIdToRecover(String tempOrderId);

    /**
     * 查询零售业务类待恢复库存的临时订单（2个小时前下单未支付的订单）
     *
     * @return
     */
    List<RetailToDoRecoverTempOrder> queryRetailTempOrdersToRecover();

    /**
     * 零售业务：
     * 库存恢复：
     * 根据订单ID查询酒店的临时订单
     *
     * @param tempOrderId
     * @param queryType
     * @return
     */
    List<RetailToDoRecoverTempOrder> queryRetailTempOrderByIdToRecover(String tempOrderId, String queryType);


    /**********************************************PG交易类商户月度结算-start**************************************************/
    /**
     * 查询指定商户结算月的有效订单总金额和数量
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    public Map<String, Object> getPGStoreEffectiveOrderAmountSum(Long storeId, String monthStartTime, String monthEndTime, String refundYearMonth);

    /**
     * 查询商户指定日期期间的有效订单总金额（含退款订单）
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @return
     */
    @Deprecated
    Map<String, Object> getPGStoreOrderAllAmountSum(Long storeId, String monthStartTime, String monthEndTime);

    /**
     * 查询指定商户当前结算月和当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @return
     */
    @Deprecated
    public Map<String, Object> getPGStoreRefundOrderAmountSum(Long storeId, String monthStartTime, String monthEndTime, String refundYearMonth);

    /**
     * 查询指定商户当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
     * 强调：不含结算月内的任何退款订单。
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    Map<String, Object> getPGStoreRefundBeforeOrderAmountSum(Long storeId, String monthStartTime, String monthEndTime, String refundYearMonth);

    /**
     * 查询指定商户指定年月PG结算信息
     *
     * @param param
     * @return
     * @see com.basoft.eorder.interfaces.query.OrderQuery[getSettleByMap]
     */
    SettleDTO getPGSettleByMap(Map<String, Object> param);
    /**********************************************PG交易类商户月度结算-end**************************************************/





    /**********************************************PG交易类商户每日结算-start**************************************************/
    /**
     * PG交易类商户每日结算：查询结算日（即昨日）的有效交易订单
     * 定义：有效交易订单=非退款订单 + 非昨日退款订单
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    Map<String, Object> getPGStoreDailyEffectiveOrderAmountSum(Long storeId, String dayStartTime, String dayEndTime, String yesterday);

    /**
     *PG交易类商户每日结算：查询指定商户结算日往前360天的退款成功订单的总金额，并且退款日期为结算日。
     * 强调：不含结算日内的任何退款订单。
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    Map<String, Object> getPGStoreDailyRefundBeforeOrderAmountSum(Long storeId, String dayStartTime, String dayEndTime, String yesterday);

    /**
     *查询指定商户指定年月日PG结算信息
     *
     * @param param
     * @return
     */
    SettleDTO getPGDailySettleByMap(Map<String, Object> param);
    /**********************************************PG交易类商户每日结算-end**************************************************/





    /**********************************************BA交易类商户月度结算-start**************************************************/
    /**
     *查询指定商户结算月的有效订单总金额和数量
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @param yearMonthString
     * @return
     */
    Map<String, Object> getBAStoreEffectiveOrderAmountSum(Long storeId, String monthStartTime, String monthEndTime, String yearMonthString);

    /**
     * 查询指定商户当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
     * 强调：不含结算月内交易但退款的订单。
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    Map<String, Object> getBAStoreRefundBeforeOrderAmountSum(Long storeId, String monthStartTime, String monthEndTime, String refundYearMonth);
    /**********************************************BA交易类商户月度结算-end**************************************************/
    /**********************************************BA交易类商户每日结算-start**************************************************/
    /**
     * BA交易类商户每日结算：查询结算日（即昨日）的有效交易订单
     * 定义：有效交易订单=完成订单 + 非昨日退款订单
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    Map<String, Object> getBAStoreDailyEffectiveOrderAmountSum(Long storeId, String dayStartTime, String dayEndTime, String yesterday);

    /**
     *BA交易类商户每日结算：查询指定商户结算日往前360天的退款成功订单的总金额，并且退款日期为结算日。
     * 强调：不含结算日内的任何退款订单。
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    Map<String, Object> getBAStoreDailyRefundBeforeOrderAmountSum(Long storeId, String dayStartTime, String dayEndTime, String yesterday);
    /**********************************************BA交易类商户每日结算-end**************************************************/





    /**********************************************BA交易类酒店商户月度结算-start**************************************************/
    /**
     *查询指定酒店商户结算月的有效订单总金额和数量
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @param yearMonthString
     * @return
     */
    Map<String, Object> getBAStoreEffectiveOrderAmountSum4Hotel(Long storeId, String monthStartTime, String monthEndTime, String yearMonthString);

    /**
     * 查询指定酒店商户当前结算月往前12个月的退款成功订单的总金额，并且退款日期为结算月内的日期。
     * 强调：不含结算月内交易但退款的订单。
     *
     * @param storeId
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    Map<String, Object> getBAStoreRefundBeforeOrderAmountSum4Hotel(Long storeId, String monthStartTime, String monthEndTime, String refundYearMonth);
    /**********************************************BA交易类酒店商户月度结算-end**************************************************/
/**********************************************BA交易类商户每日结算-start**************************************************/
    /**
     * BA交易类酒店商户每日结算：查询结算日（即昨日）的有效交易订单
     * 定义：有效交易订单=完成订单 + 非昨日退款订单
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    Map<String, Object> getBAStoreDailyEffectiveOrderAmountSum4Hotel(Long storeId, String dayStartTime, String dayEndTime, String yesterday);

    /**
     *BA交易类酒店商户每日结算：查询指定商户结算日往前360天的退款成功订单的总金额，并且退款日期为结算日。
     * 强调：不含结算日内的任何退款订单。
     *
     * @param storeId
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    Map<String, Object> getBAStoreDailyRefundBeforeOrderAmountSum4Hotel(Long storeId, String dayStartTime, String dayEndTime, String yesterday);
    /**********************************************BA交易类商户每日结算-end**************************************************/




    /**********************************************代理商月度结算-start**************************************************/
    /**
     * SA代理商：查询代理商结算月内的有效订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    @Deprecated
    List<AgentSettlementOrder> querySagentOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth);

    /*20200107完成订单代理商结算重算-版本*/
    /**
     * SA代理商：查询代理商结算月内的有效订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    List<AgentSettlementOrder> querySagentOrderList1(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth);

    /**
     * SA代理商：查询出结算月内退款但交易日期在之前12个月内的订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    List<AgentSettlementOrder> querySagentRefundBeforeOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth);

    /**
     * SA代理商：查询出结算月交易并且结算月退款的订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    List<AgentSettlementOrder> querySagentRefundOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth);

    /**
     * SA代理商：查询代理商所有结算订单
     * 1、查询代理商结算月内的有效订单
     * 2、查询出结算月内退款但交易日期在之前12个月内的订单
     * <p>
     * 该方法合并queryAgentOrderList和queryAgentRefundBeforeOrderList两个方法
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    @Deprecated
    List<AgentSettlementOrder> queryAgentAllOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth);

    /**
     * CA代理商：查询CA代理商结算月内的有效订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     * @see com.basoft.eorder.interfaces.query.OrderQuery[queryAgentAllOrderList]
     */
    @Deprecated
    List<AgentSettlementOrder> queryCagentOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth);

    /*20200107完成订单代理商结算重算-版本*/
    /**
     * CA代理商：查询CA代理商结算月内的有效订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     * @see com.basoft.eorder.interfaces.query.OrderQuery[queryAgentAllOrderList]
     */
    List<AgentSettlementOrder> queryCagentOrderList1(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth);


    /**
     * CA代理商：查询出结算月内退款但交易日期在之前12个月内的订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     * @see com.basoft.eorder.interfaces.query.OrderQuery[queryAgentAllOrderList]
     */
    List<AgentSettlementOrder> queryCagentRefundBeforeOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth);

    /**
     * CA代理商：查询出结算月交易并且结算月退款的订单
     *
     * @param agent
     * @param monthStartTime
     * @param monthEndTime
     * @param refundYearMonth
     * @return
     */
    List<AgentSettlementOrder> queryCagentRefundOrderList(Agent agent, String monthStartTime, String monthEndTime, String refundYearMonth);
    /**********************************************代理商月度结算-end**************************************************/


    /**********************************************代理商每日结算-start**************************************************/
    /**
     * SA代理商：查询代理商结算日的有效订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    @Deprecated
    List<AgentSettlementOrder> querySagentDailyOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday);

    /*20200107完成订单代理商结算重算-版本*/
    /**
     * SA代理商：查询代理商结算日的有效订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    List<AgentSettlementOrder> querySagentDailyOrderList1(Agent agent, String dayStartTime, String dayEndTime, String yesterday);

    /**
     * SA代理商：查询出结算日内退款但交易日期在之前360天内的订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    List<AgentSettlementOrder> querySagentDailyRefundBeforeOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday);

    /**
     * SA代理商：查询出结算日交易并且结算日退款的订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    List<AgentSettlementOrder> querySagentDailyRefundOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday);


    /**
     * CA代理商：查询CA代理商结算日的有效订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    @Deprecated
    List<AgentSettlementOrder> queryCagentDailyOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday);

    /*20200107完成订单代理商结算重算-版本*/
    /**
     * CA代理商：查询CA代理商结算日的有效订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    List<AgentSettlementOrder> queryCagentDailyOrderList1(Agent agent, String dayStartTime, String dayEndTime, String yesterday);



    /**
     * CA代理商：查询出结算日内退款但交易日期在之前360天内的订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    List<AgentSettlementOrder> queryCagentDailyRefundBeforeOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday);

    /**
     * CA代理商：查询出结算日交易并且结算日退款的订单
     *
     * @param agent
     * @param dayStartTime
     * @param dayEndTime
     * @param yesterday
     * @return
     */
    List<AgentSettlementOrder> queryCagentDailyRefundOrderList(Agent agent, String dayStartTime, String dayEndTime, String yesterday);
    /**********************************************代理商每日结算-end**************************************************/
}
