package com.basoft.eorder.batch.job.threads;

import com.basoft.eorder.batch.job.service.IStoreBatchService;
import com.basoft.eorder.domain.model.Store;
import lombok.extern.slf4j.Slf4j;

/**
 * 商户计费信息定时处理线程
 *
 * @author Mentor
 * @version 1.0
 * @since 20190826
 */
@Slf4j
public class MonthUpdateStoreChargeInfoThread implements Runnable {
    private IStoreBatchService storeBatchService;

    private Store store;

    private int lastYear;

    private int lastMonth;

    private int year;

    private int month;

    public MonthUpdateStoreChargeInfoThread(IStoreBatchService storeBatchService
            , Store store, int lastYear, int lastMonth, int year, int month) {
        this.storeBatchService = storeBatchService;
        this.store = store;
        this.lastYear = lastYear;
        this.lastMonth = lastMonth;
        this.year = year;
        this.month = month;
    }

    /**
     * 商户计费信息定时处理线程
     */
    @Override
    public void run() {
        try {
            storeBatchService.dealStoreChargeInfoPerMonth(store, lastYear, lastMonth, year, month);
        } catch (Exception e) {
            log.error("【商户月度计费信息更新】计费信息更新异常，异常信息为：" + e.getMessage(), e);
        }
    }
}
