package com.basoft.eorder.batch.job;

import com.basoft.eorder.batch.job.threads.RetailCompleteOrderThread;
import com.basoft.eorder.batch.job.threads.UpdateOrderStatusThread;
import com.basoft.eorder.batch.lock.BatchJobUtil;
import com.basoft.eorder.batch.lock.RedissonUtil;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.retail.RetailOrderRepository;
import com.basoft.eorder.interfaces.query.OrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 2:56 PM 12/5/19
 **/
@Slf4j
@Component
public class StoreOrderStatusJob {
    protected static final String batch_job_key = "BATCH_STORE_ORDER_STATUS";
    public static String logTitle = "点餐酒店订单状态完成定时任务";
    public static String retail_complete_log = "retail订单状态完成定时任务";
    @Autowired
    private RedissonUtil redissonUtil;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderQuery orderQuery;
    @Autowired
    private RetailOrderRepository retailOrderRepository;

    /**
     *
     * 点餐和酒店订单完成定时任务
     */
    @Scheduled(cron = "0 40 1 * * ?")
    //@Scheduled(cron = "0 0/3 * * * ?")
    private void completeOrder() {
        log.info("<><><><><><><><><>><><><><>"+logTitle+"启动<><><><><><><><><>><><><><>");
        RLock lock = redissonUtil.getRLock(batch_job_key);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                //店铺列表
                List<Store> storeList = storeRepository.getUsingStoreList();
                //只修改点餐和酒店的订单
                storeList = storeList.stream().filter(s -> s.storeType() == 1 || s.storeType() == 4).collect(Collectors.toList());
                log.info("【"+logTitle+"】待处理商户数量为：" + storeList.size());
                for (Store store : storeList) {
                    try {
                        Thread thread = new Thread(new UpdateOrderStatusThread(store,orderQuery,orderRepository));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【"+logTitle+"】商户处理线程异常，[商户ID]=" + store.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【"+logTitle+"】没有获取到定时任务执行锁:{},ThreadName :{}", batch_job_key, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【"+logTitle+"】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            BatchJobUtil.finalBatch(getLock, lock, "【"+logTitle+"】", batch_job_key);
        }
        log.info("<><><><><><><><><>><><><><>"+logTitle+"结束<><><><><><><><><>><><><><>");
    }


    /**
     * 零售业务订单批处理任务
     * 处理逻辑：当零售业务快递配送订单（配送类型-order_info表中shipping_type为4或5 4-配送到韩国 5-配送到中国）处于订单状态10，
     * 且发货时间超过30天，处理该订单状态为9，且记录订单确认收货事件12
     *
     * 线程处理每个订单，每个线程时间间隔10秒吧，缓解数据库压力
     *
     * <p>
     * 每晚3点执行一次
     */
    //@Scheduled(cron = "0 0 3 * * ?")// 生产
     @Scheduled(cron = "0 25 12 * * ?") // 开发调试
    public void retailOrderCompleteBatch() {
        log.info("<><><><><><><><><>><><><><>"+retail_complete_log+"启动<><><><><><><><><>><><><><>");
        RLock lock = redissonUtil.getRLock(batch_job_key);
        boolean getLock = false;
        try {
            // 零等待取锁，加锁120秒
            if (getLock = lock.tryLock(0, 120, TimeUnit.SECONDS)) {
                //店铺列表
                List<Store> storeList = storeRepository.getUsingStoreList();
                storeList = storeList.stream().filter(s -> s.storeType() == 3).collect(Collectors.toList());
                log.info("【"+retail_complete_log+"】待处理商户数量为：" + storeList.size());
                for (Store store : storeList) {
                    try {
                        Thread thread = new Thread(new RetailCompleteOrderThread(store,orderQuery,orderRepository,retailOrderRepository));
                        thread.start();
                    } catch (Exception e) {
                        log.error("【"+retail_complete_log+"】商户处理线程异常，[商户ID]=" + store.id() + "，异常信息为：" + e.getMessage(), e);
                    }
                    // 休眠400毫秒，减轻数据库的压力
                    Thread.sleep(400);
                }
            } else {
                log.info("【"+retail_complete_log+"】没有获取到定时任务执行锁:{},ThreadName :{}", batch_job_key, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【"+retail_complete_log+"】获取分布式锁异常：" + e.getMessage(), e);
        } finally {
            BatchJobUtil.finalBatch(getLock, lock, "【"+retail_complete_log+"】", batch_job_key);
        }
        log.info("<><><><><><><><><>><><><><>"+retail_complete_log+"结束<><><><><><><><><>><><><><>");
    }
}
