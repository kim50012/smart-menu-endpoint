package com.basoft.eorder.domain;

import com.basoft.eorder.batch.job.model.agent.AgentDailySettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlementDetail;
import com.basoft.eorder.batch.job.model.agent.AgentSettlementOrder;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.domain.model.agent.AgentAimMap;
import com.basoft.eorder.domain.model.agent.QRCodeAgent;

import java.util.List;

public interface AgentRepository {
    //新增
    Long insertAgent(Agent agent);

    //修改
    Long updateAgent(Agent agent);

	//续约
	Long renewAgent(Agent agent);

	//修改状态
	Long updateAgentStatus(Agent agent);

    //新增代理商店铺中间表
    int insertAgentStoreMap(AgentAimMap agentAimMap);

    //修改代理商店铺中间表
    int updateAgentStoreMap(AgentAimMap agentAimMap);

    //新增代理商微信用户中间表
    Long insertAgentWxMap(AgentAimMap agentAimMap);

    //修改代理商微信用户中间表
    Long updateAgentWxMap(AgentAimMap agentAimMap);

    /**
     * 根据代理商二维码信息更新代理商信息
     *
     * @param agentQRCode
     */
    void updateQRCodeAgent(QRCodeAgent agentQRCode);

    /**
     * 新增代理商二维码信息
     *
     * @param agentQRCode
     */
    void insertQRCodeAgent(QRCodeAgent agentQRCode);


    /**********************************************代理商月度结算-start**************************************************/
    /**
     * 新增代理商月度结算信息
     *
     * @param newAgentSettlement
     * @return
     */
    int insertAgentSettlement(AgentSettlement newAgentSettlement);

    /**
     * 批量新增代理商结算订单
     *
     * @param settleId
     * @param agentAllOrderList
     * @return
     */
    int batchInsertAgentOrder(Long settleId, List<AgentSettlementOrder> agentAllOrderList);

    /**
     * 更新代理商结算信息（订单数量、结算金额、代理商结算金额、VAT金额）
     *
     * @param agentSettlement
     * @return
     */
    int updateAgentSettlement(AgentSettlement agentSettlement);

    /**
     * 批量新增结算明细
     *
     * @param settleId
     * @param agentSettlementDetailList
     * @return
     */
    int batchInsertAgentOrderDetail(Long settleId, List<AgentSettlementDetail> agentSettlementDetailList);
    /**********************************************代理商月度结算-end**************************************************/

    /**********************************************代理商每日结算-start**************************************************/
    /**
     * 新增代理商每日结算信息
     *
     * @param newAgentDailySettlement
     * @return
     */
    int insertAgentDailySettlement(AgentDailySettlement newAgentDailySettlement);

    /**
     * 更新代理商每日结算信息（订单数量、结算金额、代理商结算金额、VAT金额）
     *
     * @param agentDailySettlement
     * @return
     */
    int updateAgentDailySettlement(AgentDailySettlement agentDailySettlement);
    /**********************************************代理商月度结算-end**************************************************/

}
