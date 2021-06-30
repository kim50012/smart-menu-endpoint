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

public class AgentQRCodeSubscribeEventThread implements Runnable {
    private final transient Log logger = LogFactory.getLog(this.getClass());

    private HttpServletRequest request;

    private WechatService wechatService;

    private WechatMapper wechatMapper;

    private AppInfo appInfo;

    private String keyword;

    private Map<String, String> map;

    private boolean isOldUser;

    public AgentQRCodeSubscribeEventThread(HttpServletRequest request,
              WechatService wechatService, WechatMapper wechatMapper,
              AppInfo appInfo, Map<String, String> map, String keyword,
              boolean isOldUser) {
        this.request = request;
        this.wechatService = wechatService;
        this.wechatMapper = wechatMapper;
        this.appInfo = appInfo;
        this.map = map;
        this.keyword = keyword;
        this.isOldUser = isOldUser;
    }

    @Override
    public void run() {
        try {
            logger.info("=================AgentQRCodeSubscribeEventThread thread start=====================");
            // 事件
            String event = map.get("Event");

            // 事件用户Openid
            String fromUserName = map.get("FromUserName");

            // 扫码事件参数，即代理商二维码的参数
            String eventKey = map.get("EventKey");

            // 拆分出代理商ID
            String agentId = eventKey.substring(14, eventKey.length());

            // Ticket
            String ticket = map.get("Ticket");

            logger.info("Event============" + event);
            logger.info("fromUserName============" + fromUserName);
            logger.info("EventKey=========" + eventKey);
            logger.info("agentId===========" + agentId);
            logger.info("Ticket===========" + ticket);

            String token = wechatService.getAccessToken(appInfo);
            logger.info("Token===========" + token);

            if (Constants.Event.SUBSCRIBE.equals(event)) {
                // 验证二维码有效性
                if(!wechatService.checkAgentQRCodeUseful(agentId)){
                    // 二维码无效
                    wechatService.sendAgentQRCodeScanMsg(-1, fromUserName, token, null);
                    return;
                }

                // 老用户，不允许绑定CA代理商，发送提示消息
                if(isOldUser){
                    //您已关注公众号
                    wechatService.sendAgentQRCodeScanMsg(0, fromUserName, token, null);
                    return;
                }
                // 新关注用户
                else {
                    //判断该微信用户是否绑定过ca代理商
                    List<Map<String, Object>> bindListAtSubscribe = wechatMapper.queryBindInfoByOpenid(fromUserName);
                    // 绑定过CA代理商，发送提示消息-！！！说明，业务上不会出现这种情况呢，这儿还是判断了下吧
                    if (bindListAtSubscribe != null && bindListAtSubscribe.size() > 0) {
                        // 不需要判断绑定状态，假定一旦绑定，状态永久有效

                        // 发送消息
                        logger.info("bindListAtSubscribe[0]===========" + bindListAtSubscribe.get(0));
                        Long queryAgentId = (Long) bindListAtSubscribe.get(0).get("AGT_ID");
                        logger.info("queryAgentId===========" + queryAgentId);
                        // 数据库中的绑定代理商ID非空且等于二维码中的代理商ID
                        if (StringUtils.isNotBlank(String.valueOf(queryAgentId)) && String.valueOf(queryAgentId).equals(agentId)) {
                            wechatService.sendAgentQRCodeScanMsg(1, fromUserName, token, bindListAtSubscribe.get(0));
                        } else {
                            // 您已扫描过其他同类型二维码
                            wechatService.sendAgentQRCodeScanMsg(2, fromUserName, token, null);
                        }
                    }
                    // 未绑定过CA代理商，进行绑定
                    else {
                        logger.info("Insert WxUser&CAAgent Mapping......");
                        // 插入绑定关系
                        int i = wechatMapper.bandAgentAndOpenid(fromUserName, agentId);

                        // 发送消息
                        if (i > 0) {
                            logger.info("Insert WxUser&CAAgent Success......");
                            wechatService.sendAgentQRCodeScanMsg(3, fromUserName, token, null);
                        } else {
                            wechatService.sendAgentQRCodeScanMsg(4, fromUserName, token, null);
                        }
                    }
                }
            }
            logger.info("=================AgentQRCodeSubscribeEventThread end=====================");
        } catch (Exception e) {
            logger.info("=================AgentQRCodeSubscribeEventThread error=====================", e);
        }
    }
}
