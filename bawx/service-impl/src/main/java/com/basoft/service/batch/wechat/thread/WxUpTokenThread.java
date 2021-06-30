package com.basoft.service.batch.wechat.thread;

import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 更新access_token线程
 *
 * @author dxf
 */
public class WxUpTokenThread implements Runnable {
    private final transient Log logger = LogFactory.getLog(this.getClass());

    private WechatService wechatService;

    /**
     * 系统ID
     */
    private String sysId;

    /**
     * 获取token线程方法
     *
     * @param wechatService
     * @param sysId         系统ID
     */
    public WxUpTokenThread(WechatService wechatService, String sysId) {
        this.wechatService = wechatService;
        this.sysId = sysId;
    }

    @Override
    public void run() {
        Long shopId = null;
        try {
            logger.info("=================WxUpTokenThread.run() =====================");
            AppInfoWithBLOBs appInfoWithBLOBs = wechatService.selectAppInfoByKey(sysId);
            shopId = appInfoWithBLOBs.getShopId();

            // get AccessToken
            wechatService.setAccessToken(shopId);

            // get Api Ticket
            //wechatService.getApiTicket(shopId);
            wechatService.setApiTicket(shopId);
        } catch (Exception e) {
            logger.error("=================WxUpTokenThread.run() >>>(" + shopId + ") =====================");
            logger.error(e.getMessage());
        }
    }
}