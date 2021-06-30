package com.basoft.eorder.batch.job.threads.store;

import com.basoft.eorder.batch.job.service.IStoreBatchService;
import com.basoft.eorder.domain.model.Store;
import lombok.extern.slf4j.Slf4j;

/**
 * PG交易类商户每日结算处理线程
 *
 * @author Mentor
 * @version 1.0
 * @since 20191111
 */
@Slf4j
public class PGStoreDailySettlementThread implements Runnable {
    private IStoreBatchService storeBatchService;

    private Store pgStore;

    private String yesterday;

    private String dayStartTime;

    private String dayEndTime;

    private String today;

    public PGStoreDailySettlementThread(IStoreBatchService storeBatchService
            , Store pgStore, String yesterday, String dayStartTime, String dayEndTime, String today) {
        this.storeBatchService = storeBatchService;
        this.pgStore = pgStore;
        this.yesterday = yesterday;
        this.dayStartTime = dayStartTime;
        this.dayEndTime = dayEndTime;
        this.today = today;
    }

    /**
     * 商户计费信息定时处理线程
     */
    @Override
    public void run() {
        try {
            storeBatchService.pgStoreDailySettle(pgStore, yesterday, dayStartTime, dayEndTime, today);
        } catch (Exception e) {
            log.error("【PG交易类商户每日结算】商户结算异常，异常信息为：" + e.getMessage(), e);
        }
    }
}
