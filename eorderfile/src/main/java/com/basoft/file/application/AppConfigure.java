package com.basoft.file.application;

import java.util.Optional;

public interface AppConfigure {

    String TOKEN_KEY_PREFIX_PROP = "basoft.token.prefix";
    String BASOFT_BROWSER_TOKEN_HEADER_PROP = "basoft.browser.token.header";
    String BASOFT_BROWSER_TOKEN_HEAD_PREFIX_PROP = "basoft.browser.token.headPrefix";
    String BASOFT_TOKEN_EXPIRED_TIME_PROP = "basoft.token.expiredTime";
    String BASOFT_CLIENT_LANGUAGE_PROP = "basoft.client.language.header";
    String BASOFT_REGION_ID_PROP = "region-id";
    String BASOFT_WORKER_ID_PROP = "worker-id";
    String BASOFT_UPLOAD_PATH = "upload_folder";
    String BASOFT_APP_SERVER_PROP="basoft_server";
    String BASOFT_APP_URL_PROP = "basoft_app_url";
    String BASOFT_USER_SESSION_PROP = "basoft_user_session";


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

    Optional<Object> getObject(String key);
    String get(String key);
}
