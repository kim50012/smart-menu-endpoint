package com.basoft.eorder.batch.job.threads;

import com.basoft.eorder.batch.job.service.IAgentBatchService;
import com.basoft.eorder.domain.model.agent.Agent;
import lombok.extern.slf4j.Slf4j;

/**
 * SA代理商每日结算处理线程
 *
 * @author Mentor
 * @version 1.0
 * @since 20190827
 */
@Slf4j
public class AgentSADailySettlementThread implements Runnable {
    private IAgentBatchService agentBatchService;

    private Agent agent;

    private String yesterday;

    private String dayStartTime;

    private String dayEndTime;

    private String today;

    public AgentSADailySettlementThread(IAgentBatchService agentBatchService
            , Agent agent, String yesterday, String dayStartTime, String dayEndTime, String today) {
        this.agentBatchService = agentBatchService;
        this.agent = agent;
        this.yesterday = yesterday;
        this.dayStartTime = dayStartTime;
        this.dayEndTime = dayEndTime;
        this.today = today;
    }

    /**
     * SA代理商每日结算定时处理线程
     */
    @Override
    public void run() {
        try {
            agentBatchService.storeAgentDailySettle(agent, yesterday, dayStartTime, dayEndTime, today);
        } catch (Exception e) {
            log.error("【SA代理商每日结算】SA代理商每日结算异常，异常信息为：" + e.getMessage(), e);
        }
    }
}