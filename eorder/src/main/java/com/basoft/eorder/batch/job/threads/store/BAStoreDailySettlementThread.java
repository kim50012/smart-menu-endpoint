package com.basoft.eorder.batch.job.threads.store;

import com.basoft.eorder.batch.job.service.IStoreBatchService;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.model.Store;
import lombok.extern.slf4j.Slf4j;

/**
 * BA交易类商户每日结算处理线程
 *
 * @author Mentor
 * @version 1.0
 * @since 20200103
 */
@Slf4j
public class BAStoreDailySettlementThread implements Runnable {
    private IStoreBatchService storeBatchService;

    private Store baStore;

    private String yesterday;

    private String dayStartTime;

    private String dayEndTime;

    private String today;

    public BAStoreDailySettlementThread(IStoreBatchService storeBatchService
            , Store baStore, String yesterday, String dayStartTime, String dayEndTime, String today) {
        this.storeBatchService = storeBatchService;
        this.baStore = baStore;
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
            // 酒店商户类型全按照一个标准进行结算
            if (CommonConstants.BIZ_HOTEL_INT == baStore.storeType()) {
                storeBatchService.baStoreDailySettle4Hotel(baStore, yesterday, dayStartTime, dayEndTime, today);
            } else {
                storeBatchService.baStoreDailySettle(baStore, yesterday, dayStartTime, dayEndTime, today);
            }
        } catch (Exception e) {
            log.error("【BA交易类商户每日结算】商户结算异常，异常信息为：" + e.getMessage(), e);
        }
    }
}
