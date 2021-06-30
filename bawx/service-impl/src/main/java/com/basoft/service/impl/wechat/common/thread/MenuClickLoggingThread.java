package com.basoft.service.impl.wechat.common.thread;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfo;

public class MenuClickLoggingThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private AppInfo appInfo;

	private String openid;

	private String eventKey;

	private WechatService wechatService;

	public MenuClickLoggingThread(AppInfo appInfo, String openid, String eventKey, WechatService wechatService) {
		this.appInfo = appInfo;
		this.openid = openid;
		this.eventKey = eventKey;
		this.wechatService = wechatService;
	}

	@Override
	public void run() {
		logger.info("=================MenuClickLoggingThread start=====================");
		try {
			Integer menuId = Integer.valueOf(eventKey);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("SHOP_ID", appInfo.getShopId());
			map.put("MENU_ID", menuId);
			map.put("OPENID", openid);
			wechatService.insertMenuClickLogging(map);
		} catch (Exception e) {
		}
		logger.info("=================MenuClickLoggingThread end=====================");
	}
}
