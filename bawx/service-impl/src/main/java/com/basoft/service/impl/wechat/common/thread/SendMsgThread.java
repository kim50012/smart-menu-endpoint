package com.basoft.service.impl.wechat.common.thread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.core.ware.wechat.domain.customservice.GetOnlineKflistReturn;
import com.basoft.core.ware.wechat.domain.customservice.OnlineKflist;
import com.basoft.core.ware.wechat.domain.mass.Media;
import com.basoft.core.ware.wechat.domain.mass.NewsMassDomain4Reply;
import com.basoft.core.ware.wechat.domain.material.MediaReturn;
import com.basoft.core.ware.wechat.util.PropertiesUtils;
import com.basoft.core.ware.wechat.util.WeixinMediaUtils;
import com.basoft.core.ware.wechat.util.WeixinMessageUtils;
import com.basoft.service.dao.wechat.common.WechatMapper;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.shopWxNews.ShopWxNewService;
import com.basoft.service.entity.wechat.appinfo.AppInfo;

@SuppressWarnings("unused")
public class SendMsgThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private HttpServletRequest request;

	private WechatService wechatService;

	private WechatMapper wechatMapper;

	private AppInfo appInfo;

	private Map<String, String> map;
	
	private String eventType;

	private String keyword;
	
	private String webUploadPath;

	private ShopWxNewService shopWxNewService;

	/**
	 * 构造消息发送线程
	 * 
	 * @param request
	 * @param wechatService
	 * @param wechatMapper
	 * @param appInfo
	 * @param map
	 * @param eventType 消息类型： FOCUS-关注回复； SPAM-群发；CONTENT-包含两个（AUTO-自动回复；KEYWORD-关键字回复）。
	 * @param keyword
	 */
	public SendMsgThread(HttpServletRequest request, WechatService wechatService,ShopWxNewService shopWxNewService, WechatMapper wechatMapper, AppInfo appInfo, Map<String, String> map, String eventType,
			String keyword, String webUploadPath) {
		this.request = request;
		this.wechatService = wechatService;
		this.shopWxNewService = shopWxNewService;
		this.wechatMapper = wechatMapper;
		this.appInfo = appInfo;
		this.map = map;
		this.eventType = eventType;
		this.keyword = keyword;
		this.webUploadPath = webUploadPath;
	}

	private boolean whetherMakeOauth2Url(AppInfo appInfo, String url) {
		if (StringUtils.isNotBlank(url) && (url.startsWith("/") || url.startsWith(appInfo.getDomain()) && !url.endsWith(".html"))) {
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		try {
			logger.info("=================thread start=====================");
			// 事件类型
			String event = map.get("Event");
			// 发送方帐号
			String fromUserName = map.get("FromUserName");
			String eventKey = map.get("EventKey");// 事件KEY值，qrscene_为前缀，后面为二维码的参数值
			String ticket = map.get("Ticket");// Ticket
			logger.info("Event============" + event);
			logger.info("EventKey=========" + eventKey);
			logger.info("Ticket===========" + ticket);
			
			/**https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547*/
			List<Map<String, Object>> msgList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> textMsgList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> imageMsgList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> voiceMsgList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> videoMsgList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> musicMsgList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> newsMsgList = new ArrayList<Map<String, Object>>();
			
			if("FOCUS".equals(eventType)) {//关注回复
				msgList = wechatMapper.getFocusMessage(appInfo.getShopId());
			} else if("SPAM".equals(eventType)) {// 群发
				
			} else if("CONTENT".equals(eventType)){
				// 查询关键字
				msgList = wechatMapper.getKeyWordMessage(appInfo.getShopId(), keyword);
				if (msgList.isEmpty()) {
					// 如果查询不到关键字，则查询自动回复返回给用户
					msgList = wechatMapper.getAutoMessage(appInfo.getShopId());
				}
			}

			logger.info("eventType===========" + eventType);
			
			String token = wechatService.getAccessToken(appInfo);
			
			if (!msgList.isEmpty()) {
				for (Map<String, Object> item : msgList) {
					// 1:TEXT, 2:IMAGE, 3:VOICE, 4:VIDEO, 5:NEWS-废弃的类型定义
					// 3:TEXT, 2:IMAGE, 5:VOICE, 4:VIDEO, 1:NEWS-现有的类型定义

					String msg = null;
					try {
						// mysql数据库获取
						msg = item.get("SEND_MSG_BODY").toString();
					} catch (Exception e) {
					}

					logger.info("msg===========" + msg);
					
//					String[] callCenterMsg = msg.split(",");
					
					String callcenterId = "";
					try {
						callcenterId = item.get("CALLCENTER_ID").toString()+"";
					} catch (Error e) {
						e.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					logger.info("callcenterId.isEmpty()===========" + callcenterId.isEmpty());
					
//					if ("CALLCENTER".equals(callCenterMsg[0])) {	// by dikim on 20191231
					if (!callcenterId.isEmpty()) {

						boolean assignFail = true;
						List<OnlineKflist> accountList_online;
						accountList_online = WeixinMessageUtils.getonlinekflist(token).getKf_online_list();// 获取在线多客服坐席
						
						if(accountList_online.size() > 0){//判断是否所有坐席 OFFLINE
							
							for(int j=0;j<accountList_online.size();j++){

								logger.info("accountList_online.get(j).getKf_account()===========" + accountList_online.get(j).getKf_account());
								
								if(accountList_online.get(j).getKf_account().equals(callcenterId)){//判断该关键词所属坐席在线状态

									//发送给客户"连接客服通知"
									WeixinMessageUtils.sendTextMsg(token, fromUserName, "已与客服连接，请输入咨询事项。");
									WeixinMessageUtils.createKfSession(token, fromUserName, callcenterId, null);
									assignFail = false;
 									break;
			   					}
 							}
						}
						
						if (assignFail) {
							item.put("SEND_MSG_BODY", msg);
							textMsgList.add(item);
						}
						
						/*
						boolean assignFail = true;
						List<OnlineKflist> accountList_online;
						accountList_online = WeixinMessageUtils.getonlinekflist(token).getKf_online_list();// 获取在线多客服坐席
						
						if(accountList_online.size() > 0){//判断是否所有坐席 OFFLINE
							
							for(int j=0;j<accountList_online.size();j++){
								if(accountList_online.get(j).getKf_account().equals(callCenterMsg[1])){//判断该关键词所属坐席在线状态

									//发送给客户"连接客服通知"
									WeixinMessageUtils.sendTextMsg(token, fromUserName, "已与客服连接，请输入咨询事项。");
									
									WeixinMessageUtils.createKfSession(token, fromUserName, callCenterMsg[1], null);
									assignFail = false;
 									break;
			   					}
 							}
						}
						
						if (assignFail) {
							item.put("SEND_MSG_BODY", callCenterMsg[2]);
							textMsgList.add(item);
						}
						*/
						
					}
					else {

						// 扩展为支持oracle数据库
						Integer type = null;
						try {
							// mysql数据库获取
							type = (Integer) item.get("SEND_FILE_TYPE");
						} catch (Exception e) {
							// oracle数据库获取
							BigDecimal type1 = (BigDecimal) item.get("SEND_FILE_TYPE");
							type = type1.intValue();
						}

						logger.info("SEND_FILE_TYPE===========" + item.get("SEND_FILE_TYPE"));
						
						if (type == 3) {
							textMsgList.add(item);
						} else if (type == 2) {
							imageMsgList.add(item);
						} else if (type == 5) {
							voiceMsgList.add(item);
						} else if (type == 4) {
							videoMsgList.add(item);
						} else if (type == 6) {
							musicMsgList.add(item);
						} else if (type == 1) {
							newsMsgList.add(item);
						}
						
					}
				}
			}
			
			// 挨个儿发送消息:回复文本消息-OK
			if(!textMsgList.isEmpty()){
				for (Map<String, Object> item : textMsgList) {
					String content = (String) item.get("SEND_MSG_BODY");
					logger.info("===================content==================");
					logger.info(content);
					logger.info("===================content==================");
					WeixinMessageUtils.sendTextMsg(token, fromUserName, content);
				}
			}
			
			// 回复图片消息-OK
			if(!imageMsgList.isEmpty()){
				// String uploadBaseDir = PropertiesUtils.getUploadBaseDir(); 20180601-m110
				for (Map<String, Object> item : imageMsgList) {
					String mediaId = (String) item.get("MATERIAL_FILE_WX_ID");
					logger.info("===================mediaId==================>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + mediaId);
					
					// 图片media id不会为空，把重新上传逻辑注释掉  20180601-m110
					/*if(StringUtils.isBlank(mediaId)){
						String picUrl = (String) item.get("PIC_URL");
						Long fileId = (Long) item.get("FILE_ID");
						MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, "image", uploadBaseDir + picUrl);
						mediaId = mediaReturn.getMedia_id();
						wechatMapper.updateMediaId(fileId, mediaId);
					}*/
					
					WeixinMessageUtils.sendImageMsg(token, fromUserName, mediaId);
				}
			}
			
			//回复语音消息
			if(!voiceMsgList.isEmpty()){
				for (Map<String, Object> item : voiceMsgList) {
				}
			}
			
			// 回复视频消息
			if(!videoMsgList.isEmpty()){
				String uploadBaseDir = webUploadPath;
				for (Map<String, Object> item : videoMsgList) {
					// String thumbMediaId = (String) item.get("THUMB_MEDIA_ID");
					String thumbMediaId = "";
					String mediaId = (String) item.get("MEDIA_ID");
					String title 	= (String) item.get("MSG_TITLE");
					String description = title;
					// String description = (String) item.get("CONTENT");
					if(StringUtils.isBlank(mediaId)){
						String videoUrl = (String) item.get("PIC_URL");
						Long fileId = (Long) item.get("FILE_ID");
						mediaId = WeixinMediaUtils.addVideoMaterial(token, uploadBaseDir + videoUrl, title, description);
						wechatMapper.updateMediaId(fileId, mediaId);
					}
					// 回复视频文件
					WeixinMessageUtils.sendVideoMsg(token, fromUserName, mediaId, thumbMediaId, title, description);
				}
			}
			
			// 回复音乐消息
			if(!musicMsgList.isEmpty()){
				String uploadBaseDir = PropertiesUtils.getUploadBaseDir();
				for (Map<String, Object> item : musicMsgList) {
					String title 	= (String) item.get("MSG_TITLE");
					// String description = (String) item.get("DESCRIPTION");
					String description = title;
					Integer fileId 	= (Integer) item.get("FILE_ID");
					String musicUrl 	= appInfo.getDomain() + (String) item.get("PIC_URL");
					String hqMusicUrl 	= appInfo.getDomain() + (String) item.get("PIC_URL");
					
					String thumbFileUrl 	=  (String) item.get("LINK_URL");
					MediaReturn mediaReturn = WeixinMediaUtils.uploadMedia(token, "thumb", uploadBaseDir + thumbFileUrl);
					String thumbMediaId = mediaReturn.getThumb_media_id();
					
					// 回复音乐文件
					WeixinMessageUtils.sendMusicMsg(token, fromUserName, title, description, musicUrl, hqMusicUrl, thumbMediaId);
				}
			}
			
			// 回复图文消息-v1
			/*if(!newsMsgList.isEmpty()){
				List<Article> articles = new ArrayList<Article>();
				for (Map<String,Object> item : newsMsgList) {
					String msgTitle = (String)item.get("MSG_TITLE");
					String description = (String)item.get("CONTENT");
					String picurl = appInfo.getDomain() + (String)item.get("PIC_URL");
					String url =  (String)item.get("LINK_URL");
					
					logger.info("msgTitle=====" + msgTitle);
					logger.info("description==" + description);
					logger.info("picurl=======" + picurl);
					logger.info("url==========" + url);
					
					if (whetherMakeOauth2Url(appInfo, url)) {
						com.basoft.core.ware.wechat.domain.AppInfo destAppInfo = new com.basoft.core.ware.wechat.domain.AppInfo();
						BeanUtils.copyProperties(destAppInfo, appInfo);
						url = Oauth2Utils.getNormalLinkUrl(destAppInfo, url);
					}
					
					Article article = new Article(msgTitle, description, picurl,url);
					articles.add(article);
				}
				WeixinMessageUtils.sendNewsMsg(token, fromUserName, articles);
			}*/
			
			// 回复图文消息-v2-OK-but error:Response content:{"errcode":40130,"errmsg":"invalid openid list size, at least two openid hint: [fZ.gSa0256ge20]"}
			/*if(!newsMsgList.isEmpty()){
				for (Map<String,Object> item : newsMsgList) {
					// 无论是关注回复，还是自动回复，还是关键回复，都可以获得对应图文消息微信端的media id
					String mediaId = (String)item.get("MATERIAL_FILE_WX_ID");
					logger.info("message response>>>>>>>>news message id>>>>>>=====" + mediaId);
					NewsMassDomain news = new NewsMassDomain();
		            news.setMpnews(new Media(mediaId));
		            List<String> usersList = new ArrayList<String>();
		            usersList.add(fromUserName);
		            news.setTouser(usersList);
		            WeixinMassMessageUtils.sendMass(token, news);
				}
			}*/
			
			// 回复图文消息-v2改版-OK
			if(!newsMsgList.isEmpty()){
				for (Map<String,Object> item : newsMsgList) {
					// 无论是关注回复，还是自动回复，还是关键回复，都可以获得对应图文消息微信端的media id
					String mediaId = (String)item.get("MATERIAL_FILE_WX_ID");
					logger.info("message response>>>>>>>>news message id>>>>>>=====" + mediaId);
					NewsMassDomain4Reply news = new NewsMassDomain4Reply();
		            news.setMpnews(new Media(mediaId));
		            news.setTouser(fromUserName);
		            // WeixinMassMessageUtils.sendMass(token, news);
		            WeixinMessageUtils.sendNewsMsg(token, news);
				}
			}

			// 回复图文消息-v3-OK-但是不行，因为这种方式是外链的图文消息，目前管理系统不支持这种外链的图文消息的编辑。
			/*if(!newsMsgList.isEmpty()){
				List<Article> articles = new ArrayList<Article>();
				for (Map<String, Object> newsItem : newsMsgList) {
					Long msgId = (Long) newsItem.get("MATERIAL_FILE_ID");
					// 查询图文消息
					if (msgId != null && msgId > 0) {
						List<Map<String, Object>> newsList = shopWxNewService.selectNewsListByMsgId(msgId, appInfo.getShopId());
						if (newsList != null)
							logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + newsList.size() + ">>>>>>>>>>>>>>>>>>>>>>>>>>");
							for (int i = 0; i < newsList.size(); i++) {
								Map<String, Object> item = newsList.get(i);
								String mediaId = (String)item.get("MEDIA_ID");
								Long fileId = (Long)item.get("MFILE_ID");
								String fileUrl = (String)item.get("FILE_URL");
								String author = (String)item.get("MAUTHOR");
								String title = (String) item.get("MTITLE");
								String sourceUrl = (String) item.get("MSOURCE_URL");
								String content = (String) item.get("MCONTENTWECHAT");
								Integer mshowCoverPic = (Integer) item.get("MSHOW_COVER_PIC");
								logger.info("mshowCoverPic=" + mshowCoverPic);
								logger.info("sourceUrl=" + sourceUrl);
								logger.info("title=" + title);
								
								Article a = new Article();
					            a.setThumb_media_id(mediaId);
					            a.setAuthor(author);
					            a.setTitle(title);
					            a.setContent_source_url(sourceUrl);
					            a.setContent(content);
					            a.setDigest(mdigest);
					            a.setShow_cover_pic(mshowCoverPic + "");
					            list.add(a);
								Article a = new Article();
								a.setDescription(content);
								a.setPicurl(mshowCoverPic + "");
								a.setTitle(title);
								a.setUrl(sourceUrl);
								articles.add(a);
							}
					}
				}
				WeixinMessageUtils.sendNewsMsg(token, fromUserName, articles);
			}*/
			
			//这里暂时hardcoding回复文本内容
			/*if(StringUtils.isNotBlank(scene_id)){
				WeixinMessageUtils.sendTextMsg(token, fromUserName, msg);
			}*/
			logger.info("=================thread end=====================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
