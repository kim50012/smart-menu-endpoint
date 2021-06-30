package com.basoft.eorder.batch.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:19 下午 2019/11/27
 **/
@Slf4j
public class BatchJobUtil {
    /**
     * 释放锁
     *
     * @param getLock
     * @param lock
     * @param message
     */
    public static void finalBatch(Boolean getLock, RLock lock, String message, String batchKey) {
        // 无锁返回
        if (!getLock) {
            return;
        }

        // 有锁进行释放，并进行延时40秒释放
        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(message + "延时释放执行锁异常：" + e.getMessage(), e);
        }

        lock.unlock();
        log.info(message + "释放锁完成，锁名称：{},当前线程名称ThreadName :{}", batchKey, Thread.currentThread().getName());
    }
}
