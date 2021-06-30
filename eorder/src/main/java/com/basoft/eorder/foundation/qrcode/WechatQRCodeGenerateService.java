package com.basoft.eorder.foundation.qrcode;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.QRCodeGenerateService;
import com.basoft.eorder.application.wx.api.WechatAPI;
import com.basoft.eorder.application.wx.model.QrcodeTicket;
import com.basoft.eorder.domain.AgentRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.QRCode;
import com.basoft.eorder.domain.model.StoreTable;
import com.basoft.eorder.domain.model.agent.QRCodeAgent;
import com.basoft.eorder.interfaces.query.agent.AgentQuery;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.RedisUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service("wechatQRService")
public class WechatQRCodeGenerateService implements QRCodeGenerateService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AppConfigure app;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AgentQuery agentQuery;

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public List<StoreTable> batchGenerateWechatQRCodes(List<StoreTable> collect) {
        WechatAPI wechat = WechatAPI.getInstance();

        Map<String, String> property = (Map<String, String>) app.getObject("wxPay-config").get();
        final String runningDev = property != null ? property.get("running-dev") : "real";
        final int expireSeconds = property != null ? NumberUtils.toInt(Objects.toString(property.get("qr-time"), null)) : 7200;

        String token = Objects.toString(redisUtil.get("wx_token"), null);
        if (StringUtils.isNotBlank(token)) {
            return collect.stream().map(st -> {
                QrcodeTicket ticket = null;

                if (!"real".equals(runningDev)) {
                    ticket = wechat.qrcodeCreateTemp(token, expireSeconds, st.sceneId());
                } else {
                    logger.info("qrcodeCreateFinal");
                    ticket = wechat.qrcodeCreateFinal(token, st.sceneId());
                }

                return new StoreTable().createTableByTicket(st, ticket).build();
            }).collect(Collectors.toList());
        } else {
            logger.error("<<<<<< redis wx_token is null >>>>>>");
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }
    }

    /**
     * 分配或创建二维码给餐桌。
     * （1）分配：分配存在的没有使用的QRCode给餐桌；
     * （2）创建：如果待分配的二维码不够，则需要为餐桌创建新的QRCode。
     *
     * @param collect
     * @return
     */
    @Override
    public List<StoreTable> matchGenerateWechatQRCodes(List<StoreTable> collect) {
        long storeId = collect.get(0).getStore().id();

        //没有二维码餐桌的数量
        int size = collect.size();

        /**
         *   "wxPay-config": {
         *     "running-dev":"real",
         *     "qr-time":2592000,
         *     "store-qr-cnt":50
         *   },
         */
        Map<String, String> property = (Map<String, String>) app.getObject("wxPay-config").get();

        final String runningDev = property != null ? property.get("running-dev") : "real";
        logger.info("runningDev=" + runningDev);
        // 限定商户的QRCode有效期：2592000秒，30天
        final int seconds = property != null ? NumberUtils.toInt(Objects.toString(property.get("qr-time"), null)) : 7200;
        // 限定商户的QRCode数量，设置为50
        final int qrCnt = property != null ? NumberUtils.toInt(Objects.toString(property.get("store-qr-cnt"), null)) : 50;

        List<String> actionNms = !"real".equals(runningDev) ? Arrays.asList(new String[]{"QR_SCENE", "QR_STR_SCENE"}) : Arrays.asList(new String[]{"QR_LIMIT_SCENE", "QR_LIMIT_STR_SCENE"});
        logger.info("actionNms" + actionNms);
        int nowStoreQrCnt = storeRepository.getQRCodeCountByStoreId(storeId, actionNms);
        if (qrCnt < nowStoreQrCnt + size) {
            logger.error("<<<<<< is more than qr code >>>>>>");
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("exists", "not exists");
        // 查询可以分配給餐桌的二维码集合
        List<QRCode> qrCodeList = storeRepository.getQRCodeListByMap(param);
        try {
            // 存在没有分配给餐桌的二维码
            if (qrCodeList != null && qrCodeList.size() > 0) {
                // 可以分配給餐桌的二维码数量
                int qrcodeSize = qrCodeList.size();

                // 可以分配給餐桌的二维码数量小于没有二维码餐桌的数量，则新建size - qrcodeSize个二维码Qrcode
                if (qrcodeSize < size) {
                    List<QRCode> newQrCodeList = createNewQrcode(size - qrcodeSize, storeId, runningDev, seconds);
                    qrCodeList.addAll(newQrCodeList);
                }
            }

            // 不存在没有分配给餐桌的二维码
            else {
                // 直接给没有二维码的餐桌创建新的二维码QRCode，创建数量size
                qrCodeList = createNewQrcode(size, storeId, runningDev, seconds);
            }
        } catch (Exception e) {
            logger.error("<<<<<< create new qrcode is fail >>>>>>", e);
            throw e;
        }

        // 分配QRCode给餐桌
        for (int i = 0; i < size; i++) {
            collect.get(i).setQrCodeId(qrCodeList.get(i).getQrcodeId());
            collect.get(i).setQrCodeImagePath(qrCodeList.get(i).getQrcodeUrl());
            collect.get(i).setTicket(qrCodeList.get(i).getQrcodeTicket());
        }

        // 更新QRCode信息到餐桌表
        storeRepository.updateStoreTableList(collect);

        return collect;
    }

    /**
     * 创建新的QRCode
     *
     * @param cnt
     * @param storeId
     * @param runningDev
     * @param seconds
     * @return
     */
    private List<QRCode> createNewQrcode(int cnt, long storeId, String runningDev, int seconds) {
        boolean isException = false;

        String token = Objects.toString(redisUtil.get("wx_token"), null);
        logger.info("dxf_token=" + token);
        WechatAPI wechat = WechatAPI.getInstance();

        String actionName = !"real".equals(runningDev) ? "QR_STR_SCENE" : "QR_LIMIT_STR_SCENE";
        logger.info("actionName=" + actionName);
        int expireSeconds = !"real".equals(runningDev) ? seconds : 0;
        String expireDts = !"real".equals(runningDev) ? DateUtil.getFormatStr(DateUtil.plusDateTime(DateUtil.getDateTimeNow(), expireSeconds, ChronoUnit.SECONDS), "yyyy-MM-dd HH:mm:ss") : null;

        List<QRCode> qrcodeList = Lists.newArrayList();

        for (int i = 0; i < cnt; i++) {
            String sceneStr = StringUtils.replace(Objects.toString(UUID.randomUUID()), "-", "");
            QrcodeTicket ticket = null;
            try {
                if (!"real".equals(runningDev)) {
                    ticket = wechat.qrcodeCreateTemp(token, expireSeconds, sceneStr);
                } else {
                    ticket = wechat.qrcodeCreateFinal(token, sceneStr);
                }
            } catch (Exception e) {
                logger.error("<<<<<< qrcodeCreate is fail >>>>>>", e);
                isException = true;
                break;
            }

            if (ticket != null && StringUtils.isNotBlank(ticket.getTicket()) && StringUtils.isNotBlank(ticket.getUrl())) {
                QRCode qrCode = new QRCode.Builder()
                        .qrcodeId(sceneStr)
                        .actionName(actionName)
                        .expireSeconds(expireSeconds)
                        .expireDts(expireDts)
                        .qrcodeTicket(ticket.getTicket())
                        .qrcodeUrl(ticket.getUrl())
                        .storeId(storeId)
                        .sceneStr(sceneStr)
                        .build();
                qrcodeList.add(qrCode);
            } else {
                logger.error("<<<<<< qrcodeCreate is fail >>>>>>");
                isException = true;
                break;
            }
        }

        if (qrcodeList.size() > 0) {
            storeRepository.insertQrcodeList(qrcodeList);
        }

        if (isException) {
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        return qrcodeList;
    }


    @Override
    public byte[] buildQRCode(String contents) {
        return new byte[0];
    }


    /**
     * 生成CA Agent微信二维码
     *
     * @param agentId
     * @return
     */
    @Transactional
    @Override
    public QRCodeAgent matchGenerateCagentWechatQRCode(String agentId) {
        /**
         *   "wxPay-config": {
         *     "running-dev":"real",
         *     "qr-time":2592000,
         *     "store-qr-cnt":50
         *   },
         */
        Map<String, String> property = (Map<String, String>) app.getObject("wxPay-config").get();
        final String runningDev = property != null ? property.get("running-dev") : "real";

        // 限定商户的QRCode有效期：2592000秒，30天
        final int seconds = property != null ? NumberUtils.toInt(Objects.toString(property.get("qr-time"), null)) : 7200;

        // 限定商户的QRCode数量，设置为50
        final int qrCnt = property != null ? NumberUtils.toInt(Objects.toString(property.get("store-qr-cnt"), null)) : 50;
        logger.info("微信二维码基础参数配置：runningDev=" + runningDev + ", 有效期秒数seconds=" + seconds + ", 限制数量qrCnt=" + qrCnt);


        // List<String> actionNms = !"real".equals(runningDev) ? Arrays.asList(new String[]{"QR_SCENE", "QR_STR_SCENE"}) : Arrays.asList(new String[]{"QR_LIMIT_SCENE", "QR_LIMIT_STR_SCENE"});
        // logger.info("微信二维码actionNms=" + actionNms);

        // 查询代理商的QRCode。只有一条永久二维码，生成的时候也只生成一条永久二维码
        QRCodeAgent agentQRCode = agentQuery.getQRCodeByAgentId(agentId);
        log.info("查询到的代理商二维码信息QRCodeAgent>>>>>>" + agentQRCode);

        // 已经存在微信二维码
        if(agentQRCode != null){
            // nothing to do,just go to updating agent info.
        }
        // 不存在微信二维码
        else {
            log.info("调用微信API生成为代理商生成二维码......");
            agentQRCode = createNewAgentQrcode(agentId, runningDev, seconds);
        }

        // 更新QRCode信息到代理商表
        agentRepository.updateQRCodeAgent(agentQRCode);

        return agentQRCode;
    }

    /**
     * 创建新的Agent QRCode
     *
     * @param agentId
     * @param runningDev
     * @param seconds
     * @return
     */
    private QRCodeAgent createNewAgentQrcode(String agentId, String runningDev, int seconds) {
        // 获取微信接口调用token
        String token = Objects.toString(redisUtil.get("wx_token"), null);
        logger.info("创建新的Agent QRCode[createNewAgentQrcode]Weixin access_token=" + token);
        if(StringUtils.isEmpty(token)){
            throw new BizException(ErrorCode.BIZ_EXCEPTION, "Can't get wx_token!");
        }

        WechatAPI wechat = WechatAPI.getInstance();

        String actionName = !"real".equals(runningDev) ? "QR_STR_SCENE" : "QR_LIMIT_STR_SCENE";
        logger.info("创建新的Agent QRCode[createNewAgentQrcode]actionName=" + actionName);
        int expireSeconds = !"real".equals(runningDev) ? seconds : 0;
        String expireDts = !"real".equals(runningDev) ? DateUtil.getFormatStr(DateUtil.plusDateTime(DateUtil.getDateTimeNow(), expireSeconds, ChronoUnit.SECONDS), "yyyy-MM-dd HH:mm:ss") : null;

        // 代理商二维码包含的参数：代理商ID前增加前缀，以便微信开发服务程序识别是扫描代理商二维码事件
        String sceneStr = "agent_" + agentId;

        QrcodeTicket ticket = null;
        try {
            if (!"real".equals(runningDev)) {
                ticket = wechat.qrcodeCreateTemp(token, expireSeconds, sceneStr);
            } else {
                ticket = wechat.qrcodeCreateFinal(token, sceneStr);
            }
        } catch (Exception e) {
            logger.error("<<<<<< 创建新的Agent QRCode[createNewAgentQrcode]QRcodeCreate is fail, because of not complete >>>>>>", e);
        }

        if (ticket != null && StringUtils.isNotBlank(ticket.getTicket()) && StringUtils.isNotBlank(ticket.getUrl())) {
            QRCodeAgent agentQRCode = new QRCodeAgent.Builder()
                    .agentId(agentId)
                    .actionName(actionName)
                    .expireSeconds(expireSeconds)
                    .expireDts(expireDts)
                    .qrcodeTicket(ticket.getTicket())
                    .qrcodeUrl(ticket.getUrl())
                    .sceneStr(sceneStr)
                    .build();
            logger.info("Agent QRCode[createNewAgentQrcode]创建出新的QRCodeAgent信息为>>>" + agentQRCode);
            // 将agentQRCode存入数据库
            agentRepository.insertQRCodeAgent(agentQRCode);
            logger.info("Agent QRCode[createNewAgentQrcode]创建出新的QRCodeAgent信息插入数据库成功！");

            return agentQRCode;
        } else {
            logger.error("<<<<<< 创建新的Agent QRCode[createNewAgentQrcode]QRcodeCreate is fail, because of ticket null >>>>>>");
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }
    }
}