package com.basoft.eorder.batch.job.threads;

import com.basoft.eorder.batch.job.service.IStoreBatchService;
import com.basoft.eorder.domain.model.Store;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:43 下午 2019/11/27
 **/
@Slf4j
public class StoreDailyGradeThread implements Runnable{
    private IStoreBatchService storeBatchService;

    private Store store;

    private String dayEndTime;

    public StoreDailyGradeThread(IStoreBatchService storeBatchService
        , Store store, String dayEndTime) {
        this.storeBatchService = storeBatchService;
        this.store = store;
        this.dayEndTime = dayEndTime;
    }

    @Override
    public void run() {
        try {
            storeBatchService.saveStoreDailyGrade(store, dayEndTime);
        } catch (Exception e) {
            log.error("【店铺每日评分】异常，异常信息为：" + e.getMessage(), e);
        }
    }
}
