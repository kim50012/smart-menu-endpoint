package com.basoft.eorder.batch.job.threads.store;

import com.basoft.eorder.batch.job.service.IStoreBatchService;
import com.basoft.eorder.domain.model.Store;
import lombok.extern.slf4j.Slf4j;

/**
 * 商户月度结算处理线程(手工调用)
 *
 * @author Mentor
 * @version 1.0
 * @since 20190827
 */
@Slf4j
public class PGStoreMonthSettlementThreadManual implements Runnable {
    private IStoreBatchService storeBatchService;

    private Store store;

    private int lastYear;

    private int lastMonth;

    // 暂时无用
    private int year;

    // 暂时无用
    private int month;

    private String monthStartTime;

    private String monthEndTime;

    private String target;

    public PGStoreMonthSettlementThreadManual(IStoreBatchService storeBatchService
            , Store store, int lastYear, int lastMonth, int year
            , int month, String monthStartTime, String monthEndTime, String target) {
        this.storeBatchService = storeBatchService;
        this.store = store;
        this.lastYear = lastYear;
        this.lastMonth = lastMonth;
        this.year = year;
        this.month = month;
        this.monthStartTime = monthStartTime;
        this.monthEndTime = monthEndTime;
        this.target = target;
    }

    /**
     * 商户计费信息定时处理线程
     */
    @Override
    public void run() {
        try {
            storeBatchService.pgStoreMonthSettleManual(store, lastYear,
                    lastMonth, year, month, monthStartTime, monthEndTime, target);
        } catch (Exception e) {
            log.error("【MANUAL】【商户月度结算】商户结算异常，异常信息为：" + e.getMessage(), e);
        }
    }
}
