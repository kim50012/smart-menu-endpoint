package com.basoft.eorder.batch.job.threads.store;

import com.basoft.eorder.batch.job.service.IStoreBatchService;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.model.Store;
import lombok.extern.slf4j.Slf4j;

/**
 * BA交易类商户月度结算处理线程
 *
 * @author Mentor
 * @version 1.0
 * @since 20200103
 */
@Slf4j
public class BAStoreMonthSettlementThread implements Runnable {
    private IStoreBatchService storeBatchService;

    private Store baStore;

    private int lastYear;

    private int lastMonth;

    // 暂时无用
    private int year;

    // 暂时无用
    private int month;

    private String monthStartTime;

    private String monthEndTime;

    public BAStoreMonthSettlementThread(IStoreBatchService storeBatchService
            , Store baStore, int lastYear, int lastMonth, int year
            , int month, String monthStartTime, String monthEndTime) {
        this.storeBatchService = storeBatchService;
        this.baStore = baStore;
        this.lastYear = lastYear;
        this.lastMonth = lastMonth;
        this.year = year;
        this.month = month;
        this.monthStartTime = monthStartTime;
        this.monthEndTime = monthEndTime;
    }

    /**
     * 商户计费信息定时处理线程
     */
    @Override
    public void run() {
        try {
            // 酒店商户类型全按照一个标准进行结算
            if (CommonConstants.BIZ_HOTEL_INT == baStore.storeType()) {
                storeBatchService.baStoreMonthSettle4Hotel(baStore, lastYear, lastMonth, year, month, monthStartTime, monthEndTime);
            } else {
                storeBatchService.baStoreMonthSettle(baStore, lastYear, lastMonth, year, month, monthStartTime, monthEndTime);
            }
        } catch (Exception e) {
            log.error("【BA交易类商户月度结算】商户结算异常，异常信息为：" + e.getMessage(), e);
        }
    }
}
