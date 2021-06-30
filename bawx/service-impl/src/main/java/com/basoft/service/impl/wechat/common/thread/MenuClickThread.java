package com.basoft.service.impl.wechat.common.thread;

import com.basoft.core.ware.wechat.domain.mass.Media;
import com.basoft.core.ware.wechat.domain.mass.NewsMassDomain4Reply;
import com.basoft.core.ware.wechat.util.WeixinMessageUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class MenuClickThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());
	
	private WechatService wechatService;

	private AppInfo appInfo;

	private Map<String, String> map;

	public MenuClickThread(WechatService wechatService, AppInfo appInfo, Map<String, String> map) {
		this.wechatService = wechatService;
		this.appInfo = appInfo;
		this.map = map;
	}

	@Override
	public void run() {
		try {
			logger.info("=================thread start=====================");
			// TODO Auto-generated method stub
			// 事件类型
			String event = map.get("Event");
			// 发送方帐号
			String fromUserName = map.get("FromUserName");
			String eventKey = map.get("EventKey");// 事件KEY值，qrscene_为前缀，后面为二维码的参数值
			String ticket = map.get("Ticket");// Ticket 
			 
			logger.info("Event============" + event);
			logger.info("EventKey=========" + eventKey);
			logger.info("Ticket===========" + ticket);
			
			String token = wechatService.getAccessToken(appInfo); 
			
			// 拆离
			String[] eventKeyArray = eventKey.split("##");
			String msgType = eventKeyArray[0];
			String mediaId = eventKeyArray[1];
			logger.info("MENU msgType===========" + msgType);
			logger.info("MENU mediaId===========" + mediaId);
			
			// 回复图文消息
			if("1".equals(msgType)){
				NewsMassDomain4Reply news = new NewsMassDomain4Reply();
	            news.setMpnews(new Media(mediaId));
	            news.setTouser(fromUserName);
	            WeixinMessageUtils.sendNewsMsg(token, news);
			}

			// 回复图片消息
			if("2".equals(msgType)){
				WeixinMessageUtils.sendImageMsg(token, fromUserName, mediaId);
			}

			// 文本消息
			if("3".equals(msgType)){
				// 当msgType为3时，mediaId为文本内容。
				WeixinMessageUtils.sendTextMsg(token, fromUserName, mediaId);
			}
			
			logger.info("=================thread end=====================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String s = "3####adfasdf##";
		String[] eventKeyArray = s.split("##");
		System.out.println(eventKeyArray.length);

	}
}