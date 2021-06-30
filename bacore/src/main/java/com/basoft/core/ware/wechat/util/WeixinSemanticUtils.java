package com.basoft.core.ware.wechat.util;

import com.basoft.core.ware.wechat.domain.AppInfo;
//import com.waremec.weixin.service.WeixinService;

import net.sf.json.JSONObject;

/**
 * 语义理解接口工具类
 */
public class WeixinSemanticUtils extends WeixinBaseUtils {
	/**
	 * 语义理解URL
	 */
	private static final String SEMANTIC_SEMPROXY_SEARCH_URL = "https://api.weixin.qq.com/semantic/semproxy/search?access_token=ACCESS_TOKEN";

	/**
	 * 发送语义理解请求
	 * 
	 * @param access_token 根据appid和appsecret获取到的token
	 * @param appInfo 公众号信息
	 * @param query 输入文本串
	 * @param category 需要使用的服务类型，多个用“，”隔开，不能为空
	 * @param city 城市名称
	 * @param uid 用户唯一id（非开发者id），用户区分公众号下的不同用户（建议填入用户openid），如果为空，则无法使用上下文理解功能。
	 *            appid和uid同时存在的情况下，才可以使用上下文理解功能
	 */
	public static void searshSemproxy(String access_token, AppInfo appInfo, String query, String category, String city, String uid) {
		String url = getInterfaceUrl(SEMANTIC_SEMPROXY_SEARCH_URL, access_token);
		JSONObject root = new JSONObject();
		root.put("query", query);
		root.put("category", category);
		root.put("city", city);
		root.put("appid", appInfo.getAppId());
		root.put("uid", uid);
		HttpClientUtils.requestPost(url, root);
		// Response content:{"group":{"id":101,"name":"测试组"}}
	}
}
