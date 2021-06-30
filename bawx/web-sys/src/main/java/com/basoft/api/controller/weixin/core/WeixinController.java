package com.basoft.api.controller.weixin.core;

import com.basoft.core.ware.common.aes.AesException;
import com.basoft.core.ware.common.aes.SHA1;
import com.basoft.core.ware.common.framework.utilities.UploadFileUtil;
import com.basoft.core.ware.wechat.consant.DownloadFileType;
import com.basoft.core.ware.wechat.domain.ShareInfo;
import com.basoft.core.ware.wechat.domain.msg.EventLocation;
import com.basoft.core.ware.wechat.domain.msg.Image;
import com.basoft.core.ware.wechat.domain.msg.Link;
import com.basoft.core.ware.wechat.domain.msg.Location;
import com.basoft.core.ware.wechat.domain.msg.Text;
import com.basoft.core.ware.wechat.domain.msg.Video;
import com.basoft.core.ware.wechat.domain.msg.Voice;
import com.basoft.core.ware.wechat.domain.user.Data;
import com.basoft.core.ware.wechat.domain.user.UserListReturn;
import com.basoft.core.ware.wechat.domain.user.WXUser;
import com.basoft.core.ware.wechat.util.Oauth2Utils;
import com.basoft.core.ware.wechat.util.WeixinMediaUtils;
import com.basoft.core.ware.wechat.util.WeixinSignUtils;
import com.basoft.core.ware.wechat.util.WeixinUserUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.common.WechatSignalService;
import com.basoft.service.definition.wechat.user.WeixinUserService;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/weixin")
public class WeixinController extends WeixinBaseController {
	private final transient Log logger = LogFactory.getLog(WeixinController.class);

	// 微信公众号的SYS_ID
	private static final String KEY = "1";

	@Autowired
	private WechatService wechatService;

	@Autowired
	private WechatSignalService wechatSignalService;
	
	@Autowired
	private WeixinUserService weixinUserService;

	//@PostMapping(value = "/process")
	//@GetMapping(value = "/process")
	@RequestMapping(value = "/process",method = {RequestMethod.POST, RequestMethod.GET})  
	@ResponseBody  
	public String process(@RequestParam(value = "key", defaultValue = KEY) String key) {
		log.info(request.getRequestURI());
		log.info(request.getRequestURL().toString());
		log.info(request.getQueryString());
		long startTime = System.currentTimeMillis();
		logger.info("========微信服务器-后台服务器交互开始========");
		String result = "";
		String echostr = request.getParameter("echostr");// 随机字符串
		logger.info("valid >>>>>>>>>>>>>>>>echostr=" + echostr);
		/*if (key == null || key.length() != 32) {
			requestPocket.put("result", "error");
			logger.error("WX00001==============");
			logger.error("key无效");
			return "success";
		}*/
		
		AppInfo appInfo  = null;
		try {
			 appInfo = wechatService.selectAppInfoByKey(KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		if (appInfo == null) {
			requestPocket.put("result", "error");
			logger.error("WX00002==============");
			logger.error("公众号信息找不到 key=" + key);
			return "success";
		}

		if (StringUtils.isEmpty(echostr)) {
			try {
				result = wechatSignalService.processingAndReplyMessage(request, appInfo);
			} catch (Exception e) {
				logger.error("WX00003==============");
				logger.error(e.getMessage());
				result = "success";
			}
		} else {// 验证URL有效性
			try {
				if (checkSignature(appInfo.getToken())) {
					// logger.debug("echostr==== " + echostr);
					result = echostr;
				} else {
					result = "error";
				}
			} catch (AesException e) {
				logger.error("WX00004==============");
				logger.error(e.getMessage());
				result = "error";
			}
		}
		requestPocket.put("result", result);
		logger.info("========微信服务器-后台服务器交互END========");
		long executeTime = System.currentTimeMillis() - startTime;
		logger.info("executeTime=== "+ executeTime );
		// return "success";
		return result;
	}
	
	// 微信接口验证
	private boolean checkSignature(String token) throws AesException {
		// 加密/校验流程如下：
		// 1. 将token、timestamp、nonce三个参数进行字典序排序
		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		// 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		// String token=TOKEN;
		String signature = request.getParameter("signature");// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce");// 随机数

//		logger.debug("signature====" + signature);
//		logger.debug("timestamp====" + timestamp);
//		logger.debug("nonce========" + nonce);

		String signatureNew = SHA1.getSHA2(token, timestamp, nonce);
//		logger.debug("signature====" + signature);
//		logger.debug("signatureNew====" + signatureNew);
		//
		if (signature.equalsIgnoreCase(signatureNew)) {
			return true;
		} else {
			return false;
		}
	}

	public String test() throws Exception{
		//jsSdkStting();
		 String url = getFullUrl();
		 logger.info("url====" + url);
		 String key  =  request.getParameter("key");
		try {
			AppInfo appInfo = wechatService.selectAppInfoByKey(key);
			String ticket = wechatService.getApiTicket(appInfo);
			// logger.debug("ticket=====" + ticket);
			if (StringUtils.isNotEmpty(ticket)) {
				requestPocket.put("signInfo", WeixinSignUtils.sign(ticket, url));
			}
			requestPocket.put("appInfo", appInfo);
			
			com.basoft.core.ware.wechat.domain.AppInfo originalAppInfo = new com.basoft.core.ware.wechat.domain.AppInfo();
			BeanUtils.copyProperties(originalAppInfo, appInfo);
			String shareUrl = Oauth2Utils.getShareLinkUrl(originalAppInfo, url);
			
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.setTitle("媒体发布会邀请函");
			shareInfo.setDesc("薄艺硬屏/薄艺（Art Slim）电视媒体发布会 2015.4.15 北京");
			shareInfo.setImgUrl(originalAppInfo.getDomain() +  "/uploads/150/2015_01_24/shopFile2015_01_24_15_43_041361.png");	
			shareInfo.setLink(shareUrl);
			
			requestPocket.put("shareInfo", shareInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return "test"; 
	}

	/**
	 * 分享结果处理
	 * 
	 * @return
	 */
	public String fenxiangresult() {
		String url = getFullUrl();
		// logger.debug("url====" + url);
		try {
			AppInfo appInfo = wechatService.selectAppInfoByKey(KEY);
			String ticket = wechatService.getApiTicket(appInfo);
			// logger.debug("ticket=====" + ticket);
			if (StringUtils.isNotEmpty(ticket)) {
				requestPocket.put("signInfo", WeixinSignUtils.sign(ticket, url));
			}
			requestPocket.put("appInfo", appInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fenxiangresult";
	}

	/**
	 * 关注
	 * 
	 * @return
	 */
	public String subscribe() {
		AppInfo app = wechatService.selectAppInfoByKey(key);
		logger.debug("====================================");
		String token = wechatService.getAccessToken(app);
		logger.debug("token=" + token);

		UserListReturn userListReturn = WeixinUserUtils.getUserList(token, null);
		if (userListReturn.getErrcode() == 0) {
			Data data = userListReturn.getData();
			for (String openid : data.getOpenid()) {
				WXUser user = new WXUser(openid);
				user.setSysId(key);
				try {
					weixinUserService.userSubscribe(token, user);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		logger.info("==================END==================");
		return "success";
	}

	/**
	 * 取消关注
	 * 
	 * @return
	 */
	public String unsubscribe() {
		WXUser user = new WXUser("o1yuEtw1PN5sc6W6Bxd7Q4hty2hY");
		try {
			weixinUserService.userUnsubscribe(user);
			requestPocket.put("result", user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("====================================");
		return "success";
	}

	/**
	 * 模拟接收用户消息-文本
	 * 
	 * @return
	 */
	public String text() {
		key = KEY;
		String decryptedData = "<xml><ToUserName><![CDATA[gh_c2bb99ea6fa5]]></ToUserName>"
				+ "<FromUserName><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></FromUserName>" + "<CreateTime>1417757742</CreateTime>"
				+ "<MsgType><![CDATA[text]]></MsgType>" + "<Content><![CDATA[q1]]></Content>" + "<MsgId>6089223135743160205</MsgId>" + "</xml>";
		
		// 解析xml
		Document document = null;
		try {
			document = DocumentHelper.parseText(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == document) {
			requestPocket.put("result", "");
			return "success";
		}
		Element root = document.getRootElement();
		String FromUserName = root.elementText("FromUserName");
		String ToUserName = root.elementText("ToUserName");
		String CreateTime = root.elementTextTrim("CreateTime");
		String MsgType = root.elementTextTrim("MsgType");
		String Content = root.elementTextTrim("Content");
		String MsgId = root.elementTextTrim("MsgId");
		Text text = new Text(key, MsgId, FromUserName, ToUserName, CreateTime, MsgType, Content);
		try {
			wechatSignalService.insertTextMsg(text);
			requestPocket.put("result", MsgId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "success";
	}

	/**
	 * 模拟接收用户消息-图片信息
	 * 
	 * @return
	 */
	public String image() {
		key = KEY;
		String decryptedData = "<xml><ToUserName><![CDATA[gh_c2bb99ea6fa5]]></ToUserName>"
				+ "<FromUserName><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></FromUserName>"
				+ "<CreateTime>1417761449</CreateTime>"
				+ "<MsgType><![CDATA[image]]></MsgType>"
				+ "<PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz/2EhaNNgAhfaWy061uk5W8DtTz7dPCfkC8fufGysE6Cc66TVKOz321jibHwkTbcHh1lkjP3MFH4wP2BvFV4DCH8Q/0]]></PicUrl>"
				+ "<MsgId>6089239057186927631</MsgId>" + "<MediaId><![CDATA[zBIxaXzU4wUM8epcYLkplOsX23ahD8r5bpHcgq5bdJvXSl-MfSzcjPZssws5OkW0]]></MediaId>"
				+ "</xml>";
		// String decryptedData=
		// "<xml><ToUserName><![CDATA[gh_c2bb99ea6fa5]]></ToUserName>"
		// +"<FromUserName><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></FromUserName>"
		// +"<CreateTime>1417757742</CreateTime>"
		// +"<MsgType><![CDATA[text]]></MsgType>"
		// +"<Content><![CDATA[q1]]></Content>"
		// +"<MsgId>6089223135743160205</MsgId>"
		// +"</xml>";
		
		// 解析xml
		Document document = null;
		try {
			document = DocumentHelper.parseText(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == document) {
			requestPocket.put("result", "");
			return "success";
		}
		Element root = document.getRootElement();
		String FromUserName = root.elementText("FromUserName");
		String ToUserName = root.elementText("ToUserName");
		String CreateTime = root.elementTextTrim("CreateTime");
		String MsgType = root.elementTextTrim("MsgType");
		String PicUrl = root.elementTextTrim("PicUrl");
		String MediaId = root.elementTextTrim("MediaId");
		String MsgId = root.elementTextTrim("MsgId");

		Image image = new Image(key, MsgId, FromUserName, ToUserName, CreateTime, MsgType, MediaId, PicUrl);

		long id = wechatSignalService.insertImageMsg(image);
		logger.debug(id);

		logger.debug("image=" + image);
		requestPocket.put("result", MsgId);

		return "success";
	}

	/**
	 * 模拟接收用户消息-语音信息
	 * 
	 * @return
	 */
	public String voice() {
		key = KEY;
		String decryptedData = "<xml><ToUserName><![CDATA[gh_c2bb99ea6fa5]]></ToUserName>"
				+ "<FromUserName><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></FromUserName>" + "<CreateTime>1417762763</CreateTime>"
				+ "<MsgType><![CDATA[voice]]></MsgType>" + "<MediaId><![CDATA[fRM5UNtZmWP90uHJnYFoL7rc8OKzuqiTNbOMdKGkCwtA-HVD0diAyYz6eZuNxsk3]]></MediaId>"
				+ "<Format><![CDATA[speex]]></Format>" + "<MsgId>6089244700571598848</MsgId>" + "<Recognition><![CDATA[]]></Recognition>" + "</xml>";

		// 解析xml
		Document document = null;
		try {
			document = DocumentHelper.parseText(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == document) {
			requestPocket.put("result", "");
			return "success";
		}
		Element root = document.getRootElement();
		String FromUserName = root.elementText("FromUserName");
		String ToUserName = root.elementText("ToUserName");
		String CreateTime = root.elementTextTrim("CreateTime");
		String MsgType = root.elementTextTrim("MsgType");
		String MediaId = root.elementTextTrim("MediaId");
		String MsgId = root.elementTextTrim("MsgId");
		String Format = root.elementTextTrim("Format");

		Voice vocie = new Voice(key, MsgId, FromUserName, ToUserName, CreateTime, MsgType, MediaId, Format);
		long id = wechatSignalService.insertVoiceMsg(vocie);
		logger.debug(id);
		logger.debug("vocie=" + vocie);
		requestPocket.put("result", MsgId);
		return "success";
	}

	/**
	 * 模拟接收用户消息-视频信息
	 * 
	 * @return
	 */
	public String video() {
		key = KEY;
		String decryptedData = "<xml><ToUserName><![CDATA[gh_c2bb99ea6fa5]]></ToUserName>"
				+ "<FromUserName><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></FromUserName>" + "<CreateTime>1417763573</CreateTime>"
				+ "<MsgType><![CDATA[video]]></MsgType>" + "<MediaId><![CDATA[TF60vZz2LB8sFVE8KfZvDM48g_nsBgyCabCRGt8f8kPytgnBeMFWRobu6TZJyl-C]]></MediaId>"
				+ "<ThumbMediaId><![CDATA[C4UDC2MCzKJoQq0VFTDzJJg1tHXJLqE-N_vz7mSITulk8HuDVe_sDZAKApyYObgQ]]></ThumbMediaId>"
				+ "<MsgId>6089248179697465252</MsgId>" + "</xml>";

		// 解析xml
		Document document = null;
		try {
			document = DocumentHelper.parseText(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == document) {
			requestPocket.put("result", "");
			return "success";
		}
		Element root = document.getRootElement();
		String FromUserName = root.elementText("FromUserName");
		String ToUserName = root.elementText("ToUserName");
		String CreateTime = root.elementTextTrim("CreateTime");
		String MsgType = root.elementTextTrim("MsgType");
		String MediaId = root.elementTextTrim("MediaId");
		String ThumbMediaId = root.elementTextTrim("ThumbMediaId");
		String MsgId = root.elementTextTrim("MsgId");

		Video video = new Video(key, MsgId, FromUserName, ToUserName, CreateTime, MsgType, MediaId, ThumbMediaId);
		long id = wechatSignalService.insertVideoMsg(video);
		logger.debug(id);
		logger.debug("video=" + video);
		requestPocket.put("result", MsgId);
		return "success";
	}

	/**
	 * 模拟接收用户消息-位置信息
	 * 
	 * @return
	 */
	public String location() {
		key = KEY;
		String decryptedData = "<xml><ToUserName><![CDATA[gh_c2bb99ea6fa5]]></ToUserName>"
				+ "<FromUserName><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></FromUserName>" + "<CreateTime>1417764377</CreateTime>"
				+ "<MsgType><![CDATA[location]]></MsgType>" + "<Location_X>37.547159</Location_X>" + "<Location_Y>121.268670</Location_Y>"
				+ "<Scale>15</Scale>" + "<Label><![CDATA[]]></Label>" + "<MsgId>6089251632851171569</MsgId>" + "</xml>";

		// 解析xml
		Document document = null;
		try {
			document = DocumentHelper.parseText(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == document) {
			requestPocket.put("result", "");
			return "success";
		}
		Element root = document.getRootElement();
		String FromUserName = root.elementText("FromUserName");
		String ToUserName = root.elementText("ToUserName");
		String CreateTime = root.elementTextTrim("CreateTime");
		String MsgType = root.elementTextTrim("MsgType");
		String Location_X = root.elementTextTrim("Location_X");
		String Location_Y = root.elementTextTrim("Location_Y");
		String Scale = root.elementTextTrim("Scale");
		String Label = root.elementTextTrim("Label");
		String MsgId = root.elementTextTrim("MsgId");
		
		try {
			Location location = new Location(key, MsgId, FromUserName, ToUserName, CreateTime, MsgType, Location_X, Location_Y, Scale, Label);
			long id = wechatSignalService.insertLocationMsg(location);
			logger.debug(id);
			logger.debug("Location=" + location);
			requestPocket.put("result", location);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 模拟接收用户消息-uploadLocation
	 * 
	 * @return
	 */
	public String uploadLocation() {
		key = KEY;
		// String decryptedData=
		// "<xml><ToUserName><![CDATA[gh_c2bb99ea6fa5]]></ToUserName>"
		// +"<FromUserName><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></FromUserName>"
		// +"<CreateTime>1417764377</CreateTime>"
		// +"<MsgType><![CDATA[location]]></MsgType>"
		// +"<Location_X>37.547159</Location_X>"
		// +"<Location_Y>121.268670</Location_Y>"
		// +"<Scale>15</Scale>"
		// +"<Label><![CDATA[]]></Label>"
		// +"<MsgId>6089251632851171569</MsgId>"
		// +"</xml>";
		String decryptedData = "<xml><ToUserName><![CDATA[gh_c2bb99ea6fa5]]></ToUserName>"
				+ "<FromUserName><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></FromUserName>" + "<Latitude>37.546413</Latitude>"
				+ "<Longitude>121.263474</Longitude>" + "<Precision>77.784286</Precision>" + "</xml>";

		// 解析xml
		Document document = null;
		try {
			document = DocumentHelper.parseText(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == document) {
			requestPocket.put("result", "");
			return "success";
		}
		Element root = document.getRootElement();
		String FromUserName = root.elementText("FromUserName");
		String Longitude = root.elementTextTrim("Longitude");
		String Latitude = root.elementTextTrim("Latitude");
		String Precision = root.elementTextTrim("Precision");

		try {
			// public EventLocation(String sysId, String openid, String
			// longitude, String latitude, String precision, Date createDate) {
			EventLocation location = new EventLocation(key, FromUserName, Longitude, Latitude, Precision, new Date());
			long id = wechatSignalService.insertEventLocation(location);
			logger.debug(id);
			logger.debug("Location=" + Longitude + "," + Latitude);
			requestPocket.put("result", location);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 模拟接收用户消息-链接
	 * 
	 * @return
	 */
	public String link() {
		key = KEY;
		String decryptedData = "<xml><ToUserName><![CDATA[gh_c2bb99ea6fa5]]></ToUserName>" + "<FromUserName><![CDATA[o1yuEtw1PN5sc6W6Bxd7Q4hty2hY]]></FromUserName>"
				+ "<CreateTime>1417766647</CreateTime>" + "<MsgType><![CDATA[link]]></MsgType>" + "<Title><![CDATA[逸贷感恩在此刻：今天你感恩了么？]]></Title>"
				+ "<Description><![CDATA[是什么样的倔强，让我们对身边的人总是没法说出心里话。逸贷感恩在此刻，想对TA说，就大声说出来。年底节日重重，]]></Description>"
				+ "<Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MjM5NjQ4NzAwMQ==&mid=437384497&idx=2&sn=badf36ffc2d58e775a365335469fe791#rd]]></Url>"
				+ "<MsgId>6089261382426934445</MsgId>" + "</xml>";
		// 解析xml
		Document document = null;
		try {
			document = DocumentHelper.parseText(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == document) {
			requestPocket.put("result", "");
			return "success";
		}
		Element root = document.getRootElement();
		String FromUserName = root.elementText("FromUserName");
		String ToUserName = root.elementText("ToUserName");
		String CreateTime = root.elementTextTrim("CreateTime");
		String MsgType = root.elementTextTrim("MsgType");
		String Title = root.elementTextTrim("Title");
		String Description = root.elementTextTrim("Description");
		String Url = root.elementTextTrim("Url");
		String MsgId = root.elementTextTrim("MsgId");

		try {

			Link link = new Link(key, MsgId, FromUserName, ToUserName, CreateTime, MsgType, Title, Description, Url);
			long id = wechatSignalService.insertLinkMsg(link);
			logger.debug(id);
			logger.debug("Link=" + link);
			requestPocket.put("result", link);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	public String downloadMedia() {
		String filePath = UploadFileUtil.getUploadPath(request, UploadFileUtil.UPLOAD_WEIXIN);
		String fileUrl = UploadFileUtil.getUploadUrl(request, UploadFileUtil.UPLOAD_WEIXIN);

		AppInfo appInfo = wechatService.selectAppInfoByKey(key);
		String mediaId = request.getParameter("mediaId");
		
		logger.debug("filePath======" + filePath);
		logger.debug("key===========" + key);
		logger.debug("mediaId=======" + mediaId);

		String token = wechatService.getAccessToken(appInfo);

		filePath = WeixinMediaUtils.downloadMedia(token, DownloadFileType.IMAGE, mediaId, filePath);
		if (StringUtils.isNotEmpty(filePath)) {
			File file = new File(filePath);
			fileUrl += file.getName();

			Map<String, Object> returns = new HashMap<String, Object>();

			returns.put("success", true);
			returns.put("mediaId", mediaId);
			returns.put("fileUrl", fileUrl);
			renderJSON(returns);
		} else {

			Map<String, Object> returns = new HashMap<String, Object>();

			returns.put("success", false);
			returns.put("mediaId", mediaId);
			renderJSON(returns);
		}

		return "none";
	}
}