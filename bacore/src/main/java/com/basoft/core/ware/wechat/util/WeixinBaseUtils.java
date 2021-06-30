package com.basoft.core.ware.wechat.util;

public abstract class WeixinBaseUtils {
	protected static String getInterfaceUrl(String url, String access_token) {
		return url.replaceAll("ACCESS_TOKEN", access_token);
	}
}