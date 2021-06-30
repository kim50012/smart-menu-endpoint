package com.basoft.core.ware.wechat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.core.ware.wechat.domain.AppInfo;
import com.basoft.core.ware.wechat.domain.WebAccessToken;
import com.basoft.core.ware.wechat.domain.WeixinReturn;
import com.basoft.core.ware.wechat.exception.Oauth2Exception;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;
import com.google.gson.Gson;

public class Oauth2Utils extends WeixinBaseUtils {
	private static final transient Log logger = LogFactory.getLog(Oauth2Utils.class);

	public static final String PATH = "/w/t.htm";

	public static final String UTF_8 = "utf-8";

	public static final class Params {
		public static final String CODE = "code";

		public static final String STATE = "state";

		public static final String SYSTEM_ID = "k";

		public static final String SHOP_ID = "shopId";

		public static final String FROM_OPENID = "fo";

		public static final String TARGET_URL = "tu";

		public static final String LETTER_ID = "lid";

		public static final String MENU_ID = "mid";

		public static final String EVENT_ID = "eid";

		public static final String OPENID = "openid";

		public static final String SSOID = "ssoid";
	}

	public static final class LinkType {
		public static final String MENU = "m"; // 菜单

		public static final String LETTER = "l"; // 分享1

		public static final String SHARE = "s"; // 分享2

		public static final String ADVICE = "a"; // 通知

		public static final String EVENT = "e"; // Event

		public static final String NORMAL = "n"; // 普通链接

		public static final String SSO = "sso"; // SSO

		public static final String SSO2 = "sso2"; // SSO new
	}

	/**
	 * 通过code换取网页授权access_token URL
	 */
	private static final String SNS_OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	/**
	 * 刷新access_token（如果需要） URL
	 */
	private static final String SNS_OAUTH2_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo) URL
	 */
	private static final String SNS_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo) URL
	 */
	private static final String SNS_AUTH_URL = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";

	public static String getMenuIinkUrl(AppInfo appInfo, Integer menuId) {
		String domain = appInfo.getDomain();
		String url = domain + "/w/r.htm?k=" + appInfo.getSysId() + "&r=" + menuId;
		// logger.info("Original URL = " + url);
		String encodedUrl;
		try {
			encodedUrl = URLEncoder.encode(url, UTF_8);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			throw new WeixinErrorException(e.getMessage());
		}
		
		// logger.info("Encoded URL = " + encodedUrl);
		StringBuffer sb = new StringBuffer();
		sb.append("https://open.weixin.qq.com/connect/oauth2/authorize")
		.append("?appid=").append(appInfo.getAppId())
		.append("&redirect_uri=").append(encodedUrl)
		.append("&response_type=").append("code")
		.append("&scope=").append("snsapi_base")
		.append("&state=").append("menuIink")
		.append("#wechat_redirect");
		// logger.info("Full URL = " + sb.toString());
		// logger.info("Full URL Length = " + sb.toString().length());
		return sb.toString();
	}

	private static String getOauth2SnsapiBaseUrl(AppInfo appInfo, String redirectUrl, String linkType) {
		StringBuffer sb = new StringBuffer();
		sb.append("https://open.weixin.qq.com/connect/oauth2/authorize")
			.append("?appid=").append(appInfo.getAppId())
			.append("&redirect_uri=").append(redirectUrl)
			.append("&response_type=").append("code")
			.append("&scope=").append("snsapi_base")
			.append("&state=").append(linkType)
			.append("#wechat_redirect");
		// logger.info("oauth2Url= " + sb.toString());
		// logger.info("oauth2Url length= " + sb.toString().length());
		return sb.toString();
	}

	public static String getMenuIinkUrlNew(AppInfo appInfo, Long menuId) {
		try {
			String redirectUrl = appInfo.getDomain() + PATH 
						   + "?" + Params.SYSTEM_ID + "=" + appInfo.getSysId() 
						   + "&" + Params.MENU_ID + "=" + menuId;
			// logger.info("redirectUrl=" + redirectUrl);
			String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
			// logger.info("encodedRedirectUrl=" + encodedRedirectUrl);
			return getOauth2SnsapiBaseUrl(appInfo, encodedRedirectUrl, LinkType.MENU);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Oauth2Exception(e.getMessage());
		}
	}
	
	public static String getNormalLinkUrl(AppInfo appInfo, String targetUrl) {
		try {
			if (StringUtils.isNotEmpty(targetUrl)) {
				if (targetUrl.indexOf("shopId=") < 0) {
					if (targetUrl.indexOf("?") < 0) {
						targetUrl += "?shopId=" + appInfo.getShopId();
					} else {
						targetUrl += "&shopId=" + appInfo.getShopId();
					}
				}
				String encodedTargetUrl = URLEncoder.encode(targetUrl, UTF_8);
				// logger.info("encodedTargetUrl=" + encodedTargetUrl);
				String redirectUrl = appInfo.getDomain() + PATH  
								   + "?" + Params.SYSTEM_ID 	+ "=" + appInfo.getSysId() 
								   + "&" + Params.TARGET_URL 	+ "=" + encodedTargetUrl;
				// logger.info("redirectUrl=" + redirectUrl);
				String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
				// logger.info("encodedRedirectUrl=" + encodedRedirectUrl);
				return getOauth2SnsapiBaseUrl(appInfo, encodedRedirectUrl, LinkType.NORMAL);
			} else {
				return "";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Oauth2Exception(e.getMessage());
		}
	}
	
	/**
	 * 获取通知链接
	 * 
	 * @param appInfo
	 * @param targetUrl
	 * @return
	 */
	public static String getAdviceLinkUrl(AppInfo appInfo, String targetUrl) {
		try {
			if (StringUtils.isNotEmpty(targetUrl)) {
				if (targetUrl.indexOf("shopId=") < 0) {
					if (targetUrl.indexOf("?") < 0) {
						targetUrl += "?shopId=" + appInfo.getShopId();
					} else {
						targetUrl += "&shopId=" + appInfo.getShopId();
					}
				}
				String encodedTargetUrl = URLEncoder.encode(targetUrl, UTF_8);
				// logger.info("encodedTargetUrl=" + encodedTargetUrl);
				String redirectUrl = appInfo.getDomain() + PATH  
								   + "?" + Params.SYSTEM_ID 	+ "=" + appInfo.getSysId() 
								   + "&" + Params.TARGET_URL 	+ "=" + encodedTargetUrl;
				// logger.info("redirectUrl=" + redirectUrl);
				String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
				// logger.info("encodedRedirectUrl=" + encodedRedirectUrl);
				return getOauth2SnsapiBaseUrl(appInfo, encodedRedirectUrl, LinkType.ADVICE);
			} else {
				return "";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Oauth2Exception(e.getMessage());
		}
	}

	public static String getLetterLinkUrl(AppInfo appInfo, Integer letterId, String openid) {
		try {
			String redirectUrl = appInfo.getDomain() + PATH  
							   + "?" + Params.SYSTEM_ID + "=" + appInfo.getSysId() 
							   + "&" + Params.LETTER_ID + "=" + letterId
							   + "&" + Params.FROM_OPENID + "=" + openid;
			// logger.info("redirectUrl=" + redirectUrl);
			String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
			// logger.info("encodedRedirectUrl=" + encodedRedirectUrl);
			return getOauth2SnsapiBaseUrl(appInfo, encodedRedirectUrl, LinkType.LETTER);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Oauth2Exception(e.getMessage());
		}
	}
	
	public static String getShareLinkUrl(AppInfo appInfo, String targetUrl, String openid) {
		try {
			if (StringUtils.isNotEmpty(targetUrl)) {
				if (targetUrl.indexOf("shopId=") < 0) {
					if (targetUrl.indexOf("?") < 0) {
						targetUrl += "?shopId=" + appInfo.getShopId();
					} else {
						targetUrl += "&shopId=" + appInfo.getShopId();
					}
				}
				String encodedTargetUrl = URLEncoder.encode(targetUrl, UTF_8);
				// logger.info("encodedTargetUrl=" + encodedTargetUrl);
				
				String redirectUrl = appInfo.getDomain() + PATH  
								   + "?" + Params.SYSTEM_ID + "=" + appInfo.getSysId() 
								   + "&" + Params.FROM_OPENID + "=" + openid
								   + "&" + Params.TARGET_URL + "=" + encodedTargetUrl;
				// logger.info("redirectUrl=" + redirectUrl);
				String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
				// logger.info("encodedRedirectUrl=" + encodedRedirectUrl);
				return getOauth2SnsapiBaseUrl(appInfo, encodedRedirectUrl, LinkType.SHARE);
			} else {
				throw new Oauth2Exception("getShareLinkUrl targetUrl is null");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Oauth2Exception(e.getMessage());
		}
	}
	
	/**
	 * SSO单点登录链接 旧 - 不通过底部菜单点击（通过页面上链接跳转）
	 * 
	 * @param appInfo
	 * @param menuId
	 * @return
	 */
	public static String getSSOIinkUrl(AppInfo appInfo, Integer menuId, Integer ssoId) {
		try {
			String redirectUrl = appInfo.getDomain() + PATH 
						   + "?" + Params.SYSTEM_ID + "=" + appInfo.getSysId() 
						   + "&" + Params.MENU_ID + "=" + menuId
						   + "&" + Params.SSOID + "=" + ssoId;
			// logger.info("redirectUrl=" + redirectUrl);
			String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
			// logger.info("encodedRedirectUrl=" + encodedRedirectUrl);
			return getOauth2SnsapiBaseUrl(appInfo, encodedRedirectUrl, LinkType.SSO);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Oauth2Exception(e.getMessage());
		}
	}
	
	
	/**
	 * SSO单点登录链接 新- 不通过底部菜单点击（通过页面上链接跳转） - 新的公众号 如Hamll
	 * 
	 * @param appInfo
	 * @param targetUrl
	 * @return
	 */
	public static String getSSO2IinkUrl(AppInfo appInfo, String targetUrl) {
		try {
			if (StringUtils.isNotEmpty(targetUrl)) {
				String encodedTargetUrl = URLEncoder.encode(targetUrl, UTF_8);
				// logger.info("encodedTargetUrl=" + encodedTargetUrl);
				String redirectUrl = appInfo.getDomain() + PATH + "?" + Params.SYSTEM_ID + "=" + appInfo.getSysId()
						+ "&" + Params.TARGET_URL + "=" + encodedTargetUrl;
				// logger.info("redirectUrl=" + redirectUrl);
				String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
				// logger.info("encodedRedirectUrl=" + encodedRedirectUrl);
				return getOauth2SnsapiBaseUrl(appInfo, encodedRedirectUrl, LinkType.SSO2);
			} else {
				return "";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Oauth2Exception(e.getMessage());
		}
	}
	
	public static String getEventShareLinkUrl(AppInfo appInfo, String targetUrl, String openid, Integer eventId,
			Integer jlcount) {
		try {
			// 判断链接后加的信息数量参数
			if (StringUtils.isNotEmpty(targetUrl)) {
				if (targetUrl.indexOf("shopId=") < 0) {
					if (targetUrl.indexOf("?") < 0) {
						targetUrl += "?shopId=" + appInfo.getShopId();
					} else {
						targetUrl += "&shopId=" + appInfo.getShopId();
					}
				}
				if (jlcount.intValue() >= 0) {
					if (targetUrl.indexOf("?") < 0) {
						targetUrl += "?jlcount=" + jlcount;
					} else {
						targetUrl += "&jlcount=" + jlcount;
					}
				} else {
					if (targetUrl.indexOf("?") < 0) {
						targetUrl += "?jlcount=0";
					} else {
						targetUrl += "&jlcount=0";
					}
				}
				String encodedTargetUrl = URLEncoder.encode(targetUrl, UTF_8);
				// logger.info("encodedTargetUrl=" + encodedTargetUrl);
				
				String redirectUrl = appInfo.getDomain() + PATH  
								   + "?" + Params.SYSTEM_ID + "=" + appInfo.getSysId() 
								   + "&" + Params.EVENT_ID + "=" + eventId
								   + "&" + Params.FROM_OPENID + "=" + openid
								   + "&" + Params.TARGET_URL + "=" + encodedTargetUrl;
				// logger.info("redirectUrl=" + redirectUrl);
				String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
				// logger.info("encodedRedirectUrl=" + encodedRedirectUrl);
				return getOauth2SnsapiBaseUrl(appInfo, encodedRedirectUrl, LinkType.EVENT);
			} else {
				throw new Oauth2Exception("getShareLinkUrl targetUrl is null");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Oauth2Exception(e.getMessage());
		}
	}

	public static String getEventShareLinkUrl(AppInfo appInfo, String targetUrl, String openid, Integer eventId) {
		try {
			if (StringUtils.isNotEmpty(targetUrl)) {
				if (targetUrl.indexOf("shopId=") < 0) {
					if (targetUrl.indexOf("?") < 0) {
						targetUrl += "?shopId=" + appInfo.getShopId();
					} else {
						targetUrl += "&shopId=" + appInfo.getShopId();
					}
				}
				System.out.println(targetUrl);
				String encodedTargetUrl = URLEncoder.encode(targetUrl, UTF_8);
				// logger.info("encodedTargetUrl=" + encodedTargetUrl);
				
				String redirectUrl = appInfo.getDomain() + PATH  
								   + "?" + Params.SYSTEM_ID + "=" + appInfo.getSysId() 
								   + "&" + Params.EVENT_ID + "=" + eventId
								   + "&" + Params.FROM_OPENID + "=" + openid
								   + "&" + Params.TARGET_URL + "=" + encodedTargetUrl;
				// logger.info("redirectUrl=" + redirectUrl);
				String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
				// logger.info("encodedRedirectUrl=" + encodedRedirectUrl);
				return getOauth2SnsapiBaseUrl(appInfo, encodedRedirectUrl, LinkType.EVENT);
			} else {
				throw new Oauth2Exception("getShareLinkUrl targetUrl is null");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Oauth2Exception(e.getMessage());
		}
	}

	public static String getShareLinkUrl(AppInfo appInfo, String shareUrl) throws Exception {
		String domain = appInfo.getDomain();
		String encodedShareUrl = URLEncoder.encode(shareUrl, UTF_8);
		String url = domain + "/w/s.htm?k=" + appInfo.getSysId() + "&u=" + encodedShareUrl;
		// logger.info("shareUrl =" + url);
		// logger.info("Original URL =" + url);
		String encodedUrl = URLEncoder.encode(url, UTF_8);
		// logger.info("Encoded URL =" + encodedUrl);
		StringBuffer sb = new StringBuffer();
		sb.append("https://open.weixin.qq.com/connect/oauth2/authorize").append("?appid=").append(appInfo.getAppId())
				.append("&redirect_uri=").append(encodedUrl).append("&response_type=").append("code").append("&scope=")
				.append("snsapi_base").append("&state=").append("shareLink").append("#wechat_redirect");
		// logger.info("Full URL = " + sb.toString());
		// logger.info("Full URL Length = " + sb.toString().length());
		return sb.toString();
	}

	public static String getSnsapiBaseUrl(AppInfo appInfo) throws Exception {
		String domain = appInfo.getDomain();
		// String url =
		// "http://192.168.1.103/w/r.htm?k=8D6ED58C805242C8BDB129616163CB04&c=1&t=001";
		// String url = "http://" + domain + "/w/r.htm?k=" + appInfo.getSYS_ID()
		// + "&c=1&t=001";
		String url = domain + "/w/r2.htm?k=" + appInfo.getSysId() + "&c=1&t=001";

		// logger.info("Original URL = " + url);
		String encodedUrl = URLEncoder.encode(url, UTF_8);
		// logger.info("Encoded URL = " + encodedUrl);

		StringBuffer sb = new StringBuffer();
		sb.append("https://open.weixin.qq.com/connect/oauth2/authorize").append("?appid=").append(appInfo.getAppId())
				.append("&redirect_uri=").append(encodedUrl).append("&response_type=").append("code").append("&scope=")
				.append("snsapi_base").append("&state=").append("0").append("#wechat_redirect");
		// logger.info("Full URL = " + sb.toString());
		// logger.info("Full URL Length = " + sb.toString().length());
		return sb.toString();
	}

	public static String getSnsapiUserinfoUrl(AppInfo appInfo, String redirectUrl, String linkType) throws Exception {
		String domain = appInfo.getDomain();
		String url = domain + "/w/userInfo.htm?k=" + appInfo.getSysId();
		// logger.info("Original URL = " + url);
		String encodedUrl = URLEncoder.encode(url, UTF_8);
		// logger.info("Encoded URL = " + encodedUrl);
		redirectUrl = encodedUrl;
		StringBuffer sb = new StringBuffer();
		
		sb.append("https://open.weixin.qq.com/connect/oauth2/authorize")
			.append("?appid=").append(appInfo.getAppId())
			.append("&redirect_uri=").append(redirectUrl)
			.append("&response_type=").append("code")
			.append("&scope=").append("snsapi_userinfo")
			.append("&state=").append(linkType)
			.append("#wechat_redirect");
		// logger.info("Full URL = " + sb.toString());
		// logger.info("Full URL Length = " + sb.toString().length());
		return sb.toString();
	}

	/**
	 * 通过code换取网页授权access_token
	 * 
	 * @param appInfo appid 公众号的唯一标识
	 * @param secret 公众号的appsecret
	 * @param code 填写第一步获取的code参数
	 * @return
	 */
	public static WebAccessToken getWebAccessToken(AppInfo appInfo, String code) {
		String url = SNS_OAUTH2_ACCESS_TOKEN_URL.replaceAll("APPID", appInfo.getAppId())
				.replaceAll("SECRET", appInfo.getAppSecret()).replaceAll("CODE", code);
		String result = HttpClientUtils.requestGet(url);
		WebAccessToken returns = new Gson().fromJson(result, WebAccessToken.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 刷新access_token（如果需要）
	 * 
	 * @param appInfo
	 * @param refresh_token 填写通过access_token获取到的refresh_token参数
	 * @return
	 */
	public static WebAccessToken refreshToken(AppInfo appInfo, String refresh_token) {
		String url = SNS_OAUTH2_REFRESH_TOKEN_URL.replaceAll("APPID", appInfo.getAppId()).replaceAll("REFRESH_TOKEN",
				refresh_token);
		String result = HttpClientUtils.requestGet(url);
		WebAccessToken returns = new Gson().fromJson(result, WebAccessToken.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo)
	 * 
	 * @param access_token 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 * @param openid 用户的唯一标识
	 * @return
	 */
	public static String getSnsUserinfo(String access_token, String openid) {
		String url = getInterfaceUrl(SNS_USERINFO_URL, access_token).replaceAll("OPENID", openid);
		return HttpClientUtils.requestGet(url);
	}

	/**
	 * 检验授权凭证（access_token）是否有效
	 * 
	 * @param access_token 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 * @param openid 用户的唯一标识
	 * @return
	 */
	public static void checkAccessTokenAuth(String access_token, String openid) {
		String url = getInterfaceUrl(SNS_AUTH_URL, access_token).replaceAll("OPENID", openid);
		String result = HttpClientUtils.requestGet(url);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}
}
