package com.basoft.core.ware.wechat.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.basoft.core.ware.wechat.domain.WeixinReturn;
import com.basoft.core.ware.wechat.domain.customservice.GetOnlineKflistReturn;
import com.basoft.core.ware.wechat.domain.customservice.GetRecordReturn;
import com.basoft.core.ware.wechat.domain.customservice.GetSessionListReturn;
import com.basoft.core.ware.wechat.domain.customservice.GetWaitCaseReturn;
import com.basoft.core.ware.wechat.domain.customservice.GetkflistReturn;
import com.basoft.core.ware.wechat.domain.mass.NewsMassDomain4Reply;
import com.basoft.core.ware.wechat.domain.msg.Article;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
 * 消息(客服,多客服)接口工具类
 */
public class WeixinMessageUtils extends WeixinBaseUtils {
	/******************** 接口地址定义 *********************/
	/**
	 * 发送消息 - 客服接口 - 发消息
	 */
	private static final String MESSAGE_CUSTOM_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	/**
	 * 客服(账号)管理-获取客服基本信息
	 */
	private static final String CUSTOMSERVICE_GETKFLIST_URL = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=ACCESS_TOKEN";

	/**
	 * 客服(账号)管理-获取在线客服接待信息
	 */
	private static final String CUSTOMSERVICE_GETONLINEKFLIST_URL = "https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token=ACCESS_TOKEN";

	/**
	 * 客服(账号)管理-添加客服账号
	 */
	private static final String CUSTOMSERVICE_KFACCOUNT_ADD_URL = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN";

	/**
	 * 客服(账号)管理-设置客服信息
	 */
	private static final String CUSTOMSERVICE_KFACCOUNT_UPDATE_URL = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=ACCESS_TOKEN";

	/**
	 * 客服(账号)管理-删除客服账号
	 */
	private static final String CUSTOMSERVICE_KFACCOUNT_DEL_URL = "https://api.weixin.qq.com/customservice/kfaccount/del?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT";

	/**
	 * 多客服会话控制 - 创建会话
	 */
	private static final String CUSTOMSERVICE_KFSESSION_CREATE_URL = "https://api.weixin.qq.com/customservice/kfsession/create?access_token=ACCESS_TOKEN";

	/**
	 * 多客服会话控制 - 关闭会话
	 */
	private static final String CUSTOMSERVICE_KFSESSION_CLOSE_URL = "https://api.weixin.qq.com/customservice/kfsession/close?access_token=ACCESS_TOKEN";

	/**
	 * 多客服会话控制 - 获取客户的会话状态
	 */
	private static final String CUSTOMSERVICE_KFSESSION_GETSESSION_URL = "https://api.weixin.qq.com/customservice/kfsession/getsession?access_token=ACCESS_TOKEN&openid=OPENID";

	/**
	 * 多客服会话控制 - 获取客服的会话列表
	 */
	private static final String CUSTOMSERVICE_KFSESSION_GETSESSIONLIST_URL = "https://api.weixin.qq.com/customservice/kfsession/getsessionlist?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT";

	/**
	 * 多客服会话控制 - 获取客服的会话列表
	 */
	private static final String CUSTOMSERVICE_KFSESSION_GETWAITCASE_URL = "https://api.weixin.qq.com/customservice/kfsession/getwaitcase?access_token=ACCESS_TOKEN";

	/**
	 * 获取客服聊天记录
	 */
	private static final String CUSTOMSERVICE_MSGRECORD_GETRECORD_URL = "https://api.weixin.qq.com/customservice/msgrecord/getrecord?access_token=ACCESS_TOKEN";
	
	/**
	 * <h1>获取自动回复规则</h1>
	 * 
	 * 开发者可以通过该接口，获取公众号当前使用的自动回复规则，包括关注后自动回复、消息自动回复（60分钟内触发一次）、关键词自动回复。
	 * 
	 * @param access_token
	 * @return
	 */
	public static String getCurrentAutoreplyInfo(String access_token) {
		// http请求方式：GET
		// https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN
		String url = "https://api.weixin.qq.com/cgi-bin/get_current_autoreply_info?access_token=" + access_token;
		return HttpClientUtils.requestGet(url);
	}

	/**
	 * 客服接口 - 发送文本消息
	 * 
	 * @param access_token 调用接口凭证
	 * @param touser 普通用户openid
	 * @param content 文本消息内容
	 */
	public static void sendTextMsg(String access_token, String touser, String content) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		// String postData = "{\"touser\":\"" + touser + "\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + content + "\"}}";
		// JSONObject jsonObject = JSONObject.fromObject(postData);
		JSONObject jsonObject = new JSONObject();
		JSONObject text = new JSONObject();
		text.put("content", content);
		jsonObject.put("touser", touser);
		jsonObject.put("msgtype", "text");
		jsonObject.put("text", text);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送文本消息(某个客服帐号来发消息)
	 * 
	 * @param access_token 调用接口凭证
	 * @param touser 普通用户openid
	 * @param content 文本消息内容
	 * @param kf_account 完整客服账号，格式为：账号前缀@公众号微信号
	 */
	public static void sendTextMsg(String access_token, String touser, String content, String kf_account) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		String postData = "{\"touser\":\"" + touser + "\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + content + "\"} ,\"customservice\":{\"kf_account\": \"" + kf_account
				+ "\"}}";
		JSONObject jsonObject = JSONObject.fromObject(postData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送图片消息
	 * 
	 * @param access_token 调用接口凭证
	 * @param touser 普通用户openid
	 * @param media_id 发送的图片/语音/视频的媒体ID
	 */
	public static void sendImageMsg(String access_token, String touser, String media_id) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		String postData = "{\"touser\":\"" + touser + "\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"" + media_id + "\"}}";
		JSONObject jsonObject = JSONObject.fromObject(postData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送图片消息(某个客服帐号来发消息)
	 * 
	 * @param access_token 调用接口凭证
	 * @param touser 普通用户openid
	 * @param media_id 发送的图片/语音/视频的媒体ID
	 * @param kf_account 完整客服账号，格式为：账号前缀@公众号微信号
	 */
	public static void sendImageMsg(String access_token, String touser, String media_id, String kf_account) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		String postData = "{\"touser\":\"" + touser + "\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"" + media_id + "\"} ,\"customservice\":{\"kf_account\": \"" + kf_account
				+ "\"}}";
		JSONObject jsonObject = JSONObject.fromObject(postData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送语音消息
	 * 
	 * @param access_token 调用接口凭证
	 * @param touser 普通用户openid
	 * @param media_id 发送的图片/语音/视频的媒体ID
	 */
	public static void sendVoiceMsg(String access_token, String touser, String media_id) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		String postData = "{\"touser\":\"" + touser + "\",\"msgtype\":\"voice\",\"voice\":{\"media_id\":\"" + media_id + "\"}}";
		JSONObject jsonObject = JSONObject.fromObject(postData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送语音消息(某个客服帐号来发消息)
	 * 
	 * @param access_token 调用接口凭证
	 * @param touser 普通用户openid
	 * @param media_id 发送的图片/语音/视频的媒体ID
	 * @param kf_account 完整客服账号，格式为：账号前缀@公众号微信号
	 */
	public static void sendVoiceMsg(String access_token, String touser, String media_id, String kf_account) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		String postData = "{\"touser\":\"" + touser + "\",\"msgtype\":\"voice\",\"voice\":{\"media_id\":\"" + media_id + "\"} ,\"customservice\":{\"kf_account\": \"" + kf_account
				+ "\"}}";
		JSONObject jsonObject = JSONObject.fromObject(postData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送音乐消息
	 * 
	 * @param access_token 是 调用接口凭证
	 * @param touser 是 普通用户openid
	 * @param title 否 音乐消息的标题
	 * @param description 否 音乐消息的描述
	 * @param musicurl 是 音乐链接
	 * @param hqmusicurl 是 高品质音乐链接，wifi环境优先使用该链接播放音乐
	 * @param thumb_media_id 是 缩略图的媒体ID
	 */
	public static void sendMusicMsg(String access_token, String touser, String title, String description, String musicurl, String hqmusicurl, String thumb_media_id) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		JSONObject music = new JSONObject();
		if (StringUtils.isNotBlank(title)) {
			music.put("title", title);
		}
		if (StringUtils.isNotBlank(description)) {
			music.put("description", description);
		}
		music.put("musicurl", musicurl);
		music.put("hqmusicurl", hqmusicurl);
		music.put("thumb_media_id", thumb_media_id);
		jsonObject.put("touser", touser);
		jsonObject.put("msgtype", "music");
		jsonObject.put("music", music);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送音乐消息(某个客服帐号来发消息)
	 * 
	 * @param access_token 是 调用接口凭证
	 * @param touser 是 普通用户openid
	 * @param title 否 音乐消息的标题
	 * @param description 否 音乐消息的描述
	 * @param musicurl 是 音乐链接
	 * @param hqmusicurl 是 高品质音乐链接，wifi环境优先使用该链接播放音乐
	 * @param thumb_media_id 是 缩略图的媒体ID
	 * @param kf_account 完整客服账号，格式为：账号前缀@公众号微信号
	 */
	public static void sendMusicMsg(String access_token, String touser, String title, String description, String musicurl, String hqmusicurl, String thumb_media_id,
			String kf_account) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		JSONObject music = new JSONObject();
		if (StringUtils.isNotBlank(title)) {
			music.put("title", title);
		}
		if (StringUtils.isNotBlank(description)) {
			music.put("description", description);
		}
		music.put("musicurl", musicurl);
		music.put("hqmusicurl", hqmusicurl);
		music.put("thumb_media_id", thumb_media_id);
		JSONObject customservice = new JSONObject();
		customservice.put("kf_account", kf_account);
		jsonObject.put("touser", touser);
		jsonObject.put("msgtype", "music");
		jsonObject.put("music", music);
		jsonObject.put("customservice", customservice);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送视频消息
	 * 
	 * @param access_token 是 调用接口凭证
	 * @param touser 是 普通用户openid
	 * @param media_id 是 视频的媒体ID
	 * @param thumb_media_id 是 缩略图的媒体ID
	 * @param title 否 音乐消息的标题
	 * @param description 否 音乐消息的描述
	 */
	public static void sendVideoMsg(String access_token, String touser, String media_id, String thumb_media_id, String title, String description) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		JSONObject video = new JSONObject();
		video.put("media_id", media_id);
		video.put("thumb_media_id", thumb_media_id);
		if (StringUtils.isNotBlank(title)) {
			video.put("title", title);
		}
		if (StringUtils.isNotBlank(description)) {
			video.put("description", description);
		}
		jsonObject.put("touser", touser);
		jsonObject.put("msgtype", "video");
		jsonObject.put("video", video);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送视频消息(某个客服帐号来发消息)
	 * 
	 * @param access_token 是 调用接口凭证
	 * @param touser 是 普通用户openid
	 * @param media_id 是 视频的媒体ID
	 * @param thumb_media_id 是 缩略图的媒体ID
	 * @param title 否 音乐消息的标题
	 * @param description 否 音乐消息的描述
	 * @param kf_account 完整客服账号，格式为：账号前缀@公众号微信号
	 */
	public static void sendVideoMsg(String access_token, String touser, String media_id, String thumb_media_id, String title, String description, String kf_account) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		JSONObject video = new JSONObject();
		video.put("media_id", media_id);
		video.put("thumb_media_id", thumb_media_id);
		if (StringUtils.isNotBlank(title)) {
			video.put("title", title);
		}
		if (StringUtils.isNotBlank(description)) {
			video.put("description", description);
		}
		JSONObject customservice = new JSONObject();
		customservice.put("kf_account", kf_account);
		jsonObject.put("touser", touser);
		jsonObject.put("msgtype", "video");
		jsonObject.put("video", video);
		jsonObject.put("customservice", customservice);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送图文消息 图文消息条数限制在10条以内，注意，如果图文数超过10，则将会无响应。
	 * 
	 * @param access_token
	 * @param touser
	 * @param articles
	 * @return
	 */
	public static void sendNewsMsg(String access_token, String touser, List<Article> articles) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		Map<String, Object> articlesmap = new HashMap<String, Object>();
		articlesmap.put("articles", articles);
		jsonObject.put("touser", touser);
		jsonObject.put("msgtype", "news");
		jsonObject.put("news", articlesmap);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送图文消息(某个客服帐号来发消息) 图文消息条数限制在10条以内，注意，如果图文数超过10，则将会无响应。
	 * 
	 * @param access_token
	 * @param touser
	 * @param articles
	 * @param kf_account 完整客服账号，格式为：账号前缀@公众号微信号
	 */
	public static void sendNewsMsg(String access_token, String touser, List<Article> articles, String kf_account) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		Map<String, Object> articlesmap = new HashMap<String, Object>();
		articlesmap.put("articles", articles);
		JSONObject customservice = new JSONObject();
		customservice.put("kf_account", kf_account);
		jsonObject.put("touser", touser);
		jsonObject.put("msgtype", "news");
		jsonObject.put("news", articlesmap);
		jsonObject.put("customservice", customservice);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}
	
	/**
	 * 客服接口 - 发送图文消息给单个用户。图文消息条数限制在10条以内，注意，如果图文数超过10，则将会无响应。
	 * @param access_token
	 * @param news
	 */
	public static void sendNewsMsg(String access_token, NewsMassDomain4Reply news) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		JSONObject jsonObject = JSONObject.fromObject(news);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/**
	 * 客服接口 - 发送卡券
	 * 
	 * @param access_token 调用接口凭证
	 * @param touser
	 * @param card_id
	 * @param card_ext
	 */
	public static void sendCard(String access_token, String touser, String card_id, String card_ext) {
		String url = getInterfaceUrl(MESSAGE_CUSTOM_SEND_URL, access_token);
		String postData = "{\"touser\":\"" + touser + "\",\"wxcard\":{\"card_id\":\"" + card_id + "\", \"card_ext\":\"" + card_ext + "\"}}";
		JSONObject jsonObject = JSONObject.fromObject(postData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn weixinReturn = new Gson().fromJson(result, WeixinReturn.class);
		if (weixinReturn.isError()) {
			throw new WeixinErrorException(weixinReturn);
		}
	}

	/***************************************************************************/
	/****************************** 客服（账号）管理 ******************************/
	/***************************************************************************/
	/**
	 * 客服（账号）管理 - 获取客服基本信息 开发者通过本接口，根据AppID获取公众号中所设置的客服基本信息，包括客服工号、客服昵称、客服登录账号。
	 * 开发者利用客服基本信息，结合客服接待情况，可以开发例如“指定客服接待”等功能。
	 * 
	 * @param access_token
	 * @return
	 */
	public static GetkflistReturn getkflist(String access_token) {
		String url = getInterfaceUrl(CUSTOMSERVICE_GETKFLIST_URL, access_token);
		String result = HttpClientUtils.requestGet(url);
		GetkflistReturn returns = new Gson().fromJson(result, GetkflistReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 客服（账号）管理 - 获取在线客服接待信息 开发者通过本接口，根据AppID获取公众号中当前在线的客服的接待信息，包括客服工号、客服登录账号、
	 * 客服在线状态（手机在线、PC客户端在线、手机和PC客户端全都在线）、客服自动接入最大值、客服当前接待客户数。
	 * 开发者利用本接口提供的信息，结合客服基本信息，可以开发例如“指定客服接待”等功能；结合会话记录， 可以开发”在线客服实时服务质量监控“等功能。
	 * 
	 * @param access_token
	 * @return
	 */
	public static GetOnlineKflistReturn getonlinekflist(String access_token) {
		String url = getInterfaceUrl(CUSTOMSERVICE_GETONLINEKFLIST_URL, access_token);
		String result = HttpClientUtils.requestGet(url);
		GetOnlineKflistReturn returns = new Gson().fromJson(result, GetOnlineKflistReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 客服（账号）管理 - 添加客服账号
	 * 
	 * @param access_token
	 * @param kf_account 完整客服账号 格式为：账号前缀@公众号微信号
	 * @param nickname 客服昵称，最长6个汉字或12个英文字符
	 * @param password 客服账号登录密码，格式为密码明文的32位加密MD5值
	 */
	public static void addKfAccount(String access_token, String kf_account, String nickname, String password) {
		String url = getInterfaceUrl(CUSTOMSERVICE_KFACCOUNT_ADD_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("kf_account", kf_account);
		jsonObject.put("nickname", nickname);
		jsonObject.put("password", EncryptUtils.MD5(password));
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}

	/**
	 * 客服（账号）管理 - 设置客服信息
	 * 
	 * @param access_token
	 * @param kf_account 完整客服账号 格式为：账号前缀@公众号微信号
	 * @param nickname 客服昵称，最长6个汉字或12个英文字符
	 * @param password 客服账号登录密码，格式为密码明文的32位加密MD5值
	 */
	public static void updateKfAccount(String access_token, String kf_account, String nickname, String password) {
		String url = getInterfaceUrl(CUSTOMSERVICE_KFACCOUNT_UPDATE_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("kf_account", kf_account);
		jsonObject.put("nickname", nickname);
		jsonObject.put("password", EncryptUtils.MD5(password));
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}

	/**
	 * 客服（账号）管理 - 删除客服账号
	 * 
	 * @param access_token
	 * @param kf_account 完整客服账号，格式为：账号前缀@公众号微信号
	 */
	public static void delKfAccount(String access_token, String kf_account) {
		String url = getInterfaceUrl(CUSTOMSERVICE_KFACCOUNT_DEL_URL, access_token).replaceAll("KFACCOUNT", kf_account);
		String result = HttpClientUtils.requestGet(url);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}

	/***************************************************************************/
	/****************************** 多客服会话控制 ******************************/
	/***************************************************************************/
	/**
	 * 多客服会话控制 - 创建会话
	 * 
	 * 开发者可以使用本接口，为多客服的客服工号创建会话，将某个客户直接指定给客服工号接待，
	 * 需要注意此接口不会受客服自动接入数以及自动接入开关限制。只能为在线的客服（PC客户端在线，或者已绑定多客服助手）创建会话。
	 * 
	 * @param access_token
	 * @param openid 是 客户openid
	 * @param kf_account 是 完整客服账号，格式为：账号前缀@公众号微信号
	 * @param text 否 附加信息，文本会展示在客服人员的多客服客户端
	 */
	public static void createKfSession(String access_token, String openid, String kf_account, String text) {
		if (StringUtils.isEmpty(openid)) {
			throw new WeixinErrorException("客户openid为空!");
		}
		if (StringUtils.isEmpty(kf_account)) {
			throw new WeixinErrorException("客服账号为空!");
		}
		String url = getInterfaceUrl(CUSTOMSERVICE_KFSESSION_CREATE_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("openid", openid);
		jsonObject.put("kf_account", kf_account);
		if (StringUtils.isNotEmpty(text)) {
			jsonObject.put("text", text);
		}
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}

	/**
	 * 多客服会话控制 - 关闭会话
	 * 
	 * 开发者可以使用本接口，关闭一个会话。
	 * 
	 * @param access_token
	 * @param openid 是 客户openid
	 * @param kf_account 是 完整客服账号，格式为：账号前缀@公众号微信号
	 * @param text 否 附加信息，文本会展示在客服人员的多客服客户端
	 */
	public static void closeKfSession(String access_token, String openid, String kf_account, String text) {
		if (StringUtils.isEmpty(openid)) {
			throw new WeixinErrorException("客户openid为空!");
		}
		if (StringUtils.isEmpty(kf_account)) {
			throw new WeixinErrorException("客服账号为空!");
		}
		String url = getInterfaceUrl(CUSTOMSERVICE_KFSESSION_CLOSE_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("openid", openid);
		jsonObject.put("kf_account", kf_account);
		if (StringUtils.isNotEmpty(text)) {
			jsonObject.put("text", text);
		}
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}

	/**
	 * 多客服会话控制 - 获取客户的会话状态 开发者可以通过本接口获取客户当前的会话状态。
	 * 
	 * @param access_token
	 * @param openid 是 客户openid
	 */
	public static void getCustSessionStatus(String access_token, String openid) {
		if (StringUtils.isEmpty(openid)) {
			throw new WeixinErrorException("客户openid为空!");
		}
		String url = getInterfaceUrl(CUSTOMSERVICE_KFSESSION_GETSESSION_URL, access_token).replaceAll("OPENID", openid);
		;
		String result = HttpClientUtils.requestGet(url);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}

	/**
	 * 多客服会话控制 - 获取客服的会话列表 开发者可以通过本接口获取某个客服正在接待的会话列表。
	 * 
	 * @param access_token
	 * @param kf_account 是 完整客服账号，格式为：账号前缀@公众号微信号，账号前缀最多10个字符，必须是英文或者数字字符
	 * @return
	 */
	public static GetSessionListReturn getKfSessionList(String access_token, String kf_account) {
		if (StringUtils.isEmpty(kf_account)) {
			throw new WeixinErrorException("客服账号为空!");
		}
		String url = getInterfaceUrl(CUSTOMSERVICE_KFSESSION_GETSESSIONLIST_URL, access_token).replaceAll("KFACCOUNT", kf_account);
		String result = HttpClientUtils.requestGet(url);
		GetSessionListReturn returns = new Gson().fromJson(result, GetSessionListReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 多客服会话控制 - 获取未接入会话列表 开发者可以通过本接口获取当前正在等待队列中的会话列表，此接口最多返回最早进入队列的100个未接入会话。
	 * 
	 * @param access_token
	 * @return
	 */
	public static GetWaitCaseReturn getWaitCase(String access_token) {
		String url = getInterfaceUrl(CUSTOMSERVICE_KFSESSION_GETWAITCASE_URL, access_token);
		String result = HttpClientUtils.requestGet(url);
		GetWaitCaseReturn returns = new Gson().fromJson(result, GetWaitCaseReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/***************************************************************************/
	/****************************** 获取客服聊天记录 *****************************/
	/***************************************************************************/

	/**
	 * 获取客服聊天记录接口
	 * 
	 * @param access_token
	 * @param starttime 是 查询开始时间，UNIX时间戳
	 * @param endtime 是 查询结束时间，UNIX时间戳，每次查询不能跨日查询
	 * @param pageindex 是 查询第几页，从1开始
	 * @param pagesize 是 每页大小，每页最多拉取50条
	 * @return
	 */
	public static GetRecordReturn getDkfRecord(String access_token, Date starttime, Date endtime, Integer pageindex, Integer pagesize) {
		if (starttime == null) {
			throw new WeixinErrorException("查询开始时间为空!");
		}
		if (endtime == null) {
			throw new WeixinErrorException("查询结束时间!");
		}
		String url = getInterfaceUrl(CUSTOMSERVICE_MSGRECORD_GETRECORD_URL, access_token);
		if (pageindex == null) {
			pageindex = 1;
		}
		if (pagesize == null) {
			pagesize = 50;
		}
		Long lstarttime = starttime.getTime() / 1000;
		Long lendtime = endtime.getTime() / 1000;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("starttime", lstarttime);
		jsonObject.put("endtime", lendtime);
		jsonObject.put("pagesize", pagesize);
		jsonObject.put("pageindex", pageindex);
		String result = HttpClientUtils.requestPost2(url, jsonObject);
		GetRecordReturn returns = new Gson().fromJson(result, GetRecordReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}
}