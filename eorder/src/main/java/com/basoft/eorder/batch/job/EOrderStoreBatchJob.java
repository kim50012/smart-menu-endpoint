package com.basoft.eorder.batch.job;

import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.batch.job.service.IStoreBatchService;
import com.basoft.eorder.batch.job.threads.HotelPriceThread;
import com.basoft.eorder.batch.job.threads.MonthUpdateStoreChargeInfoThread;
import com.basoft.eorder.batch.job.threads.StoreDailyGradeThread;
import com.basoft.eorder.batch.job.threads.store.BAStoreDailySettlementThread;
import com.basoft.eorder.batch.job.threads.store.BAStoreMonthSettlementThread;
import com.basoft.eorder.batch.job.threads.store.PGStoreDailySettlementThread;
import com.basoft.eorder.batch.job.threads.store.PGStoreMonthSettlementThread;
import com.basoft.eorder.batch.lock.BatchJobUtil;
import com.basoft.eorder.batch.lock.RedissonUtil;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.basoft.eorder.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * EOrder定时任务
 *
 * @author Mentor
 * @version 1.0
 * @since 20190826
 */
@Slf4j
@Component
public class EOrderStoreBatchJob {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    IStoreBatchService storeBatchService;

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private InventoryHotelQuery inventoryHotelQuery;

    /**
     * 【商户结算】
     * 商户计费信息定时处理任务，该定时任务必须在“商户月度结算”定时任务之前。
     * 商户计费信息定时处理任务，同时处理PG交易类和BA交易类商户。
     * <p>
     * 每月1号0点10和1点10分执行任务  0 10 0,1 1 * ?
     */
    @Scheduled(cron = "0 10 0,1 1 * ?")
    // @Scheduled(cron = "0 0/5 * * * ?")//20190906生产上线，10分钟一次
    public void updateStoreChargeInfo() {
        log.info("<><><><><><><><><>><><><><>开始更新商户月度计费信息，定时任务启动<><><><><><><><><>><><><><>");
        String batchKey = "MONTH_UPDATE_STORE_CHARGE_INFO";
        RLock lock = redissonUtil.getRLock(batchKey);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                // 获取所有商户列表
                List<Store> storeList = storeRepository.getUsingStoreList();
                log.info("【商户月度计费信息更新】待处理商户数量为：" + storeList.size());


                int lastYear = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "yyyy"));
                int lastMonth = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "MM"));
                int year = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now(), "yyyy"));
                int month = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now(), "MM"));
                log.info("【商户月度计费信息更新】年月信息lastYear（上月所在年），lastMonth（上月），year（当前月所在年），month（当前月）::" + lastYear + ", "
                        + lastMonth + ", " + year + ", " + month);

                for (Store store : storeList) {
                    try {
                        Thread thread = new Thread(new MonthUpdateStoreChargeInfoThread(storeBatchService,
                                store, lastYear, lastMonth, year, month));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【商户月度计费信息更新】商户处理线程异常，[商户ID]=" + store.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【商户月度计费信息更新】没有获取到定时任务执行锁:{},ThreadName :{}", batchKey, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【商户月度计费信息更新】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            // 无锁返回
            if (!getLock) {
                return;
            }

            // 有锁进行释放，并进行延时40秒释放
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("【商户月度计费信息更新】延时释放执行锁异常：" + e.getMessage(), e);
            }

            lock.unlock();
            log.info("【商户月度计费信息更新】释放锁完成，锁名称：{},当前线程名称ThreadName :{}", batchKey, Thread.currentThread().getName());
        }
        log.info("<><><><><><><><><>><><><><>更新商户月度计费信息定时任务主线程执行结束<><><><><><><><><>><><><><>");
    }

    /**
     * 【PG交易类商户结算】
     * PG交易类商户月度结算
     * 该定时任务必须在“商户计费信息定时处理任务”之后。
     * <p>
     * 每月1号3点执行任务  0 0 3 10 * ?
     */
    @Scheduled(cron = "0 0 3 1 * ?") //生产
    // @Scheduled(cron = "0 0/10 * * * ?")//20190906生产上线，10分钟一次
    // @Scheduled(cron = "0 0 0/2 * * ?")
    public void pgStoreMonthSettlement() {
        log.info("<><><><><><><><><>><><><><>开始PG交易类商户月度结算，定时任务启动<><><><><><><><><>><><><><>");
        String batchKey = "PG_STORE_MONTH_SETTLEMENT_KEY";
        RLock lock = redissonUtil.getRLock(batchKey);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                // 获取所有PG交易类商户列表（获取非删除的商户列表，状态非3）
                List<Store> pgStoreList = storeRepository.getUsingPGOrBAStoreList(CommonConstants.CASH_SETTLE_TYPE_PG);
                log.info("【PG交易类商户月度结算】待结算商户数量为：" + pgStoreList.size());

                int lastYear = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "yyyy"));
                int lastMonth = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "MM"));
                // 暂时无用
                int year = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now(), "yyyy"));
                // 暂时无用
                int month = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now(), "MM"));
                log.info("【PG交易类商户月度结算】年月信息lastYear（上月所在年），lastMonth（上月），year（当前月所在年），month（当前月）::" + lastYear + ", "
                        + lastMonth + ", " + year + ", " + month);

                String monthStartTime = DateUtil.getFirstDayOfMonth(lastYear, lastMonth) + " 00:00:00";
                String monthEndTime = DateUtil.getLastDayOfMonth(lastYear, lastMonth) + " 23:59:59";
                log.info("【PG交易类商户月度结算】商户结算月起始时间和截止时间::" + monthStartTime + ", " + monthEndTime);

                for (Store pgStore : pgStoreList) {
                    try {
                        Thread thread = new Thread(new PGStoreMonthSettlementThread(storeBatchService,
                                pgStore, lastYear, lastMonth, year, month, monthStartTime, monthEndTime));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【PG交易类商户月度结算】商户月度结算线程异常，[商户ID]=" + pgStore.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【PG交易类商户月度结算】没有获取到定时任务执行锁:{},ThreadName :{}", batchKey, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【PG交易类商户月度结算】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            // 无锁返回
            if (!getLock) {
                return;
            }

            // 有锁进行释放，并进行延时40秒释放
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("【PG交易类商户月度结算】延时释放执行锁异常：" + e.getMessage(), e);
            }

            lock.unlock();
            log.info("【PG交易类商户月度结算】释放锁完成，锁名称：{},当前线程名称ThreadName :{}", batchKey, Thread.currentThread().getName());
        }
        log.info("<><><><><><><><><>><><><><>PG交易类商户月度结算定时任务主线程执行结束<><><><><><><><><>><><><><>");
    }

    /**
     * 【PG交易类商户结算】
     * PG交易类商户每日结算
     * 该定时任务必须在“商户计费信息定时处理任务”之后。
     * <p>
     * 每天2点执行任务  0 0 2 * * ?
     */
    @Scheduled(cron = "0 0 2 * * ?") //生产
    // @Scheduled(cron = "0 26 18 * * ?")
    // @Scheduled(cron = "0 0/20 * * * ?")
    // @Scheduled(cron = "0 0 0/3 * * ?")
    public void pgStoreDailySettlement() {
        log.info("<><><><><><><><><>><><><><>开始PG交易类商户每日结算，定时任务启动<><><><><><><><><>><><><><>");
        String batchKey = "PG_STORE_DAILY_SETTLEMENT_KEY";
        RLock lock = redissonUtil.getRLock(batchKey);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                // 获取所有PG交易类商户列表
                List<Store> pgStoreList = storeRepository.getUsingPGOrBAStoreList(CommonConstants.CASH_SETTLE_TYPE_PG);
                log.info("【PG交易类商户每日结算】待结算商户数量为：" + pgStoreList.size());

                // 获取昨天的日期 yyyy-MM-dd
                String yesterday = DateUtil.getYesterday();
                String dayStartTime = yesterday + " 00:00:00";
                String dayEndTime = yesterday + " 23:59:59";
                String today = DateUtil.getToday();
                log.info("【PG交易类商户商户每日结算】结算日期：{}，结算起始时间：{}，结算结束时间：{}, 当前日期：{}",
                        yesterday, dayStartTime, dayEndTime, today);

                for (Store pgStore : pgStoreList) {
                    try {
                        Thread thread = new Thread(new PGStoreDailySettlementThread(storeBatchService,
                                pgStore, yesterday, dayStartTime, dayEndTime, today));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【PG交易类商户每日结算】商户每日结算线程异常，[商户ID]=" + pgStore.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【PG交易类商户每日结算】没有获取到定时任务执行锁:{},ThreadName :{}", batchKey, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【PG交易类商户每日结算】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            // 无锁返回
            if (!getLock) {
                return;
            }

            // 有锁进行释放，并进行延时40秒释放
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("【PG交易类商户每日结算】延时释放执行锁异常：" + e.getMessage(), e);
            }

            lock.unlock();
            log.info("【PG交易类商户每日结算】释放锁完成，锁名称：{},当前线程名称ThreadName :{}", batchKey, Thread.currentThread().getName());
        }
        log.info("<><><><><><><><><>><><><><>PG交易类商户每日结算定时任务主线程执行结束<><><><><><><><><>><><><><>");
    }

    /**
     * 【交易金BA结算类商户结算】
     * BA交易类商户月度结算
     * 该定时任务必须在“商户计费信息定时处理任务”之后。
     * <p>
     * 每月1号5点执行任务  0 0 5 1 * ?
     */
    @Scheduled(cron = "0 0 5 1 * ?") // 生产
    // @Scheduled(cron = "0 45 17 * * ?")
    // @Scheduled(cron = "0 0/50 * * * ?")
    public void baStoreMonthSettlement() {
        log.info("<><><><><><><><><>><><><><>开始BA交易类商户月度结算，定时任务启动<><><><><><><><><>><><><><>");
        String batchKey = "BA_STORE_MONTH_SETTLEMENT_KEY";
        RLock lock = redissonUtil.getRLock(batchKey);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                // 获取所有BA交易类商户列表（获取非删除的商户列表，状态非3）
                List<Store> baStoreList = storeRepository.getUsingPGOrBAStoreList(CommonConstants.CASH_SETTLE_TYPE_BA);
                log.info("【BA交易类商户月度结算】待结算商户数量为：" + baStoreList.size());

                int lastYear = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "yyyy"));
                int lastMonth = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().minus(1, ChronoUnit.MONTHS), "MM"));
                // 暂时无用
                int year = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now(), "yyyy"));
                // 暂时无用
                int month = Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now(), "MM"));
                log.info("【BA交易类商户月度结算】年月信息lastYear（上月所在年），lastMonth（上月），year（当前月所在年），month（当前月）::" + lastYear + ", "
                        + lastMonth + ", " + year + ", " + month);

                String monthStartTime = DateUtil.getFirstDayOfMonth(lastYear, lastMonth) + " 00:00:00";
                String monthEndTime = DateUtil.getLastDayOfMonth(lastYear, lastMonth) + " 23:59:59";
                log.info("【BA交易类商户月度结算】商户结算月起始时间和截止时间::" + monthStartTime + ", " + monthEndTime);

                for (Store baStore : baStoreList) {
                    try {
                        Thread thread = new Thread(new BAStoreMonthSettlementThread(storeBatchService,
                                baStore, lastYear, lastMonth, year, month, monthStartTime, monthEndTime));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【BA交易类商户月度结算】商户月度结算线程异常，[商户ID]=" + baStore.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【BA交易类商户月度结算】没有获取到定时任务执行锁:{},ThreadName :{}", batchKey, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【BA交易类商户月度结算】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            // 无锁返回
            if (!getLock) {
                return;
            }

            // 有锁进行释放，并进行延时40秒释放
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("【BA交易类商户月度结算】延时释放执行锁异常：" + e.getMessage(), e);
            }

            lock.unlock();
            log.info("【BA交易类商户月度结算】释放锁完成，锁名称：{},当前线程名称ThreadName :{}", batchKey, Thread.currentThread().getName());
        }
        log.info("<><><><><><><><><>><><><><>BA交易类商户月度结算定时任务主线程执行结束<><><><><><><><><>><><><><>");
    }

    /**
     * 【BA交易类商户结算】
     * BA交易类商户每日结算
     * 该定时任务必须在“商户计费信息定时处理任务”之后。
     * <p>
     * 每天5点执行任务  0 0 5 * * ?
     */
    @Scheduled(cron = "0 0 5 * * ?") //生产
    // @Scheduled(cron = "0 38 18 * * ?")
    // @Scheduled(cron = "0 0/55 * * * ?")
    public void baStoreDailySettlement() {
        log.info("<><><><><><><><><>><><><><>开始BA交易类商户每日结算，定时任务启动<><><><><><><><><>><><><><>");
        String batchKey = "BA_STORE_DAILY_SETTLEMENT_KEY";
        RLock lock = redissonUtil.getRLock(batchKey);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                // 获取所有BA交易类商户列表
                List<Store> baStoreList = storeRepository.getUsingPGOrBAStoreList(CommonConstants.CASH_SETTLE_TYPE_BA);
                log.info("【BA交易类商户每日结算】待结算商户数量为：" + baStoreList.size());

                // 获取昨天的日期 yyyy-MM-dd
                String yesterday = DateUtil.getYesterday();
                String dayStartTime = yesterday + " 00:00:00";
                String dayEndTime = yesterday + " 23:59:59";
                String today = DateUtil.getToday();
                log.info("【BA交易类商户商户每日结算】结算日期：{}，结算起始时间：{}，结算结束时间：{}, 当前日期：{}",
                        yesterday, dayStartTime, dayEndTime, today);

                for (Store baStore : baStoreList) {
                    try {
                        Thread thread = new Thread(new BAStoreDailySettlementThread(storeBatchService,
                                baStore, yesterday, dayStartTime, dayEndTime, today));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【BA交易类商户商户每日结算】商户每日结算线程异常，[商户ID]=" + baStore.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【BA交易类商户商户每日结算】没有获取到定时任务执行锁:{},ThreadName :{}", batchKey, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【BA交易类商户商户每日结算】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            // 无锁返回
            if (!getLock) {
                return;
            }

            // 有锁进行释放，并进行延时40秒释放
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("【BA交易类商户商户每日结算】延时释放执行锁异常：" + e.getMessage(), e);
            }

            lock.unlock();
            log.info("【BA交易类商户商户每日结算】释放锁完成，锁名称：{},当前线程名称ThreadName :{}", batchKey, Thread.currentThread().getName());
        }
        log.info("<><><><><><><><><>><><><><>BA交易类商户每日结算定时任务主线程执行结束<><><><><><><><><>><><><><>");
    }

    public static String storeGradeLog = "开始商户每日评分定时任务";
    public static String storeDailyGradeKey = "STORE_DAILY_GRADE_KEY";

    /**
     * 店铺评分,评论条数
     * 每晚3点执行
     */
    @Scheduled(cron = "0 10 9 * * ?")
    // @Scheduled(cron = "0 0/5 * * * ?")
    private void storeGrade() {
        log.info("<><><><><><><><><>><><><><>" + storeGradeLog + "启动<><><><><><><><><>><><><><>");
        RLock lock = redissonUtil.getRLock(storeDailyGradeKey);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                // 获取所有商户列表
                List<Store> storeList = storeRepository.getUsingStoreList();
                log.info("【" + storeGradeLog + "】待修改商户数量为：" + storeList.size());

                String today = DateUtil.getToday();
                log.info("【" + storeGradeLog + "】评分日期=当前日期：{}", today);

                for (Store store : storeList) {
                    try {
                        Thread thread = new Thread(new StoreDailyGradeThread(storeBatchService,
                                store, today));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【" + storeGradeLog + "】商户每日评分线程异常，[商户ID]=" + store.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【" + storeGradeLog + "】没有获取到定时任务执行锁:{},ThreadName :{}", storeDailyGradeKey, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【" + storeGradeLog + "】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            BatchJobUtil.finalBatch(getLock, lock, "【" + storeGradeLog + "】", storeDailyGradeKey);
        }
        log.info("<><><><><><><><><>><><><><>" + storeGradeLog + "主线程执行结束<><><><><><><><><>><><><><>");
    }


    public static String hotelPriceLog = "酒店每日最低价定时任务";
    public static String hotelPriceJobKey = "酒店每日最低价定时任务";

    /**
     * 酒店未来最低价
     * 每晚12点10执行
     *
     * @author Dong Xifu
     * @date 12/10/19 10:16 AM
     */
    @Scheduled(cron = "0 10 0 * * ?")
    // @Scheduled(cron = "0 0/5 * * * ?")
    private void hotelFuturePrice() {
        log.info("<><><><><><><><><>><><><><>" + hotelPriceLog + "启动<><><><><><><><><>><><><><>");
        RLock lock = redissonUtil.getRLock(hotelPriceJobKey);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                //店铺每月结算列表
                List<Store> storeList = storeRepository.getUsingStoreList();
                storeList = storeList.stream().filter(s -> s.storeType() == 4).collect(Collectors.toList());
                //storeList = storeList.stream().filter(s -> s.id().longValue()==709049140512625668L).collect(Collectors.toList());
                log.info("【" + hotelPriceLog + "】待处理商户数量为：" + storeList.size());
                UserSession us = new UserSession();
                for (Store store : storeList) {
                    try {
                        us = new UserSession.Builder().storeId(store.id()).account("batchJob").build();
                        Thread thread = new Thread(new HotelPriceThread(us, storeRepository, inventoryHotelQuery));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【" + hotelPriceLog + "】商户处理线程异常，[商户ID]=" + store.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【" + hotelPriceLog + "】没有获取到定时任务执行锁:{},ThreadName :{}", hotelPriceJobKey, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【" + hotelPriceLog + "】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            BatchJobUtil.finalBatch(getLock, lock, "【" + hotelPriceLog + "】", hotelPriceJobKey);
        }
        log.info("<><><><><><><><><>><><><><>" + hotelPriceLog + "结束<><><><><><><><><>><><><><>");
    }
}