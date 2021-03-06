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
     * ????????????????????????????????????
     * ???1??????????????????????????????????????????QRCode????????????
     * ???2??????????????????????????????????????????????????????????????????????????????QRCode???
     *
     * @param collect
     * @return
     */
    @Override
    public List<StoreTable> matchGenerateWechatQRCodes(List<StoreTable> collect) {
        long storeId = collect.get(0).getStore().id();

        //??????????????????????????????
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
        // ???????????????QRCode????????????2592000??????30???
        final int seconds = property != null ? NumberUtils.toInt(Objects.toString(property.get("qr-time"), null)) : 7200;
        // ???????????????QRCode??????????????????50
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
        // ?????????????????????????????????????????????
        List<QRCode> qrCodeList = storeRepository.getQRCodeListByMap(param);
        try {
            // ???????????????????????????????????????
            if (qrCodeList != null && qrCodeList.size() > 0) {
                // ???????????????????????????????????????
                int qrcodeSize = qrCodeList.size();

                // ???????????????????????????????????????????????????????????????????????????????????????size - qrcodeSize????????????Qrcode
                if (qrcodeSize < size) {
                    List<QRCode> newQrCodeList = createNewQrcode(size - qrcodeSize, storeId, runningDev, seconds);
                    qrCodeList.addAll(newQrCodeList);
                }
            }

            // ??????????????????????????????????????????
            else {
                // ??????????????????????????????????????????????????????QRCode???????????????size
                qrCodeList = createNewQrcode(size, storeId, runningDev, seconds);
            }
        } catch (Exception e) {
            logger.error("<<<<<< create new qrcode is fail >>>>>>", e);
            throw e;
        }

        // ??????QRCode?????????
        for (int i = 0; i < size; i++) {
            collect.get(i).setQrCodeId(qrCodeList.get(i).getQrcodeId());
            collect.get(i).setQrCodeImagePath(qrCodeList.get(i).getQrcodeUrl());
            collect.get(i).setTicket(qrCodeList.get(i).getQrcodeTicket());
        }

        // ??????QRCode??????????????????
        storeRepository.updateStoreTableList(collect);

        return collect;
    }

    /**
     * ????????????QRCode
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
     * ??????CA Agent???????????????
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

        // ???????????????QRCode????????????2592000??????30???
        final int seconds = property != null ? NumberUtils.toInt(Objects.toString(property.get("qr-time"), null)) : 7200;

        // ???????????????QRCode??????????????????50
        final int qrCnt = property != null ? NumberUtils.toInt(Objects.toString(property.get("store-qr-cnt"), null)) : 50;
        logger.info("????????????????????????????????????runningDev=" + runningDev + ", ???????????????seconds=" + seconds + ", ????????????qrCnt=" + qrCnt);


        // List<String> actionNms = !"real".equals(runningDev) ? Arrays.asList(new String[]{"QR_SCENE", "QR_STR_SCENE"}) : Arrays.asList(new String[]{"QR_LIMIT_SCENE", "QR_LIMIT_STR_SCENE"});
        // logger.info("???????????????actionNms=" + actionNms);

        // ??????????????????QRCode?????????????????????????????????????????????????????????????????????????????????
        QRCodeAgent agentQRCode = agentQuery.getQRCodeByAgentId(agentId);
        log.info("????????????????????????????????????QRCodeAgent>>>>>>" + agentQRCode);

        // ???????????????????????????
        if(agentQRCode != null){
            // nothing to do,just go to updating agent info.
        }
        // ????????????????????????
        else {
            log.info("????????????API?????????????????????????????????......");
            agentQRCode = createNewAgentQrcode(agentId, runningDev, seconds);
        }

        // ??????QRCode?????????????????????
        agentRepository.updateQRCodeAgent(agentQRCode);

        return agentQRCode;
    }

    /**
     * ????????????Agent QRCode
     *
     * @param agentId
     * @param runningDev
     * @param seconds
     * @return
     */
    private QRCodeAgent createNewAgentQrcode(String agentId, String runningDev, int seconds) {
        // ????????????????????????token
        String token = Objects.toString(redisUtil.get("wx_token"), null);
        logger.info("????????????Agent QRCode[createNewAgentQrcode]Weixin access_token=" + token);
        if(StringUtils.isEmpty(token)){
            throw new BizException(ErrorCode.BIZ_EXCEPTION, "Can't get wx_token!");
        }

        WechatAPI wechat = WechatAPI.getInstance();

        String actionName = !"real".equals(runningDev) ? "QR_STR_SCENE" : "QR_LIMIT_STR_SCENE";
        logger.info("????????????Agent QRCode[createNewAgentQrcode]actionName=" + actionName);
        int expireSeconds = !"real".equals(runningDev) ? seconds : 0;
        String expireDts = !"real".equals(runningDev) ? DateUtil.getFormatStr(DateUtil.plusDateTime(DateUtil.getDateTimeNow(), expireSeconds, ChronoUnit.SECONDS), "yyyy-MM-dd HH:mm:ss") : null;

        // ?????????????????????????????????????????????ID???????????????????????????????????????????????????????????????????????????????????????
        String sceneStr = "agent_" + agentId;

        QrcodeTicket ticket = null;
        try {
            if (!"real".equals(runningDev)) {
                ticket = wechat.qrcodeCreateTemp(token, expireSeconds, sceneStr);
            } else {
                ticket = wechat.qrcodeCreateFinal(token, sceneStr);
            }
        } catch (Exception e) {
            logger.error("<<<<<< ????????????Agent QRCode[createNewAgentQrcode]QRcodeCreate is fail, because of not complete >>>>>>", e);
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
            logger.info("Agent QRCode[createNewAgentQrcode]???????????????QRCodeAgent?????????>>>" + agentQRCode);
            // ???agentQRCode???????????????
            agentRepository.insertQRCodeAgent(agentQRCode);
            logger.info("Agent QRCode[createNewAgentQrcode]???????????????QRCodeAgent??????????????????????????????");

            return agentQRCode;
        } else {
            logger.error("<<<<<< ????????????Agent QRCode[createNewAgentQrcode]QRcodeCreate is fail, because of ticket null >>>>>>");
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }
    }
}