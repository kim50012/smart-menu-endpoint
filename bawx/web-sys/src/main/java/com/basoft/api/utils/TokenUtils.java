package com.basoft.api.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;

/**
 * TokenUtils
 * 
 * @author basoft
 */
public class TokenUtils {
	private static final transient Log logger = LogFactory.getLog(TokenUtils.class);
	static {
		logger.info("===============TokenUtils======================");
	}

	/**
	 * 获取token
	 * 
	 * @param sysId
	 * @return
	 */
	public static String getToken(String sysId) {
		WechatService wechatService = SpringUtils.getBean(WechatService.class);
		AppInfoWithBLOBs appInfo = wechatService.selectAppInfoByKey(sysId);
		String token = wechatService.getAccessToken(appInfo);
		logger.info("token==" + token);
		return token;
	}

	/**
	 * 获取token
	 * 
	 * @param shopId
	 * @return
	 */
	public static String getToken(Long shopId) {
		WechatService wechatService = SpringUtils.getBean(WechatService.class);
		AppInfoWithBLOBs appInfo = wechatService.selectAppInfoByShopId(shopId);
		String token = wechatService.getAccessToken(appInfo);
		logger.info("token==" + token);
		return token;
	}

	/**
	 * 获取token
	 * 
	 * @param appInfo
	 * @return
	 */
	public static String getToken(AppInfoWithBLOBs appInfo) {
		WechatService wechatService = SpringUtils.getBean(WechatService.class);
		String token = wechatService.getAccessToken(appInfo);
		logger.info("token==" + token);
		return token;
	}
}