package com.basoft.eorder.batch.job.service;

import com.basoft.eorder.domain.model.Store;

public interface IStoreBatchService {
    /**
     * 处理并同步商户指定年月的计费信息
     *
     * @param store
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     */
    public void dealStoreChargeInfoPerMonth(Store store, int lastYear, int lastMonth, int year, int month) throws Exception;

    /**
     * PG交易类商户月度结算(定时任务)
     *
     * @param pgStore
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     * @param monthStartTime
     * @param monthEndTime
     */
    public void pgStoreMonthSettle(Store pgStore, int lastYear, int lastMonth, int year,
                                   int month, String monthStartTime, String monthEndTime) throws Exception;

    /**
     * 商户月度结算(手工)
     * 同定时任务结算逻辑完全相同，@see storeMonthSettle
     *
     * @param store
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     * @param monthStartTime
     * @param monthEndTime
     * @param target
     */
    public void pgStoreMonthSettleManual(Store store, int lastYear, int lastMonth, int year,
                                         int month, String monthStartTime, String monthEndTime, String target) throws Exception;

    /**
     * PG交易类商户每日结算
     *
     * @param pgStore
     * @param yesterday
     * @param dayStartTime
     * @param dayEndTime
     * @param today
     */
    public void pgStoreDailySettle(Store pgStore, String yesterday, String dayStartTime, String dayEndTime, String today);





    /**
     * BA交易类商户月度结算(定时任务)
     *
     * @param baStore
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     * @param monthStartTime
     * @param monthEndTime
     */
    public void baStoreMonthSettle(Store baStore, int lastYear, int lastMonth, int year,
                                   int month, String monthStartTime, String monthEndTime) throws Exception;

    /**
     * BA交易类商户每日结算
     *
     * @param baStore
     * @param yesterday
     * @param dayStartTime
     * @param dayEndTime
     * @param today
     */
    public void baStoreDailySettle(Store baStore, String yesterday, String dayStartTime, String dayEndTime, String today);





    /**
     * BA交易类商户月度结算(定时任务) 4 DAOSHOU HOTEL
     *
     * @param baStore
     * @param lastYear
     * @param lastMonth
     * @param year
     * @param month
     * @param monthStartTime
     * @param monthEndTime
     */
    public void baStoreMonthSettle4Hotel(Store baStore, int lastYear, int lastMonth, int year,
                                   int month, String monthStartTime, String monthEndTime) throws Exception;

    /**
     * BA交易类商户每日结算  4 DAOSHOU HOTEL
     *
     * @param baStore
     * @param yesterday
     * @param dayStartTime
     * @param dayEndTime
     * @param today
     */
    public void baStoreDailySettle4Hotel(Store baStore, String yesterday, String dayStartTime, String dayEndTime, String today);





    /**
     * 店铺每日评分平均值计算
     * 每日评论数
     *
     * @param store
     * @param dayEndTime
     */
    public void saveStoreDailyGrade(Store store, String dayEndTime);
}