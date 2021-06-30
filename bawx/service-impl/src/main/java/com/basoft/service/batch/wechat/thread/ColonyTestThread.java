package com.basoft.service.batch.wechat.thread;

import com.basoft.core.ware.wechat.domain.statistic.ArticleSummary;
import com.basoft.core.ware.wechat.util.WeixinStatisticUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.enumerate.BatchEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:10 2019/6/26
 **/
public class ColonyTestThread implements Runnable {
    private final transient Log logger = LogFactory.getLog(this.getClass());

    private WechatService wechatService;

    /**
     * 系统ID
     */
    private String sysId;

    /**
     * SHOPID
     */
    private Long shopId;

    public ColonyTestThread(WechatService wechatService,String sysId,Long shopId) {
        this.wechatService = wechatService;
        this.sysId = sysId;
        this.shopId = shopId;
    }

    @Override
    public void run() {

        try {
            logger.info("=================colonyTestThread.run() =====================");

            System.out.println("执行中执行中执行中执行中执行中执行中执行中执行中执行中执行中执行中");
            //定时任务解锁
            wechatService.updateWxBatchLockEnd(sysId, BatchEnum.batchColonyTest.getType());
            logger.info("=================colonyTestThread.end() =====================");
        } catch (Exception e) {
            //定时任务解锁
            wechatService.updateWxBatchLockEnd(sysId, BatchEnum.batchColonyTest.getType());

            logger.error("======ren(" + shopId + ") error========");
            logger.error(e.getMessage());
        }
    }
}
