package com.basoft.service.impl.wechat.common;

import com.basoft.core.ware.common.aes.WXBizMsgCrypt;
import com.basoft.core.ware.common.framework.utilities.LogUtils;
import com.basoft.core.ware.wechat.domain.ExecutionLog;
import com.basoft.core.ware.wechat.domain.msg.EventLocation;
import com.basoft.core.ware.wechat.domain.msg.Image;
import com.basoft.core.ware.wechat.domain.msg.Link;
import com.basoft.core.ware.wechat.domain.msg.Location;
import com.basoft.core.ware.wechat.domain.msg.Text;
import com.basoft.core.ware.wechat.domain.msg.Video;
import com.basoft.core.ware.wechat.domain.msg.Voice;
import com.basoft.core.ware.wechat.domain.user.WXUser;
import com.basoft.core.ware.wechat.util.Constants;
import com.basoft.core.ware.wechat.util.XmlUtils;
import com.basoft.service.dao.wechat.common.WechatMapper;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.common.WechatSignalService;
import com.basoft.service.definition.wechat.shopWxNews.ShopWxNewService;
import com.basoft.service.definition.wechat.user.WeixinUserService;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import com.basoft.service.impl.wechat.common.thread.AgentQRCodeEventThread;
import com.basoft.service.impl.wechat.common.thread.AgentQRCodeSubscribeEventThread;
import com.basoft.service.impl.wechat.common.thread.DownloadImageMsgThread;
import com.basoft.service.impl.wechat.common.thread.ExecutionLoggingThread;
import com.basoft.service.impl.wechat.common.thread.MenuClickThread;
import com.basoft.service.impl.wechat.common.thread.QRCodeEventThread;
import com.basoft.service.impl.wechat.common.thread.SendMsgThread;
import com.basoft.service.util.ServletRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * WechatSignalProcessor
 * 
 * @author basoft
 */
@Slf4j
@Service
@Configuration
public class WechatSignalServiceImpl implements WechatSignalService{
	@Value("${deploy.domain.prefix}")
	private String deployDomainPrefix;

	@Autowired
	private WechatService wechatService;

	@Autowired
	private WeixinUserService weixinUserService;
	
	@Autowired
	private WechatMapper wechatMapper;
	
	@Autowired
	private IdService idService;

	@Autowired
	private ShopWxNewService shopWxNewService;
	
	@Value("${basoft.web.upload-path}")
    private String webUploadPath;

	/**
	* ?????????????????????????????????????????????????????? 
	* ??????????????????????????????????????????????????????????????????????????????success??????????????????.
	* ???????????????????????????????????????????????????????????????????????????????????????????????????API?????????????????????.
	* ?????????????????????????????????
	* @param request
	* @param appInfo
	* @return
	* @throws Exception
	*/
	public String processingAndReplyMessage(HttpServletRequest request,AppInfo appInfo) throws Exception{
		
		Date startTime =new Date();
		/**
		 * ????????????????????????????????????????????????????????????????????????????????????
		 * ??????????????????????????????????????????????????????????????????????????????
		 * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		 * 1??????????????????????????????????????????0???????????????????????????XML????????????content????????????????????????
		 * 2???????????????success
		 */
		String returnMsg = "";
		
		String postData = ServletRequestUtils.readStreamParameter(request);
		log.info("===============================postData===============================");
		log.info(postData);
		log.info("===============================postData===============================");
		if (StringUtils.isEmpty(postData)) {
			log.error("<<WXPR00001>>");
			throw new Exception("postData is null!!!!!!!");
		}
		
		String signature  	= request.getParameter("signature"); //?????????????????????signature???????????????????????????token?????????????????????timestamp?????????nonce?????????
		String timestamp  	= request.getParameter("timestamp"); //?????????
		String nonce  		= request.getParameter("nonce");	 //?????????
		String encrypt_type = request.getParameter("encrypt_type");//???????????? raw:????????? ,aes:aes??????
		String msg_signature= request.getParameter("msg_signature");//?????????????????????
		
		log.info(LogUtils.format("signature") 	+ signature);
		log.info(LogUtils.format("timestamp") 	+ timestamp);
		log.info(LogUtils.format("nonce") 		+ nonce);
		log.info(LogUtils.format("encrypt_type") + encrypt_type);
		log.info(LogUtils.format("msg_signature")+ msg_signature);
		log.info(LogUtils.format("postData") 	+ postData);
		
		
		String time 		= "";
		String ToUserName 	= ""; // ??????????????????
		String FromUserName = ""; // ????????????????????????OpenID???
		String CreateTime 	= ""; // ?????????????????? ????????????
		String MsgType 		= ""; // ????????????
		String Event 		= "";// ????????????
		String EventKey  	= "";// ??????KEY???
		String MsgId	 	= "";// ??????id???64?????????
		
		Map<String,String> requestMap = null;
		WXBizMsgCrypt pc = null;
		
		if(StringUtils.isEmpty(encrypt_type)){
			log.error("<<WXPR00002>>");
			log.info("**************** ???????????? ==> Server ********************");
			log.info("postData=\n" + postData);
			log.info("**************** ???????????? ==> Server end ****************");
			
			// ??????parseXml????????????xml?????????????????????map???
			requestMap = XmlUtils.parseXml2Map(postData);
		}else if("aes".equals(encrypt_type)){//?????????????????? ??? ??????
			//????????????
			pc = new WXBizMsgCrypt(appInfo.getToken(), appInfo.getEncordingAesKey(), appInfo.getAppId());
			String decryptedData =  pc.decryptMsg(msg_signature, timestamp, nonce, postData);
			log.info("**************** ???????????? ==> Server ********************");
			log.info("decryptedData=\n" + decryptedData);
			log.info("**************** ???????????? ==> Server end ****************");
			
			// ??????parseXml????????????xml?????????????????????map???
			requestMap = XmlUtils.parseXml2Map(decryptedData);
		}
			

		time = System.currentTimeMillis() + "";
		ToUserName 	 = requestMap.get("ToUserName");	// ??????????????????
		FromUserName = requestMap.get("FromUserName");	// ????????????????????????OpenID???
		CreateTime 	 = requestMap.get("CreateTime");	// ?????????????????? ????????????
		MsgType 	 = requestMap.get("MsgType");		// ????????????

		log.info(LogUtils.format("ToUserName") 	+ ToUserName);
		log.info(LogUtils.format("FromUserName")	+ FromUserName);
		log.info(LogUtils.format("CreateTime")  	+ CreateTime);
		log.info(LogUtils.format("MsgType") 		+ MsgType);

		// ????????????
		if (Constants.MsgType.EVENT.equals(MsgType)) {
			Event = requestMap.get("Event");// ????????????
			log.info(LogUtils.format("Event") + Event);

			// ???????????? OR ??????????????????????????????(???????????????????????????????????????????????????)
			if (Constants.Event.SUBSCRIBE.equals(Event)) {
				log.info("???EVENT SUBSCRIBE???>>>>>????????????????????????......");

				// ????????????KEY???????????????32??????????????????????????????????????????????????????scene_id
				EventKey = requestMap.get("EventKey");
				log.info("???EVENT SUBSCRIBE????????????????????????????????????>>>>>>{}", EventKey);

				// 1?????????????????????
				Thread msgThread = new Thread(new SendMsgThread(request, wechatService,shopWxNewService,wechatMapper, appInfo, requestMap,"FOCUS","",webUploadPath));
				msgThread.start();

				log.info("???EVENT SUBSCRIBE????????????????????????????????????>>>>>>{}", EventKey);

				// 2????????????????????????????????????????????????????????????
				// ????????????????????????????????????
				boolean isOldUser = false;
				if (StringUtils.isNotBlank(EventKey)) {
					// 3.1 ??????CA???????????????????????????
					if(EventKey.contains("agent_")){
						//??????????????????????????????????????????????????????
						List<Map<String, Object>> wxUserList = wechatMapper.getWxUserByOpenid(FromUserName);
						// ?????????
						if (wxUserList != null && wxUserList.size() > 0) {
							isOldUser = true;
						}

						// ??????????????????????????????????????????
						try {
							Thread thread = new Thread(new AgentQRCodeSubscribeEventThread(request, wechatService, wechatMapper, appInfo, requestMap, EventKey, isOldUser));
							thread.start();
						} catch (Exception e) {
							log.error("????????????????????????????????????????????????:" + e.getMessage(), e);
						}
					}
					// 3.2 ???????????????????????????
					else {
						try {
							Thread thread = new Thread(new QRCodeEventThread(request
									, wechatService
									, wechatMapper
									, appInfo
									, requestMap
									, EventKey
									, deployDomainPrefix));
							thread.start();
						} catch (Exception e) {
							log.error("??????????????????????????????????????????:" + e.getMessage(), e);
						}
					}
				}

				// 3???????????????????????????
				log.info("???EVENT SUBSCRIBE???>>>>>????????????????????????");
				// String token = wechatService.getAccessToken(appInfo);
				WXUser user = new WXUser();
				user.setSysId(appInfo.getSysId());
				user.setOpenid(FromUserName);
				weixinUserService.userSubscribe(wechatService.getAccessToken(appInfo), user);

				//????????????????????? -ended
				/*if(appInfo.getSysId().equals("XB4EPA9I5XSOPQHBE69JILV3AMQJY8VN") 
						|| appInfo.getSysId().equals("ZPC80I3JVOCTII09AFHKFY10CCYWNJRH")
						|| appInfo.getSysId().equals("8D6ED58C805242C8BDB129616163CB04")){
					try {
						log.info("000000000000000004:User SUBSCRIBE");
						log.info("thread0000000011: send read park>>>>>>>>>>>>>>>>>" );
						
						Thread thread = new Thread(new ShinSeGaeEvent10ReadParkThread(request, weixinService,appInfo, FromUserName));
						thread.start();
					} catch (Exception e) {
						// TODO: handle exception
						log.error("thread0000000011:" + e.getMessage());
					}
				}*/
				//ShinSeGaeEvent10ReadParkThread
				//????????????????????? END
					
				// ???????????????
				//returnMsg = WeixinResponseMessageUtils.genResponseMsg(request,appInfo,requestMap,weixinMessageDao.getSubscribeMessage(appInfo));
			}else if (Constants.Event.UNSUBSCRIBE.equals(Event)) {// ??????????????????
				WXUser user = new WXUser();
				user.setSysId(appInfo.getSysId());
				user.setOpenid(FromUserName);
				weixinUserService.userUnsubscribe(user);
			}else if(Constants.Event.SCAN.equals(Event)) {// ??????????????????????????????(?????????????????????????????????)
				EventKey = requestMap.get("EventKey");// ??????KEY???????????????32??????????????????????????????????????????????????????scene_id
				String Ticket = requestMap.get("Ticket");// Ticket  ????????????ticket?????????????????????????????????
				
				log.info(LogUtils.format("EventKey") + EventKey);
				log.info(LogUtils.format("Ticket") 	+ Ticket);
				
				try {
					// ????????????????????????
					if(EventKey.startsWith("agent_")){
						Thread thread = new Thread(new AgentQRCodeEventThread(request, wechatService, wechatMapper, appInfo, requestMap, EventKey));
						thread.start();
					}
					// ?????????StoreTable??????????????????
					else {
						Thread thread = new Thread(new QRCodeEventThread(request
								, wechatService
								, wechatMapper
								, appInfo
								, requestMap
								, EventKey
								, deployDomainPrefix));
						thread.start();
					}
				} catch (Exception e) {
					// TODO: handle exception
					log.error("thread11111111111:" + e.getMessage());
				}
				 
			}else if(Constants.Event.LOCATION.equals(Event)) {// ????????????????????????
				String Latitude  = requestMap.get("Latitude");  //??????????????????
				String Longitude = requestMap.get("Longitude"); //??????????????????
				String Precision = requestMap.get("Precision"); //??????????????????
				
				log.info(LogUtils.format("Latitude") 	+ Latitude);
				log.info(LogUtils.format("Longitude") 	+ Longitude);
				log.info(LogUtils.format("Precision") 	+ Precision);
				
			}else if(Constants.Event.CLICK.equals(Event)) {// ?????????????????????(??????????????????????????????????????????)
				EventKey = requestMap.get("EventKey"); // ??????KEY?????????????????????????????????KEY?????????
				log.info(LogUtils.format("EventKey") + EventKey);
				try {
					Thread thread = new Thread(new MenuClickThread(wechatService,appInfo, requestMap));
					thread.start();
				} catch (Exception e) {
					// TODO: handle exception
					log.error("thread33333333333:" + e.getMessage());
				}
				//???MenuClickThread????????????????????????????????????????????????????????????????????????
//				try {
//					Thread thread = new Thread(new MenuClickLoggingThread(appInfo,FromUserName,EventKey ,weixinService));
//					thread.start();
//				} catch (Exception e) {
//					// TODO: handle exception
//					log.error("thread33333333333:" + e.getMessage());
//				}
//				return WeixinResponseMessageUtils.genResponseMsg(request,appInfo,requestMap,weixinMessageDao.getClickEventReturnMessage(appInfo, Integer.valueOf(EventKey)));
			
			}else if(Constants.Event.VIEW.equals(Event)) {// ?????????????????????(??????????????????????????????????????????)
				EventKey = requestMap.get("EventKey");// ??????KEY?????????????????????URL
				log.info(LogUtils.format("EventKey") + EventKey);
			}else if(Constants.Event.SCANCODE_PUSH.equals(Event)) {// ?????????????????????-(???????????????)
				EventKey = requestMap.get("EventKey");
//					Element ScanCodeInfo = root.element("ScanCodeInfo");
//					String ScanType = ScanCodeInfo.elementTextTrim("ScanType");
//					String ScanResult = ScanCodeInfo.elementTextTrim("ScanResult");
				log.info(LogUtils.format("EventKey") + EventKey);
//					log.info("ScanType=========" + ScanType);
//					log.info("ScanResult=========" + ScanResult);
			}else if(Constants.Event.SCANCODE_WAITMSG.equals(Event)) {// ?????????????????????-(??????????????????????????????????????????????????????)
				EventKey = requestMap.get("EventKey");
//					Element ScanCodeInfo = root.element("ScanCodeInfo");
//					String ScanType = ScanCodeInfo.elementTextTrim("ScanType");
//					String ScanResult = ScanCodeInfo.elementTextTrim("ScanResult");
				log.info(LogUtils.format("EventKey") + EventKey);
//					log.info("ScanType=========" + ScanType);
//					log.info("ScanResult=========" + ScanResult);
			}else if(Constants.Event.PIC_SYSPHOTO.equals(Event) || Constants.Event.PIC_WEIXIN.equals(Event)
					|| Constants.Event.PIC_PHOTO_OR_ALBUM.equals(Event)) {// ?????????????????????-(????????????????????????,???????????????????????????,??????????????????????????????)
				EventKey = requestMap.get("EventKey");
				log.info(LogUtils.format("EventKey") + EventKey);
				// log.info("Count=========" + Count);
				// log.info("PicMd5Sum=========" + PicMd5Sum);

			}else if(Constants.Event.LOCATION_SELECT.equals(Event)) {// ?????????????????????(???????????????????????????)
				EventKey = requestMap.get("EventKey");
				// Element SendLocationInfo = root.element("SendLocationInfo");
				// String Location_X =
				// SendLocationInfo.elementTextTrim("Location_X");
				// String Location_Y =
				// SendLocationInfo.elementTextTrim("Location_Y");
				// String Scale = SendLocationInfo.elementTextTrim("Scale");
				// String Label = SendLocationInfo.elementTextTrim("Label");
				// String Poiname = SendLocationInfo.elementTextTrim("Poiname");

				log.info(LogUtils.format("EventKey") + EventKey);
				// log.info("Location_X=========" + Location_X);
				// log.info("Location_Y=========" + Location_Y);
				// log.info("Scale=========" + Scale);
				// log.info("Label=========" + Label);
				// log.info("Poiname=========" + Poiname);

			}else if(Constants.Event.KF_CREATE_SESSION.equals(Event)) {// ?????????????????????-???????????? 
				String KfAccount = requestMap.get("KfAccount"); 
				log.info(LogUtils.format("KfAccount") + KfAccount);
				// TODO: handle biz
			}else if(Constants.Event.KF_CLOSE_SESSION.equals(Event)) {//  ?????????????????????-????????????
				String KfAccount = requestMap.get("KfAccount"); 
				log.info(LogUtils.format("KfAccount") + KfAccount);
				// TODO: handle biz
			}else if(Constants.Event.KF_SWITCH_SESSION.equals(Event)) {//  ?????????????????????-????????????
				String FromKfAccount = requestMap.get("FromKfAccount"); 
				String ToKfAccount = requestMap.get("ToKfAccount"); 
				
				log.info(LogUtils.format("FromKfAccount") + FromKfAccount);
				log.info(LogUtils.format("ToKfAccount") + ToKfAccount);
				// TODO: handle biz
			}else if (Constants.Event.MASSSENDJOBFINISH.equals(Event)) {//????????????  
				String MsgID = requestMap.get("MsgID");  //???????????????ID
				String Status = requestMap.get("Status"); //????????????????????????send success?????????send fail?????????err(num)??????
														    //???send success??????????????????????????????????????????????????????
														  	//??????????????????????????????????????????????????????
															//err(num)?????????????????????????????????????????????????????????
															//		err(10001) - ???????????? 
															//		err(20001) - ???????????? 
															//		err(20004) - ???????????? 
															//		err(20002) - ???????????? 
															//		err(20006) - ?????????????????? 
															//		err(20008) - ???????????? 
															//		err(20013) - ???????????? 
															//		err(22000) - ????????????(????????????) 
															//		err(21000) - ????????????
				String TotalCount = requestMap.get("TotalCount"); 	//group_id?????????????????????openid_list???????????????
				String FilterCount = requestMap.get("FilterCount"); //????????????????????????????????????????????????????????????????????????????????????
																	//??????????????????4????????????????????????????????????????????????????????????
																	//FilterCount = SentCount + ErrorCount
				String SentCount = requestMap.get("SentCount"); //????????????????????????
				String ErrorCount = requestMap.get("ErrorCount"); //????????????????????????
				
				log.info(LogUtils.format("MsgID") 		+ MsgID);
				log.info(LogUtils.format("Status") 		+ Status);
				log.info(LogUtils.format("TotalCount") 	+ TotalCount);
				log.info(LogUtils.format("FilterCount") 	+ FilterCount);
				log.info(LogUtils.format("SentCount") 	+ SentCount);
				log.info(LogUtils.format("ErrorCount") 	+ ErrorCount);
				
				if("send success".equals(Status)){
					// TODO: handle biz
				}else{
					// TODO: handle biz
				}
			}else if(Constants.Event.TEMPLATESENDJOBFINISH.equals(Event)) {// ????????????????????????????????? 
				String MsgID = requestMap.get("MsgID"); //??????id
				String Status = requestMap.get("Status"); //?????????????????????
				
				log.info(LogUtils.format("MsgID") 	+ MsgID);
				log.info(LogUtils.format("Status") 	+ Status);
				
				//----yancunling weixinTemplateMessageDao.updateSendStatus(Long.valueOf(MsgID), Status);
				
				if("success".equals(Status)){
//					1??????????????????????????????XML?????????
//					<xml>
//			           <ToUserName><![CDATA[gh_7f083739789a]]></ToUserName>
//			           <FromUserName><![CDATA[oia2TjuEGTNoeX76QEjQNrcURxG8]]></FromUserName>
//			           <CreateTime>1395658920</CreateTime>
//			           <MsgType><![CDATA[event]]></MsgType>
//			           <Event><![CDATA[TEMPLATESENDJOBFINISH]]></Event>
//			           <MsgID>200163836</MsgID>
//			           <Status><![CDATA[success]]></Status>
//			           </xml>
					// TODO: handle biz
				}else{
//						2????????????????????????????????????????????????????????????????????????????????????????????????XML?????????
//						 <xml>
//				           <ToUserName><![CDATA[gh_7f083739789a]]></ToUserName>
//				           <FromUserName><![CDATA[oia2TjuEGTNoeX76QEjQNrcURxG8]]></FromUserName>
//				           <CreateTime>1395658984</CreateTime>
//				           <MsgType><![CDATA[event]]></MsgType>
//				           <Event><![CDATA[TEMPLATESENDJOBFINISH]]></Event>
//				           <MsgID>200163840</MsgID>
//				           <Status><![CDATA[failed:user block]]></Status>
//				           </xml>
					
//						3????????????????????????????????????????????????XML?????????
//						 <xml>
//				           <ToUserName><![CDATA[gh_7f083739789a]]></ToUserName>
//				           <FromUserName><![CDATA[oia2TjuEGTNoeX76QEjQNrcURxG8]]></FromUserName>
//				           <CreateTime>1395658984</CreateTime>
//				           <MsgType><![CDATA[event]]></MsgType>
//				           <Event><![CDATA[TEMPLATESENDJOBFINISH]]></Event>
//				           <MsgID>200163840</MsgID>
//				           <Status><![CDATA[failed: system failed]]></Status>
//				           </xml>
					
					// TODO: handle biz
				}
			}
			
		}else{//??????????????????
			if (Constants.MsgType.TEXT.equals(MsgType)) {// ?????????????????? - ????????????
				String Content = requestMap.get("Content");// ??????????????????
				MsgId = requestMap.get("MsgId");// ??????id???64?????????
				
				log.info(LogUtils.format("Content") 	+ Content);
				log.info(LogUtils.format("MsgId") 	+ MsgId);
				
				Text textMsg = new Text(appInfo.getSysId(), MsgId, FromUserName, ToUserName, CreateTime, MsgType, Content);
				textMsg.setId(idService.generateWxResponseMessage());
				insertTextMsg(textMsg);
				log.info("inserted....");
				
				//???????????????????????????????????????
				//----YANCUNLING CheckDkf checkDkf = weixinCustomService.checkIsUseDkf(appInfo, Content);
				//----YANCUNLING if(checkDkf.isDkf()){//?????????????????????
				if(false){
					log.info("...");
					/*log.info("<<<<<<<<forword to dkf>>>>>>>>");
					log.info("is auto=>" + checkDkf.isAuto() + ", kf_aacount=>" + checkDkf.getKf_account());
					log.info("<<<<<<<<forword to dkf>>>>>>>>");
					
					if(checkDkf.isAuto()){//????????????????????????
						String responseMsg =  XmlTemplete.transferCustomerService(FromUserName, ToUserName, time);
						log.info("???????????????????????????");
						log.info("transfer_customer_service1===" + responseMsg);
						if(pc != null){
							return pc.encryptMsg(responseMsg, time, nonce);
						}else{
							return responseMsg;
						}
					}else{//???????????????????????????
						String responseMsg =  XmlTemplete.transferCustomerService(FromUserName, ToUserName, time, checkDkf.getKf_account());
						log.info("???????????????????????????");
						log.info("transfer_customer_service2===" + responseMsg);
						if(pc != null){
							return pc.encryptMsg(responseMsg, time, nonce);
						}else{
							return responseMsg;
						}
					}*/
				} else {
					// Thread msgThread = new Thread(new SendMsgThread(request, wechatService,wechatMapper, appInfo, requestMap,Content));
					Thread msgThread = new Thread(new SendMsgThread(request, wechatService, shopWxNewService, wechatMapper, appInfo, requestMap,"CONTENT",Content,webUploadPath));
					
					msgThread.start();
					// returnMsg = WeixinResponseMessageUtils.genResponseMsg(request,appInfo,requestMap,weixinMessageDao.getReturnMessage(appInfo, Content));
				}
			}else if (Constants.MsgType.IMAGE.equals(MsgType)) {// ?????????????????? - ????????????
				String PicUrl = requestMap.get("PicUrl");// ????????????
				String MediaId = requestMap.get("MediaId");// ??????????????????id?????????????????????????????????????????????????????????
				MsgId = requestMap.get("MsgId");// ??????id???64?????????
				
				log.info(LogUtils.format("PicUrl") 	+ PicUrl);
				log.info(LogUtils.format("MediaId") 	+ MediaId);
				log.info(LogUtils.format("MsgId") 	+ MsgId);
				
				Image image = new Image(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, MediaId,PicUrl);
//					insertImageMsg(image);
				
				Thread msgThread = new Thread(new DownloadImageMsgThread(request,appInfo, image));
				msgThread.start();
				
			}else if (Constants.MsgType.VOICE.equals(MsgType)) {// ?????????????????? - ????????????
				String MediaId = requestMap.get("MediaId");// ??????????????????id????????????????????????????????????????????????????????????
				String Format = requestMap.get("Format");// ??????????????????amr???speex???
				@SuppressWarnings("unused")
				String Recognition = requestMap.get("Recognition");// ?????????????????????UTF8??????
				MsgId = requestMap.get("MsgId");// ??????id???64?????????
				
				log.info(LogUtils.format("MediaId") 		+ MediaId);
				log.info(LogUtils.format("Format") 		+ Format);
				log.info(LogUtils.format("Recognition") 	+ Recognition);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				 
				Voice vocie = new Voice(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, MediaId,Format);
				insertVoiceMsg(vocie);
				
			}else if (Constants.MsgType.VIDEO.equals(MsgType)) {// ?????????????????? - ????????????
				String MediaId = requestMap.get("MediaId");// ??????????????????id?????????????????????????????????????????????????????????
				String ThumbMediaId = requestMap.get("ThumbMediaId");// ??????????????????????????????id?????????????????????????????????????????????????????????
				MsgId = requestMap.get("MsgId");// ??????id???64?????????
			
				log.info(LogUtils.format("MediaId") 		+ MediaId);
				log.info(LogUtils.format("ThumbMediaId") + ThumbMediaId);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				
				Video video = new Video(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, MediaId,ThumbMediaId);
				insertVideoMsg(video);
			}else if (Constants.MsgType.SHORTVIDEO.equals(MsgType)) {// ?????????????????? - ???????????????
				String MediaId = requestMap.get("MediaId");// ??????????????????id?????????????????????????????????????????????????????????
				String ThumbMediaId = requestMap.get("ThumbMediaId");// ??????????????????????????????id?????????????????????????????????????????????????????????
				MsgId = requestMap.get("MsgId");// ??????id???64?????????
			
				log.info(LogUtils.format("MediaId") 		+ MediaId);
				log.info(LogUtils.format("ThumbMediaId") + ThumbMediaId);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				
				Video video = new Video(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, MediaId,ThumbMediaId);
				insertVideoMsg(video);
			}else if (Constants.MsgType.LOCATION.equals(MsgType)) {// ?????????????????? -
				// ??????????????????
				String Location_X = requestMap.get("Location_X");// ??????????????????
				String Location_Y = requestMap.get("Location_Y");// ??????????????????
				String Scale = requestMap.get("Scale");// ??????????????????
				String Label = requestMap.get("Label");// ??????????????????
				MsgId = requestMap.get("MsgId");// ??????id???64?????????
			 
				log.info(LogUtils.format("Location_X") 	+ Location_X);
				log.info(LogUtils.format("Location_Y") 	+ Location_Y);
				log.info(LogUtils.format("Scale") 		+ Scale);
				log.info(LogUtils.format("Label") 		+ Label);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				
				Location location = new Location(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, Location_X,Location_Y,Scale,Label);
				insertLocationMsg(location);
			}else if (Constants.MsgType.LINK.equals(MsgType)) {// ?????????????????? - ????????????
				String Title = requestMap.get("Title");// ????????????
				String Description = requestMap.get("Description");// ????????????
				String Url = requestMap.get("Url");// ????????????
				MsgId = requestMap.get("MsgId");// ??????id???64?????????
				
				log.info(LogUtils.format("Title") 		+ Title);
				log.info(LogUtils.format("Description") 	+ Description);
				log.info(LogUtils.format("Url") 			+ Url);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				
				Link link = new Link(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, Title,Description,Url);
				insertLinkMsg(link);
				
			} 
		}
		
		//????????????????????????
		long executeTime = System.currentTimeMillis() - startTime.getTime();
		log.info("executeTime=== "+ executeTime );
		
		ExecutionLog  log = new ExecutionLog();
		log.setSysId(appInfo.getSysId());
		log.setShopId(appInfo.getShopId());
		log.setStartTime(startTime);
		log.setExecuteTime(executeTime);
		log.setToUserName(ToUserName);
		log.setFromUserName(FromUserName);
		log.setCreateTime(CreateTime);
		log.setMsgType(MsgType);
		log.setEvent(Event);
		log.setEventKey(EventKey);
		log.setMsgId(MsgId);
		
		Thread thread = new Thread(new ExecutionLoggingThread(wechatService,Constants.LogType.WX_SERVER_IF, log));
		thread.start();
		
		return returnMsg;
	}
	
	public Long insertTextMsg(Text text) {
		return wechatMapper.insertTextMsg(text);
	}

	public long insertImageMsg(Image image) {
		return wechatMapper.insertImageMsg(image); 
	}

	public long insertVoiceMsg(Voice vocie) {
		return wechatMapper.insertVoiceMsg(vocie); 
	}

	public long insertVideoMsg(Video video) {
		return wechatMapper.insertVideoMsg(video); 
	}

	public long insertLocationMsg(Location location) {
		return wechatMapper.insertLocationMsg(location); 
	}

	public long insertLinkMsg(Link link) {
		return wechatMapper.insertLinkMsg(link); 
	}

	public long insertEventLocation(EventLocation location) {
		return wechatMapper.insertEventLocation(location); 
	}
}
