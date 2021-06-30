package com.basoft.service.impl.wechat.common.thread;

import com.basoft.core.ware.wechat.util.Constants;
import com.basoft.service.dao.wechat.common.WechatMapper;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class AgentQRCodeEventThread implements Runnable {
    private final transient Log logger = LogFactory.getLog(this.getClass());

    private HttpServletRequest request;

    private WechatService wechatService;

    private WechatMapper wechatMapper;

    private AppInfo appInfo;

    private String keyword;

    Map<String, String> map;

    public AgentQRCodeEventThread(HttpServletRequest request, WechatService wechatService, WechatMapper wechatMapper, AppInfo appInfo, Map<String, String> map, String keyword) {
        this.request = request;
        this.wechatService = wechatService;
        this.wechatMapper = wechatMapper;
        this.appInfo = appInfo;
        this.map = map;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        try {
            logger.info("=================AgentQRCodeEventThread thread start=====================");
            // 事件
            String event = map.get("Event");

            // 事件用户Openid
            String fromUserName = map.get("FromUserName");

            // 扫码事件参数，即代理商二维码的参数
            String eventKey = map.get("EventKey");

            // 拆分出代理商ID
            String agentId = eventKey.substring(6, eventKey.length());

            // Ticket
            String ticket = map.get("Ticket");

            logger.info("Event============" + event);
            logger.info("fromUserName============" + fromUserName);
            logger.info("EventKey=========" + eventKey);
            logger.info("agentId===========" + agentId);
            logger.info("Ticket===========" + ticket);

            String token = wechatService.getAccessToken(appInfo);
            logger.info("Token===========" + token);

            if (Constants.Event.SCAN.equals(event)) {
                // 验证二维码有效性
                if(!wechatService.checkAgentQRCodeUseful(agentId)){
                    // 二维码无效
                    wechatService.sendAgentQRCodeScanMsg(-1, fromUserName, token, null);
                    return;
                }

                // 根据代理商ID和用户openid查询是否存在绑定关系
                // List<Map<String, Object>> bindList = wechatMapper.queryBindInfoByOpenidAndAgent(fromUserName, agentId);

                // 根据用户openid查询是否存在绑定关系
                List<Map<String, Object>> bindList = wechatMapper.queryBindInfoByOpenid(fromUserName);
                // 存在绑定关系
                if (bindList != null && bindList.size() > 0) {
                    // 不需要判断绑定状态，假定一旦绑定，状态永久有效

                    // 发送消息
                    logger.info("BindList[0]===========" + bindList.get(0));
                    Long queryAgtId = (Long) bindList.get(0).get("AGT_ID");
                    logger.info("QueryAgtId===========" + queryAgtId);
                    if (StringUtils.isNotBlank(String.valueOf(queryAgtId)) && String.valueOf(queryAgtId).equals(agentId)) {
                        wechatService.sendAgentQRCodeScanMsg(1, fromUserName, token, bindList.get(0));
                    } else {
                        wechatService.sendAgentQRCodeScanMsg(2, fromUserName, token, null);
                    }
                } else {
                    //20191014-关注过的老用户不绑定代理商-start
                    // 根据openid查询该用户是否关注过公众号（曾经或现在）
                    List<Map<String, Object>> wxUserList = wechatMapper.getWxUserByOpenid(fromUserName);
                    // 关注过发送消息后，结束。。。
                    if (wxUserList != null && wxUserList.size() > 0) {
                        wechatService.sendAgentQRCodeScanMsg(0, fromUserName, token, null);
                        return;
                    }
                    //20191014-关注过的老用户不绑定代理商-end

                    // 绑定关系
                    int i = wechatMapper.bandAgentAndOpenid(fromUserName, agentId);

                    // 发送消息
                    if (i > 0) {
                        wechatService.sendAgentQRCodeScanMsg(3, fromUserName, token, null);
                    } else {
                        wechatService.sendAgentQRCodeScanMsg(4, fromUserName, token, null);
                    }
                }
            }
            logger.info("=================thread end=====================");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("=================thread error=====================", e);
        }
    }
}
