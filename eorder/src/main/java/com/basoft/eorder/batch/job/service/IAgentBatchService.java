package com.basoft.eorder.batch.job.service;

import com.basoft.eorder.domain.model.agent.Agent;

public interface IAgentBatchService {
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
                                      int month, String monthStartTime, String monthEndTime) throws Exception;

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
    public void customerAgentMonthSettle(Agent agent, int lastYear, int lastMonth, int year, int month, String monthStartTime, String monthEndTime);

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
    void storeAgentDailySettle(Agent agent, String yesterday, String dayStartTime, String dayEndTime, String today);

    /**
     * CA代理商每日结算(定时任务)
     *
     * @param agent
     * @param yesterday
     * @param dayStartTime
     * @param dayEndTime
     * @param today
     */
    void customerAgentDailySettle(Agent agent, String yesterday, String dayStartTime, String dayEndTime, String today);
}