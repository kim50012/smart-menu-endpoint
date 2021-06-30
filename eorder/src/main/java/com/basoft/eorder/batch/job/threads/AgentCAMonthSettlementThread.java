package com.basoft.eorder.batch.job.threads;

import com.basoft.eorder.batch.job.service.IAgentBatchService;
import com.basoft.eorder.domain.model.agent.Agent;
import lombok.extern.slf4j.Slf4j;

/**
 * CA代理商月度结算处理线程
 *
 * @author Mentor
 * @version 1.0
 * @since 20190827
 */
@Slf4j
public class AgentCAMonthSettlementThread implements Runnable {
    private IAgentBatchService agentBatchService;

    private Agent agent;

    private int lastYear;

    private int lastMonth;

    // 暂时无用
    private int year;

    // 暂时无用
    private int month;

    private String monthStartTime;

    private String monthEndTime;

    public AgentCAMonthSettlementThread(IAgentBatchService agentBatchService
            , Agent agent, int lastYear, int lastMonth, int year
            , int month, String monthStartTime, String monthEndTime) {
        this.agentBatchService = agentBatchService;
        this.agent = agent;
        this.lastYear = lastYear;
        this.lastMonth = lastMonth;
        this.year = year;
        this.month = month;
        this.monthStartTime = monthStartTime;
        this.monthEndTime = monthEndTime;
    }

    /**
     * CA代理商月度结算定时处理线程
     */
    @Override
    public void run() {
        try {
            agentBatchService.customerAgentMonthSettle(agent, lastYear, lastMonth, year, month, monthStartTime, monthEndTime);
        } catch (Exception e) {
            log.error("【CA代理商月度结算】SA代理商结算异常，异常信息为：" + e.getMessage(), e);
        }
    }
}
