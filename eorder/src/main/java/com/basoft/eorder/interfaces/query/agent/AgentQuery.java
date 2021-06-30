package com.basoft.eorder.interfaces.query.agent;

import com.basoft.eorder.batch.job.model.agent.AgentDailySettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlementDetail;
import com.basoft.eorder.batch.job.model.agent.AgentStoreMap;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.domain.model.agent.QRCodeAgent;
import com.basoft.eorder.interfaces.query.AgentStoreDTO;
import com.basoft.eorder.interfaces.query.StoreDTO;
import com.basoft.eorder.interfaces.query.UserOrderDTO;
import com.basoft.eorder.interfaces.query.plAgt.PlAgtAmountDTO;

import java.util.List;
import java.util.Map;

public interface AgentQuery {
    AgentDTO getAgentById(Long agtId);

    AgentDTO getAgentDto(Map<String, Object> param);

    Agent getAgent(Map<String, Object> param);

    int getAgentCount(Map<String, Object> param);

    List<AgentDTO> getAgentListByMap(Map<String, Object> param);

    int getIsBindCount(Map<String, Object> param);

    //查询该代理商拥有的店铺列表
    List<AgentStoreDTO> getIsBindStoreListByMap(Map<String, Object> param);

    int getEmptyStoreCount(Map<String, Object> param);

    //查询没有被代理的店铺
    List<StoreDTO> getEmptyStoreListByMap(Map<String, Object> param);

    int getAgentStoreCount(Map<String, Object> param);

    //查询所有店铺(被绑定的状态isBind:1)
    List<AgentStoreDTO> getAgentStoreList(Map<String, Object> param);

    int getAgentStoreSettleCount(Map<String, Object> param);

    List<AgentStoreDTO> getAgentStoreSettleList(Map<String, Object> param);

    //回显绑定关系用于修改
    List<AgentAimMapDTO> getAgtAimDtoList(Map<String, Object> param);

    //某个代理商下的微信用户订单总数
    int getAgtUserOrderCnt(Map<String, Object> param);

    /**
     * 某个代理商下的微信用户结算
     * 返回结果：交易总金额 代理商总金额 交易数量 最近交易日期
     *
     * @Param
     * @return java.util.List<UserOrderDTO>
     * @author Dong Xifu
     * @date 2019/10/11 2:31 下午
     */
    List<UserOrderDTO> getAgtUserOrderList(Map<String, Object> param);

    //某个代理商下的店铺总数
    int getSaAgtStoreSettleCount(Map<String, Object> param);

    //某个代理商下的店铺结算
    List<AgentStoreDTO> getSaAgtStoreSettleList(Map<String, Object> param);

    /**
     * 根据代理商ID查询代理商二维码信息
     *
     * @param agentId
     * @return
     */
    QRCodeAgent getQRCodeByAgentId(String agentId);

    Map<String,Object> getCustOrderSum(Map<String, Object> param);

    Map<String,Object>  getAgtOrderSum(Map<String, Object> param);

    //代理商下的店铺总结算金额
    Map<String,Object>  getAgtStoreOrderSum(Map<String, Object> param);

    //代理商下的微信用户总结算金额
    Map<String,Object>  getAgtWxUserOrderSum(Map<String, Object> param);

    //按月总订单总数
    int getAgentOrderCount(Map<String, Object> param);

    //按月已结算订单总数
    int getFinishAgtOrderCount(Map<String, Object> param);

    //按月订单记录列表
    List<AgentOrderDTO> getAgentOrderList(Map<String, Object> param);

    //按月已完成订单列表
    List<AgentOrderDTO> getAgentFinishOrderList(Map<String, Object> param);

    int getCaAgentOrderSumCount(Map<String, Object> param);

    //ca下的用户订单记录(带总金额)H5
    List<AgentOrderDTO> getCaAgentOrderSumList(Map<String, Object> param);

    int getSaAgentOrderSumCount(Map<String, Object> param);

    //sa下的店铺订单记录
    List<AgentOrderDTO> getSaAgentOrderSumList(Map<String, Object> param);

    /******************************app统计************************************/

    //代理商下今天结算金额统计
    AgtAmountQtyDTO getAgtYearStatics(Map<String, Object> param);

    /**
     *统计chart图(每个月代理商结算数据)
     *
     * @param param
     * @return
     */
    List<AgtAmountQtyDTO> getAgentOrderStatics(Map<String, Object> param);


    /**
     * 查询代理商代理商户信息列表
     *
     * @param agent
     * @return
     */
    List<AgentStoreMap> getAgentStoreInfo(Agent agent);

    /**
     * 根据代理商类型查询代理商列表
     *
     * @param agentType
     * @return
     */
    List<Agent> getUsingAgentList(String agentType);

    /**
     *查询月度结算信息
     *
     * @param agent
     * @param yearMonth
     * @return
     */
    AgentSettlement queryAgentSettlement(Agent agent, String yearMonth);


    /**
     * 查询代理商每日结算信息
     *
     * @param agent
     * @param yesterday
     * @return
     */
    AgentDailySettlement queryAgentDailySettlement(Agent agent, String yesterday);

    /**
     * 查询代理商结算月的结算金额、代理商结算金额和VAT金额
     *
     * @Long sid
     * @return
     */
    Map<String, Object> getAgentSettleInfo(Long sid);

    /**
     * 查询统计代理商的商户月度结算信息
     *
     * @param settleId
     * @param lastYear
     * @param lastMonth
     * @param yearMonthStringNoLine
     * @return
     */
    List<AgentSettlementDetail> getAgentSettlementDetailList(Long settleId, int lastYear, int lastMonth, String yearMonthStringNoLine);

    /**
     * 查询统计代理商的商户每日结算信息
     *
     * @param settleId
     * @return
     */
    List<AgentSettlementDetail> getAgentDailySettlementDetailList(Long settleId);

    /**
     * 根据代理商编码查询代理商信息
     *
     * @param agtCode
     * @return
     */
    Agent getAgentByCode(String agtCode);

    /**
     * 获取平台、代理商总收入 平台净利润(昨天 本月 今年)
     *
     * @Param map
     * @author Dong Xifu
     * @date 2019/10/28 3:25 下午
     */
    List<PlAgtAmountDTO> getPlAgtAmountStatsSum();

    //平台金额统计
    List<PlAgtAmountDTO> getPlAmountStatsByDate(Map<String, Object> param);

    List<AgentOrderDTO> agtFeeRank(Map<String, Object> param);
}