package com.basoft.core.ware.wechat.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.core.ware.wechat.domain.WeixinReturn;
import com.basoft.core.ware.wechat.domain.mass.MassReturn;
import com.basoft.core.ware.wechat.domain.mass.Media;
import com.basoft.core.ware.wechat.domain.mass.NewsData;
import com.basoft.core.ware.wechat.domain.mass.PreviewMassReturn;
import com.basoft.core.ware.wechat.domain.mass.SendMassStatusReturn;
import com.basoft.core.ware.wechat.domain.mass.Text;
import com.basoft.core.ware.wechat.domain.mass.UploadImgReturn;
import com.basoft.core.ware.wechat.domain.material.MediaReturn;
import com.basoft.core.ware.wechat.exception.MediaException;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
 * 高级群发接口工具类
 */
public class WeixinMassMessageUtils extends WeixinBaseUtils {
	private static final transient Log logger = LogFactory.getLog(WeixinMassMessageUtils.class);

	/**
	 * 上传图文消息内的图片获取URL -2015-07-31微信开放此接口
	 */
	private static final String MEDIA_UPLOAD_IMG_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";

	/**
	 * 上传图文消息素材接口
	 */
	private static final String MEDIA_UPLOAD_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";

	/**
	 * 根据分组进行群发接口
	 */
	private static final String MESSAGE_MASS_SEND_ALL_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";

	/**
	 * 根据OpenID列表群发接口
	 */
	private static final String MESSAGE_MASS_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";

	/**
	 * 删除群发接口
	 */
	private static final String MESSAGE_MASS_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=ACCESS_TOKEN";

	/**
	 * 预览接口
	 */
	private static final String MESSAGE_MASS_PREVIEW_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN";

	/**
	 * 查询群发消息发送状态接口
	 */
	private static final String MESSAGE_MASS_GET_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=ACCESS_TOKEN";
	
	private static JSONObject getFilter(boolean is_to_all, String group_id) {
		JSONObject filter = new JSONObject();
		if (is_to_all) {
			filter.put("is_to_all", true);
		} else {
			filter.put("is_to_all", false);
			filter.put("group_id", group_id);
		}
		return filter;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param file
	 * @return 扩展名
	 */
	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") > 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}
	
	/**
	 * 上传图文消息内的图片获取URL -2015-07-31微信开放此接口
	 * <div>备注：</div>
	 * <h3>公众平台新增图文内容中图片上传接口，并过滤外链图片</h3>
	 * 为了加快图文内容的浏览速度，公众平台新增了图文消息内容中的图片上传接口。
	 * 开发者可以通过该接口上传所需要的图片来获得图片链接，再把图片链接放到图文内容中。
	 * <br>
	 * 同时，为了加强平台安全性，从8月15日起， 系统将自动过滤图文内容中的外链图片（8月15日之前的图文内容不会过滤）。
	 * 下述接口受到影响，请开发者尽快修改：
	 * <ol>
	 * 	<li>群发图文消息时，上传图文的接口，查看接口文档</li>
	 * 	<li>素材管理中的新增永久图文素材接口，查看接口文档</li>
	 * </ol>
	 * <div>
	 * 微信团队
	 * <br>
	 * 2015年07月31日
	 * </div>
	 * @param access_token 调用接口凭证
	 * @param fileUrl
	 * @return
	 * 
	 *  请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
	 */
	public static String uploadImg(String access_token,  String fileUrl) {
		/*if (!chechType(type)) {
			throw new MediaException(ErrorCode.CODE_40004.getCode(), ErrorCode.CODE_40004.getDesc());
		}*/
		String url = getInterfaceUrl(MEDIA_UPLOAD_IMG_URL, access_token);
		File file = new File(fileUrl);
		if (!file.exists() || !file.isFile()) {
			String errMsg = "不合法的文件路径=" + fileUrl;
			logger.error(errMsg);
			throw new MediaException(errMsg);
		}
		String ext = getFileExtension(file);
		if (!"jpg".equals(ext) && !"jpeg".equals(ext) && !"png".equals(ext)) { // jpg,jpeg,png
			String errMsg = "图片仅支持jpg/jpeg/png格式";
			logger.error(errMsg);
			throw new MediaException(errMsg);
		}
		if (FileUtils.sizeOf(file) > 1024 * 1024) { // 大于1M
			String errMsg = "大小必须在1MB以下!" + FileUtils.sizeOf(file);
			logger.error(errMsg);
			throw new MediaException(errMsg);
		}
		String result = HttpClientUtils.uploadImg(url, fileUrl);
		logger.info("result====" + result);
		UploadImgReturn returns = new Gson().fromJson(result, UploadImgReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getUrl();
	}

	/**
	 * 上传图文消息素材
	 * 
	 * @param access_token
	 * @param newsData POST数据
	 * @return
	 */
	public static MediaReturn uploadNews(String access_token, NewsData newsData) {
		String url = getInterfaceUrl(MEDIA_UPLOAD_NEWS_URL, access_token);
		JSONObject jsonObject = JSONObject.fromObject(newsData);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		MediaReturn returns = new Gson().fromJson(result, MediaReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 根据分组进行群发 - 文本
	 * 
	 * @param access_token
	 * @param is_to_all 否
	 *            用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，选择false可根据group_id发送给指定群组的用户
	 * @param group_id 否 群发到的分组的group_id，参加用户管理中用户分组接口，若is_to_all值为true，可不填写group_id
	 * @param content 是 文本内容
	 * @return
	 */
	public static MassReturn sendTextAll(String access_token, boolean is_to_all, String group_id, String content) {
		String url = getInterfaceUrl(MESSAGE_MASS_SEND_ALL_URL, access_token);
		JSONObject jsonText = new JSONObject();
		jsonText.put("content", content);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("filter", getFilter(is_to_all, group_id));
		jsonObject.put("text", jsonText);
		jsonObject.put("msgtype", "text");
		String result = HttpClientUtils.requestPost(url, jsonObject);
		MassReturn returns = new Gson().fromJson(result, MassReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 根据分组进行群发 - 图片（注意此处media_id需通过基础支持中的上传下载多媒体文件来得到）
	 * 
	 * @param access_token
	 * @param is_to_all 否
	 *            用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，选择false可根据group_id发送给指定群组的用户
	 * @param group_id 否 群发到的分组的group_id，参加用户管理中用户分组接口，若is_to_all值为true，可不填写group_id
	 * @param media_id 是 用于群发的消息的media_id
	 * @return
	 */
	public static MassReturn sendImageAll(String access_token, boolean is_to_all, String group_id, String media_id) {
		String url = getInterfaceUrl(MESSAGE_MASS_SEND_ALL_URL, access_token);
		JSONObject jsonMediaId = new JSONObject();
		jsonMediaId.put("media_id", media_id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("filter", getFilter(is_to_all, group_id));
		jsonObject.put("image", jsonMediaId);
		jsonObject.put("msgtype", "image");
		String result = HttpClientUtils.requestPost(url, jsonObject);
		MassReturn returns = new Gson().fromJson(result, MassReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 根据分组进行群发 - 语音（注意此处media_id需通过基础支持中的上传下载多媒体文件来得到）
	 * 
	 * @param access_token
	 * @param is_to_all 否
	 *            用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，选择false可根据group_id发送给指定群组的用户
	 * @param group_id 否 群发到的分组的group_id，参加用户管理中用户分组接口，若is_to_all值为true，可不填写group_id
	 * @param media_id 是 用于群发的消息的media_id
	 * @return
	 */
	public static MassReturn sendVoiceAll(String access_token, boolean is_to_all, String group_id, String media_id) {
		String url = getInterfaceUrl(MESSAGE_MASS_SEND_ALL_URL, access_token);
		JSONObject jsonMediaId = new JSONObject();
		jsonMediaId.put("media_id", media_id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("filter", getFilter(is_to_all, group_id));
		jsonObject.put("voice", jsonMediaId);
		jsonObject.put("msgtype", "voice");
		String result = HttpClientUtils.requestPost(url, jsonObject);
		MassReturn returns = new Gson().fromJson(result, MassReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 根据分组进行群发 - 视频（请注意，此处视频的media_id需通过POST请求到下述接口特别地得到： 
	 * https://file.api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=ACCESS_TOKEN 
	 * POST数据如下（此处media_id需通过基础支持中的上传下载多媒体文件来得到）：）
	 * 	<pre><code>
	 * 	{
	 * 	  "media_id": "rF4UdIMfYK3efUfyoddYRMU50zMiRmmt_l0kszupYh_SzrcW5Gaheq05p_lHuOTQ",
	 *	  "title": "TITLE",
	 *	  "description": "Description"
	 *	}
	 *</code>
	 *返回将为 
	 *<code>
	 * 	{
	 * 	  "type":"video",
	 *	  "media_id":"IhdaAQXuvJtGzwwc0abfXnzeezfO0NgPK6AQYShD8RQYMTtfzbLdBIQkQziv2XJc",
	 *	  "created_at":1398848981
	 *	}
	 *</code></pre>
	 * @param access_token
	 * @param is_to_all 否 用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，选择false可根据group_id发送给指定群组的用户
	 * @param group_id	否 群发到的分组的group_id，参加用户管理中用户分组接口，若is_to_all值为true，可不填写group_id
	 * @param media_id	是 用于群发的消息的media_id 此处的ID为例如"IhdaAQXuvJtGzwwc0abfXnzeezfO0NgPK6AQYShD8RQYMTtfzbLdBIQkQziv2XJc",
	 * @return
	 */
	public static MassReturn sendVideoAll(String access_token, boolean is_to_all, String group_id, String media_id) {
		String url = getInterfaceUrl(MESSAGE_MASS_SEND_ALL_URL, access_token);
		JSONObject jsonMediaId = new JSONObject();
		jsonMediaId.put("media_id", media_id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("filter", getFilter(is_to_all, group_id));
		jsonObject.put("mpvideo", jsonMediaId);
		jsonObject.put("msgtype", "mpvideo");
		String result = HttpClientUtils.requestPost(url, jsonObject);
		MassReturn returns = new Gson().fromJson(result, MassReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 根据分组进行群发 - 图文消息（注意图文消息的media_id需要通过上述上传图文消息素材方法来得到）：
	 * 
	 * @param access_token
	 * @param is_to_all 否
	 *            用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，选择false可根据group_id发送给指定群组的用户
	 * @param group_id 否 群发到的分组的group_id，参加用户管理中用户分组接口，若is_to_all值为true，可不填写group_id
	 * @param media_id 是 用于群发的消息的media_id
	 * @return
	 * 
	 * 		2015年7月28日 群发接口在群发图文消息时，增加了msg_data_id的返回，可以用于在群发后使用图文分析接口获取图文消息的数据。
	 */
	public static MassReturn sendMpnewsAll(String access_token, boolean is_to_all, String group_id, String media_id) {
		String url = getInterfaceUrl(MESSAGE_MASS_SEND_ALL_URL, access_token);
		JSONObject jsonMediaId = new JSONObject();
		jsonMediaId.put("media_id", media_id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("filter", getFilter(is_to_all, group_id));
		jsonObject.put("mpnews", jsonMediaId);
		jsonObject.put("msgtype", "mpnews");
		String result = HttpClientUtils.requestPost(url, jsonObject);
		MassReturn returns = new Gson().fromJson(result, MassReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 根据OpenID列表群发
	 * 
	 * @param access_token
	 * @param obj
	 * @return
	 */
	public static MassReturn sendMass(String access_token, Object obj) {
		String url = getInterfaceUrl(MESSAGE_MASS_SEND_URL, access_token);
		JSONObject jsonObject = JSONObject.fromObject(obj);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		MassReturn returns = new Gson().fromJson(result, MassReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 删除群发 <br>
	 * 请注意，只有已经发送成功的消息才能删除删除消息只是将消息的图文详情页失效，<br>
	 * 已经收到的用户，还是能在其本地看到消息卡片。 另外，删除群发消息只能删除图文<br>
	 * 消息和视频消息，其他类型的消息一经发送，无法删除。
	 * 
	 * @param access_token
	 * @param msgId 发送出去的消息ID
	 */
	public static void deleteMassMessage(String access_token, Long msgId) {
		String url = getInterfaceUrl(MESSAGE_MASS_DELETE_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg_id", msgId);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WeixinReturn returns = new Gson().fromJson(result, WeixinReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
	}

	/**
	 * 预览接口 发者可通过该接口发送消息给指定用户，在手机端查看消息的样式和排版。
	 * 
	 * @param access_token
	 * @param touser 接收消息用户对应该公众号的openid
	 * @param msgtype
	 *            群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为video
	 * @param media_id_or_text 用于群发的消息的media_id 或文本信息
	 * @return
	 */
	public static Long previewMassMessage(String access_token, String touser, String msgtype, String media_id_or_text) {
		String url = getInterfaceUrl(MESSAGE_MASS_PREVIEW_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("touser", touser);
		jsonObject.put("msgtype", msgtype);
		if (msgtype.equals("text")) {
			jsonObject.put(msgtype, new Text(media_id_or_text));
		} else {
			jsonObject.put(msgtype, new Media(media_id_or_text));
		}
		String result = HttpClientUtils.requestPost(url, jsonObject);
		PreviewMassReturn returns = new Gson().fromJson(result, PreviewMassReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getMsg_id();
	}

	/**
	 * 查询群发消息发送状态
	 * 
	 * @param access_token
	 * @param msgId 发送出去的消息ID
	 * @return
	 * @throws Exception
	 */
	public static SendMassStatusReturn getSendMassStatus(String access_token, Long msgId) throws Exception {
		String url = getInterfaceUrl(MESSAGE_MASS_GET_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg_id", msgId);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		SendMassStatusReturn sendMassStatusReturn = new Gson().fromJson(result, SendMassStatusReturn.class);
		return sendMassStatusReturn;
	}
}
