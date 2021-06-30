package com.basoft.service.impl.wechat.common.thread;

import com.basoft.core.ware.wechat.domain.material.MediaReturn;
import com.basoft.core.ware.wechat.domain.msg.Article;
import com.basoft.core.ware.wechat.domain.template.DataItem;
import com.basoft.core.ware.wechat.util.Constants;
import com.basoft.core.ware.wechat.util.PropertiesUtils;
import com.basoft.core.ware.wechat.util.WeixinMediaUtils;
import com.basoft.core.ware.wechat.util.WeixinMessageUtils;
import com.basoft.core.ware.wechat.util.WeixinTemplateUtils;
import com.basoft.service.dao.wechat.common.WechatMapper;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class QRCodeEventThread implements Runnable {
	private HttpServletRequest request;

	private WechatService wechatService;

	private WechatMapper wechatMapper;

	private AppInfo appInfo;

	private String keyword;
	
	private static final String UTF_8 = "utf-8";

	private static final String PATH = "/w/t.htm";

	private static final String DEFAUT_COLOR = "#173177";

	private static final String RED_COLOR = "#df1414";

	private static final String BLUE_COLOR = "#2714df";

	private String deployDomainPrefix;
	
	public static final class Params {
		public static final String SYSTEM_ID = "k";
		public static final String TARGET_URL = "tu";
	}
	
	Map<String, String> map;

	public QRCodeEventThread(HttpServletRequest request
			, WechatService wechatService
			, WechatMapper wechatMapper
			, AppInfo appInfo
			, Map<String, String> map
			, String keyword
			, String deployDomainPrefix) {
		this.request = request;
		this.wechatService = wechatService;
		this.wechatMapper = wechatMapper;
		this.appInfo = appInfo;
		this.map = map;
		this.keyword = keyword;
		this.deployDomainPrefix = deployDomainPrefix;
	}

	private boolean whetherMakeOauth2Url(AppInfo appInfo, String url) {
		if (StringUtils.isNotBlank(url) && (url.startsWith("/") || url.startsWith(appInfo.getDomain()) && !url.endsWith(".html"))) {
			return true;
		}
		return false;
	}

	private String getNormalLinkUrl(AppInfo appInfo, String targetUrl) {
		try {
			if (StringUtils.isNotEmpty(targetUrl)) {
				String encodedTargetUrl = URLEncoder.encode(targetUrl, UTF_8);
//				String redirectUrl = appInfo.getDomain() + PATH  
//								   + "?" + Params.SYSTEM_ID 	+ "=" + appInfo.getSysId() 
//								   + "&" + Params.TARGET_URL 	+ "=" + encodedTargetUrl;
//				String encodedRedirectUrl = URLEncoder.encode(redirectUrl, UTF_8);
				StringBuffer sb = new StringBuffer();
				sb.append("https://open.weixin.qq.com/connect/oauth2/authorize")
					.append("?appid=").append(appInfo.getAppId())
					.append("&redirect_uri=").append(encodedTargetUrl)
					.append("&response_type=").append("code")
					.append("&scope=").append("snsapi_base")
					.append("&state=").append('n')
					.append("#wechat_redirect");
				return sb.toString();
			} else {
				return targetUrl;
			}
		} catch (Exception e) {
			return targetUrl;
		}
	}


	@Override
	public void run() {
		try {
			log.info("=================thread start=====================");
			log.info("=================deployDomainPrefix::::{}=====================", deployDomainPrefix);
			// TODO Auto-generated method stub
			// 浜嬩欢绫诲瀷
			String event = map.get("Event");
			// 鍙戦�佹柟甯愬彿
			String fromUserName = map.get("FromUserName");
			String eventKey = map.get("EventKey");// 浜嬩欢KEY鍊硷紝qrscene_涓哄墠缂�锛屽悗闈负浜岀淮鐮佺殑鍙傛暟鍊�
			String ticket = map.get("Ticket");// Ticket 
			String scene_id = "";
			String msg = "";
			String token = "";
			
			log.info("Event============" + event);
			log.info("EventKey=========" + eventKey);
			log.info("Ticket===========" + ticket);
			
			if (Constants.Event.SUBSCRIBE.equals(event)) {
				if(StringUtils.isNotBlank(eventKey)){
					scene_id = eventKey.substring(8); //QRCode 鍦烘櫙鍊糏D
					msg = "棣栨鍏虫敞 scene_id:" + scene_id;
					Thread.sleep(2000);
					token = wechatService.getAccessToken(appInfo);
				}
			}else if (Constants.Event.SCAN.equals(event)) {
				scene_id = eventKey; //QRCode 鍦烘櫙鍊糏D
				msg = "宸插叧娉ㄧ敤鎴� scene_id:" + scene_id;
				token = wechatService.getAccessToken(appInfo);
			}
			
			keyword = "QR_" + scene_id;
			
			//鏍规嵁scene_id鏉ュ垽鏂洖澶嶄粈涔堟牱鐨勫唴瀹癸紙浠庢暟鎹簱杩斿洖鍥炲鐨勭被鍨嬶級
			log.info("scene_id=========" + scene_id);
			log.info("msg==============" + msg);
			log.info("toUser===========" + fromUserName);
			log.info("token===========" + token);
			log.info("scene_id===========" +  scene_id);
			log.info("keyword===========" +  keyword);
 
			
			try {
				// log.info("<><><><>scan<><><>");
				// wechatMapper.insertQRCodeScanedResult(appInfo.getShopId(),fromUserName,Integer.valueOf(scene_id));
				// log.info("<><><><>scan<><><>");
			} catch (Exception e) {
				// TODO: handle exception 
			}
			
			//keyword -> scene_id by dikim
			List<Map<String,Object>> msgList = wechatMapper.getReturnMessage(appInfo.getSysId(), scene_id ,deployDomainPrefix);
			
			List<Map<String,Object>> textMsgList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> imageMsgList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> voiceMsgList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> videoMsgList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> musicMsgList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> newsMsgList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> tempMsgList = new ArrayList<Map<String,Object>>();	// by dikim template message
			
			if(!msgList.isEmpty()){
				for (Map<String,Object> item : msgList) {
					String type = (String) item.get("MSG_TYPE");
					if("text".equalsIgnoreCase(type)){
						textMsgList.add(item);
					}else if("image".equalsIgnoreCase(type)){
						imageMsgList.add(item);
					}else if("voice".equalsIgnoreCase(type)){
						voiceMsgList.add(item);
					}else if("video".equalsIgnoreCase(type)){
						videoMsgList.add(item);
					}else if("music".equalsIgnoreCase(type)){
						musicMsgList.add(item);
					}else if("news".equalsIgnoreCase(type)){
						newsMsgList.add(item);
					}else if("tempMsg".equalsIgnoreCase(type)){
						tempMsgList.add(item);
					}
				}
			}
			
			//template message
			if(!tempMsgList.isEmpty()){
				log.info("QRCodeEventThread[RUN]扫码回复信息列表长度::::" + tempMsgList.size());
				for (Map<String, Object> item : tempMsgList) {
					log.info("QRCodeEventThread[RUN]回复信息详情::::" + item);
					String content = "";
					String templateId = "";
					
					String first = (String)item.get("FIRST");
					String keyword1 = (String)item.get("KEYWORD1");
					String keyword2 = (String)item.get("KEYWORD2");
					String keyword3 = (String)item.get("KEYWORD3");
					String addr = (String)item.get("DETAIL_ADDR");
					String remark = (String)item.get("REMARK");
					String url =  (String)item.get("LINK_URL");
					String status =  (String)item.get("STATUS");

					String er_first =  (String)item.get("ER_FIRST");
					String er_keyword1 =  (String)item.get("ER_KEYWORD1");
					String er_keyword2 =  (String)item.get("ER_KEYWORD2");
					String er_remark =  (String)item.get("ER_REMARK");
					
					url = getNormalLinkUrl(appInfo, url);
					log.info("first=====" + first);
					log.info("keyword1==" + keyword1);
					log.info("keyword2==" + keyword2);
					log.info("keyword3==" + keyword3);
					log.info("addr======" + addr);
					log.info("remark====" + remark);
					log.info("url=======" + url);
					log.info("status====" + status);



					// Map<String, DataItem> data = new HashMap<String, DataItem>();
					if (status.equals("NORMAL")) {
						/*data.put("first", new DataItem(first, DEFAUT_COLOR));
						data.put("keyword1", new DataItem(keyword1, BLUE_COLOR));
						data.put("keyword2", new DataItem(keyword2, RED_COLOR));
						data.put("keyword3", new DataItem(keyword3, DEFAUT_COLOR));
						data.put("remark", new DataItem(content, DEFAUT_COLOR));
						templateId = "3FvGFSLV9ISwWqp6Ic0NW4QfwLY471_7ly1r9MC4CWY";*/
						content = "扫码成功通知";
						content = content + "\n-----------------------------";
						//content = content + "\n\n" + first;
						content = content + "\n商户名称: " + keyword1;

						// 桌台名称优化调整
						//if("0层0号桌".equals(keyword2) || keyword2.contains("0号桌")){//类似1层10号桌发送出现问题
						boolean sendSimple = false;
						String keyword2Str = StringUtils.substringBetween(keyword2,"层","桌");
						if(keyword2Str != null && keyword2Str.length() == 2){
							sendSimple = true;
						}
						if("0层0号桌".equals(keyword2) || (keyword2.contains("0号桌") && sendSimple)){
							// 不显示桌台
						} else {
							content = content + "\n桌台名称: " + keyword2;
						}

						content = content + "\n扫码时间: " + keyword3;

						//content = content + "\n" + addr;
						content = content + "\n\n<a href='" + url + "'>" + remark + "</a>";
						content = content + "\n";
						
						
						WeixinMessageUtils.sendTextMsg(token, fromUserName, content);
					}
					else {
						Map<String, DataItem> data = new HashMap<String, DataItem>();
						data.put("first", new DataItem(er_first, DEFAUT_COLOR));
						data.put("keyword1", new DataItem(er_keyword1, DEFAUT_COLOR));
						data.put("keyword2", new DataItem(er_keyword2, RED_COLOR));
						data.put("remark", new DataItem(er_remark, DEFAUT_COLOR));	
						templateId = "KFhgwbRDJxHikEWvm0TzcBX9Rj2XhdbbOW4qLPLDDkQ";
						url = "";
						WeixinTemplateUtils.sendTemplateMessage(token, fromUserName, templateId, url, data);
					}
				}
			}
			
			//鎸ㄤ釜鍎垮彂閫佹秷鎭�
			if(!textMsgList.isEmpty()){// 鍥炲鏂囨湰娑堟伅
				for (Map<String, Object> item : textMsgList) {
					String content = "";
					
					String msgTitle = (String)item.get("MSG_TITLE");
					String detailAddr = (String)item.get("DETAIL_ADDR");
					String tag = (String)item.get("TAG");
					String number = (String)item.get("NUMBER");
					String url =  (String)item.get("LINK_URL");
					
					url = getNormalLinkUrl(appInfo, url);
					
					log.info("msgTitle=====" + msgTitle);
					log.info("detailAddr==" + detailAddr);
					log.info("tag=======" + tag);
					log.info("number=======" + number);
					log.info("url==========" + url);

					content = content + msgTitle;
					content = content + "\n" + detailAddr;
					content = content + "\nFloor : " + tag;
					content = content + "\nTable : " + number;
					content = content + "\n\n" + url;
					
					WeixinMessageUtils.sendTextMsg(token, fromUserName, content);
				}
			}
			
			if(!imageMsgList.isEmpty()){//鍥炲鍥剧墖娑堟伅
				String uploadBaseDir = PropertiesUtils.getUploadBaseDir();
				for (Map<String, Object> item : imageMsgList) {
					
					String mediaId = (String) item.get("MEDIA_ID");
					
					if(StringUtils.isBlank(mediaId)){
						String picUrl = (String) item.get("PIC_URL");
						Long fileId = (Long) item.get("FILE_ID");
						
						MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, "image", uploadBaseDir + picUrl);
						mediaId = mediaReturn.getMedia_id();
						wechatMapper.updateMediaId(fileId, mediaId);
					}
					
					WeixinMessageUtils.sendImageMsg(token, fromUserName, mediaId);
				}
			}
			if(!voiceMsgList.isEmpty()){//鍥炲璇煶娑堟伅
//				for (Map<String, Object> item : voiceMsgList) {
//				}
			}
			if(!videoMsgList.isEmpty()){//鍥炲瑙嗛娑堟伅
				String uploadBaseDir = PropertiesUtils.getUploadBaseDir();
				
				for (Map<String, Object> item : videoMsgList) {
//					String thumbMediaId = (String) item.get("THUMB_MEDIA_ID");
					String thumbMediaId = "";
					String mediaId = (String) item.get("MEDIA_ID");
					String title 	= (String) item.get("MSG_TITLE");
					String description = title;
//					String description = (String) item.get("CONTENT");
					
					if(StringUtils.isBlank(mediaId)){
						String videoUrl = (String) item.get("PIC_URL");
						Long fileId = (Long) item.get("FILE_ID");
						mediaId = WeixinMediaUtils.addVideoMaterial(token, uploadBaseDir + videoUrl, title, description);
						wechatMapper.updateMediaId(fileId, mediaId);
					}
					//TODO 鍥炲瑙嗛鏂囦欢
					WeixinMessageUtils.sendVideoMsg(token, fromUserName, mediaId, thumbMediaId, title, description);
					
				}
			}
			if(!musicMsgList.isEmpty()){//鍥炲闊充箰娑堟伅
				String uploadBaseDir = PropertiesUtils.getUploadBaseDir();
				for (Map<String, Object> item : musicMsgList) {
					String title 	= (String) item.get("MSG_TITLE");
//					String description = (String) item.get("DESCRIPTION");
					String description = title;
					Integer fileId 	= (Integer) item.get("FILE_ID");
					String musicUrl 	= appInfo.getDomain() + (String) item.get("PIC_URL");
					String hqMusicUrl 	= appInfo.getDomain() + (String) item.get("PIC_URL");
					
					String thumbFileUrl 	= (String) item.get("LINK_URL");
					MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, "thumb", uploadBaseDir + thumbFileUrl);
					String thumbMediaId = mediaReturn.getThumb_media_id();
					
					//TODO 鍥炲闊充箰鏂囦欢
					WeixinMessageUtils.sendMusicMsg(token, fromUserName, title, description, musicUrl, hqMusicUrl, thumbMediaId);
				}
			}
			
			if(!newsMsgList.isEmpty()){//鍥炲鍥炬枃娑堟伅
					
				List<Article> articles = new ArrayList<Article>();
				for (Map<String,Object> item : newsMsgList) {
					
					String msgTitle = (String)item.get("MSG_TITLE");
					String description = (String)item.get("CONTENT");
					String picurl = (String)item.get("PIC_URL");
					String url =  (String)item.get("LINK_URL");
					
					
					log.info("msgTitle=====" + msgTitle);
					log.info("description==" + description);
					log.info("picurl=======" + picurl);
					log.info("url==========" + url);

					url = getNormalLinkUrl(appInfo, url);

					log.info("TODOurl==========" + url);
					
					Article article = new Article(msgTitle, description, picurl, url);
					
					articles.add(article);
				}
				
				WeixinMessageUtils.sendNewsMsg(token, fromUserName, articles);
				 
			} 
			
//			//杩欓噷鏆傛椂hardcoding鍥炲鏂囨湰鍐呭
//			if(StringUtils.isNotBlank(scene_id)){
//				WeixinMessageUtils.sendTextMsg(token, fromUserName, msg);
//			}
			log.info("=================thread end=====================");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
}
