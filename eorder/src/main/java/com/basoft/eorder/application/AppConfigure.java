package com.basoft.eorder.application;

import java.util.Optional;

/**
 * 常量定义
 * 
 * @author BA
 */
public interface AppConfigure {
	String TOKEN_KEY_PREFIX_PROP = "basoft.token.prefix";
	String BASOFT_BROWSER_TOKEN_HEADER_PROP = "basoft.browser.token.header";
	String BASOFT_BROWSER_TOKEN_HEAD_PREFIX_PROP = "basoft.browser.token.headPrefix";
	String BASOFT_TOKEN_EXPIRED_TIME_PROP = "basoft.token.expiredTime";
	String BASOFT_CLIENT_LANGUAGE_PROP = "basoft.client.language.header";
	String BASOFT_REGION_ID_PROP = "region-id";
	String BASOFT_WORKER_ID_PROP = "worker-id";
	String BASOFT_UPLOAD_PATH = "upload_folder";
	String BASOFT_APP_SERVER_PROP = "basoft_server";
	String BASOFT_APP_URL_PROP = "basoft_app_url";
	String BASOFT_USER_SESSION_PROP = "basoft_user_session";

	String BASOFT_WX_SESSION_PROP = "basoft_wechat_session";

	String BASOFT_DOMAIN = "/";

	String BASOFT_COOKIE_TIME = "_tm";
	String BASOFT_COOKIE_TOKEN = "_token";

	String BASOFT_H5_SESSION = "wechat-h5-auth";

	String BASOFT_WECHAT_INIT_ROOT = "wechatRootUrl";
	String BASOFT_WECHAT_MENU_REDIRECT_ROOT = "menuRedirectRoot";
	String BASOFT_WECHAT_NOTIFY_URL = "wechatNotifyUrl";
	String BASOFT_WECHAT_CERT_PATH = "wechatCertPath";

	String BASOFT_WX_TEMPLATE_CONFIG = "template-msg-config";
	
	int TIME_HALF_HOUR = 60 * 30*12;
	int TIME_ONE_HOUR = 60 * 60;
	int TIME_ONE_DAY = 60 * 60 * 24;

/*
    public static final String BACK_USER_SESSION = "backUserSession";
    // 后台Shop ID
    public static final String BACK_SHOP_ID_SESSION = "backShopIdSession";
    public static final String SESSION_OPENID = "sessoinOpenid";
    public static final String SESSION_OPENID_PAIR = "sessoinOpenidPair";
    //店铺信息
    public static final String BACK_SHOPINFO_SESSION = "backShopInfoSession";
    public static final String SESSION_MEMEBER = "sessoinMember";
    public static final String SESSION_SKIN = "sessionSkin";
    public static final String HASD_USER_ID_SESSION = "hasdUserIdSession";
*/

	// 微信公众号APP INFO
	String WECHAT_APP_ID = "wechatAppId";
	String WECHAT_APP_SECRET = "wechatAppSecret";

	// 微信端域名
	String WECHAT_DOMAIN = "wechatDomain";

	Optional<Object> getObject(String key);

	String get(String key);
}