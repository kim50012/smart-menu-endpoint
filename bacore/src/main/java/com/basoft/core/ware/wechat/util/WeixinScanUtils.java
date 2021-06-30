package com.basoft.core.ware.wechat.util;

public class WeixinScanUtils extends WeixinBaseUtils {
	/******************** 接口地址定义 *********************/
	/**
	 * 获取商户信息接口
	 */
	private static final String SCAN_MERCHANTINFO_GET_URL = "https://api.weixin.qq.com/scan/merchantinfo/get?access_token=ACCESS_TOKEN";

	/**
	 * <h1>获取商户信息</h1>
	 * 
	 * 使用该接口，商户可获取账号下的类目与号段等信息。
	 * 
	 * @param access_token
	 * @return
	 */
	public static String getScanMerchantinfo(String access_token) {
		String url = getInterfaceUrl(SCAN_MERCHANTINFO_GET_URL, access_token);
		return HttpClientUtils.requestGet(url);
	}
}
