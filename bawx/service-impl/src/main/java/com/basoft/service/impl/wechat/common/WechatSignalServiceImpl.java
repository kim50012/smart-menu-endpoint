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
	* 处理微信服务器发送来的消息并回复消息 
	* 备注：对于微信服务器推送的消息，将消息先存储，先返回success给微信服务器.
	* 后台可后续再处理消息，如果需要回复用户消息，可通过调用客服消息接口API再回复用户消息.
	* 已此来解决请求超时问题
	* @param request
	* @param appInfo
	* @return
	* @throws Exception
	*/
	public String processingAndReplyMessage(HttpServletRequest request,AppInfo appInfo) throws Exception{
		
		Date startTime =new Date();
		/**
		 * 假如服务器无法保证在五秒内处理并回复，必须做出下述回复，
		 * 这样微信服务器才不会对此作任何处理，并且不会发起重试
		 * （这种情况下，可以使用客服消息接口进行异步回复），否则，将出现严重的错误提示。详见下面说明：
		 * 1、直接回复空串（指字节长度为0的空字符串，而不是XML结构体中content字段的内容为空）
		 * 2、直接回复success
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
		
		String signature  	= request.getParameter("signature"); //微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String timestamp  	= request.getParameter("timestamp"); //时间戳
		String nonce  		= request.getParameter("nonce");	 //随机数
		String encrypt_type = request.getParameter("encrypt_type");//加密类型 raw:不加密 ,aes:aes加密
		String msg_signature= request.getParameter("msg_signature");//对消息体的签名
		
		log.info(LogUtils.format("signature") 	+ signature);
		log.info(LogUtils.format("timestamp") 	+ timestamp);
		log.info(LogUtils.format("nonce") 		+ nonce);
		log.info(LogUtils.format("encrypt_type") + encrypt_type);
		log.info(LogUtils.format("msg_signature")+ msg_signature);
		log.info(LogUtils.format("postData") 	+ postData);
		
		
		String time 		= "";
		String ToUserName 	= ""; // 开发者微信号
		String FromUserName = ""; // 发送方帐号（一个OpenID）
		String CreateTime 	= ""; // 消息创建时间 （整型）
		String MsgType 		= ""; // 消息类型
		String Event 		= "";// 事件类型
		String EventKey  	= "";// 事件KEY值
		String MsgId	 	= "";// 消息id，64位整型
		
		Map<String,String> requestMap = null;
		WXBizMsgCrypt pc = null;
		
		if(StringUtils.isEmpty(encrypt_type)){
			log.error("<<WXPR00002>>");
			log.info("**************** 微信平台 ==> Server ********************");
			log.info("postData=\n" + postData);
			log.info("**************** 微信平台 ==> Server end ****************");
			
			// 调用parseXml方法解析xml格式请求消息到map里
			requestMap = XmlUtils.parseXml2Map(postData);
		}else if("aes".equals(encrypt_type)){//消息加密方式 ： 加密
			//解密消息
			pc = new WXBizMsgCrypt(appInfo.getToken(), appInfo.getEncordingAesKey(), appInfo.getAppId());
			String decryptedData =  pc.decryptMsg(msg_signature, timestamp, nonce, postData);
			log.info("**************** 微信平台 ==> Server ********************");
			log.info("decryptedData=\n" + decryptedData);
			log.info("**************** 微信平台 ==> Server end ****************");
			
			// 调用parseXml方法解析xml格式请求消息到map里
			requestMap = XmlUtils.parseXml2Map(decryptedData);
		}
			

		time = System.currentTimeMillis() + "";
		ToUserName 	 = requestMap.get("ToUserName");	// 开发者微信号
		FromUserName = requestMap.get("FromUserName");	// 发送方帐号（一个OpenID）
		CreateTime 	 = requestMap.get("CreateTime");	// 消息创建时间 （整型）
		MsgType 	 = requestMap.get("MsgType");		// 消息类型

		log.info(LogUtils.format("ToUserName") 	+ ToUserName);
		log.info(LogUtils.format("FromUserName")	+ FromUserName);
		log.info(LogUtils.format("CreateTime")  	+ CreateTime);
		log.info(LogUtils.format("MsgType") 		+ MsgType);

		// 事件处理
		if (Constants.MsgType.EVENT.equals(MsgType)) {
			Event = requestMap.get("Event");// 事件类型
			log.info(LogUtils.format("Event") + Event);

			// 关注事件 OR 扫描带参数二维码事件(用户未关注时，进行关注后的事件推送)
			if (Constants.Event.SUBSCRIBE.equals(Event)) {
				log.info("【EVENT SUBSCRIBE】>>>>>开始处理关注事件......");

				// 获取事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
				EventKey = requestMap.get("EventKey");
				log.info("【EVENT SUBSCRIBE】扫码关注的二维码参数为>>>>>>{}", EventKey);

				// 1、发送关注消息
				Thread msgThread = new Thread(new SendMsgThread(request, wechatService,shopWxNewService,wechatMapper, appInfo, requestMap,"FOCUS","",webUploadPath));
				msgThread.start();

				log.info("【EVENT SUBSCRIBE】扫码关注的二维码参数为>>>>>>{}", EventKey);

				// 2、处理扫描二维码关注微信公众号的扫码事项
				// 扫描微信公众号二维码关注
				boolean isOldUser = false;
				if (StringUtils.isNotBlank(EventKey)) {
					// 3.1 扫描CA代理商的二维码关注
					if(EventKey.contains("agent_")){
						//查询该微信用户关注该公众号的信息列表
						List<Map<String, Object>> wxUserList = wechatMapper.getWxUserByOpenid(FromUserName);
						// 关注过
						if (wxUserList != null && wxUserList.size() > 0) {
							isOldUser = true;
						}

						// 启动扫码代理商二维码处理线程
						try {
							Thread thread = new Thread(new AgentQRCodeSubscribeEventThread(request, wechatService, wechatMapper, appInfo, requestMap, EventKey, isOldUser));
							thread.start();
						} catch (Exception e) {
							log.error("启动扫码代理商二维码处理线程异常:" + e.getMessage(), e);
						}
					}
					// 3.2 扫描商户二维码关注
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
							log.error("扫码商户二维码的处理线程异常:" + e.getMessage(), e);
						}
					}
				}

				// 3、保存关注用户信息
				log.info("【EVENT SUBSCRIBE】>>>>>保存同步用户信息");
				// String token = wechatService.getAccessToken(appInfo);
				WXUser user = new WXUser();
				user.setSysId(appInfo.getSysId());
				user.setOpenid(FromUserName);
				weixinUserService.userSubscribe(wechatService.getAccessToken(appInfo), user);

				//新世界发送红包 -ended
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
				//新世界发送红包 END
					
				// 获取欢迎词
				//returnMsg = WeixinResponseMessageUtils.genResponseMsg(request,appInfo,requestMap,weixinMessageDao.getSubscribeMessage(appInfo));
			}else if (Constants.Event.UNSUBSCRIBE.equals(Event)) {// 取消关注事件
				WXUser user = new WXUser();
				user.setSysId(appInfo.getSysId());
				user.setOpenid(FromUserName);
				weixinUserService.userUnsubscribe(user);
			}else if(Constants.Event.SCAN.equals(Event)) {// 扫描带参数二维码事件(用户已关注时的事件推送)
				EventKey = requestMap.get("EventKey");// 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
				String Ticket = requestMap.get("Ticket");// Ticket  二维码的ticket，可用来换取二维码图片
				
				log.info(LogUtils.format("EventKey") + EventKey);
				log.info(LogUtils.format("Ticket") 	+ Ticket);
				
				try {
					// 代理商二维码扫码
					if(EventKey.startsWith("agent_")){
						Thread thread = new Thread(new AgentQRCodeEventThread(request, wechatService, wechatMapper, appInfo, requestMap, EventKey));
						thread.start();
					}
					// 商户（StoreTable）二维码扫码
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
				 
			}else if(Constants.Event.LOCATION.equals(Event)) {// 上报地理位置事件
				String Latitude  = requestMap.get("Latitude");  //地理位置纬度
				String Longitude = requestMap.get("Longitude"); //地理位置经度
				String Precision = requestMap.get("Precision"); //地理位置精度
				
				log.info(LogUtils.format("Latitude") 	+ Latitude);
				log.info(LogUtils.format("Longitude") 	+ Longitude);
				log.info(LogUtils.format("Precision") 	+ Precision);
				
			}else if(Constants.Event.CLICK.equals(Event)) {// 自定义菜单事件(点击菜单拉取消息时的事件推送)
				EventKey = requestMap.get("EventKey"); // 事件KEY值，与自定义菜单接口中KEY值对应
				log.info(LogUtils.format("EventKey") + EventKey);
				try {
					Thread thread = new Thread(new MenuClickThread(wechatService,appInfo, requestMap));
					thread.start();
				} catch (Exception e) {
					// TODO: handle exception
					log.error("thread33333333333:" + e.getMessage());
				}
				//用MenuClickThread主动推送的方式代替下面被动回复及菜单点击记录线程
//				try {
//					Thread thread = new Thread(new MenuClickLoggingThread(appInfo,FromUserName,EventKey ,weixinService));
//					thread.start();
//				} catch (Exception e) {
//					// TODO: handle exception
//					log.error("thread33333333333:" + e.getMessage());
//				}
//				return WeixinResponseMessageUtils.genResponseMsg(request,appInfo,requestMap,weixinMessageDao.getClickEventReturnMessage(appInfo, Integer.valueOf(EventKey)));
			
			}else if(Constants.Event.VIEW.equals(Event)) {// 自定义菜单事件(点击菜单跳转链接时的事件推送)
				EventKey = requestMap.get("EventKey");// 事件KEY值，设置的跳转URL
				log.info(LogUtils.format("EventKey") + EventKey);
			}else if(Constants.Event.SCANCODE_PUSH.equals(Event)) {// 自定义菜单事件-(扫码推事件)
				EventKey = requestMap.get("EventKey");
//					Element ScanCodeInfo = root.element("ScanCodeInfo");
//					String ScanType = ScanCodeInfo.elementTextTrim("ScanType");
//					String ScanResult = ScanCodeInfo.elementTextTrim("ScanResult");
				log.info(LogUtils.format("EventKey") + EventKey);
//					log.info("ScanType=========" + ScanType);
//					log.info("ScanResult=========" + ScanResult);
			}else if(Constants.Event.SCANCODE_WAITMSG.equals(Event)) {// 自定义菜单事件-(扫码推事件且弹出“消息接收中”提示框)
				EventKey = requestMap.get("EventKey");
//					Element ScanCodeInfo = root.element("ScanCodeInfo");
//					String ScanType = ScanCodeInfo.elementTextTrim("ScanType");
//					String ScanResult = ScanCodeInfo.elementTextTrim("ScanResult");
				log.info(LogUtils.format("EventKey") + EventKey);
//					log.info("ScanType=========" + ScanType);
//					log.info("ScanResult=========" + ScanResult);
			}else if(Constants.Event.PIC_SYSPHOTO.equals(Event) || Constants.Event.PIC_WEIXIN.equals(Event)
					|| Constants.Event.PIC_PHOTO_OR_ALBUM.equals(Event)) {// 自定义菜单事件-(弹出系统拍照发图,弹出微信相册发图器,弹出拍照或者相册发图)
				EventKey = requestMap.get("EventKey");
				log.info(LogUtils.format("EventKey") + EventKey);
				// log.info("Count=========" + Count);
				// log.info("PicMd5Sum=========" + PicMd5Sum);

			}else if(Constants.Event.LOCATION_SELECT.equals(Event)) {// 自定义菜单事件(弹出地理位置选择器)
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

			}else if(Constants.Event.KF_CREATE_SESSION.equals(Event)) {// 多客服会话控制-接入会话 
				String KfAccount = requestMap.get("KfAccount"); 
				log.info(LogUtils.format("KfAccount") + KfAccount);
				// TODO: handle biz
			}else if(Constants.Event.KF_CLOSE_SESSION.equals(Event)) {//  多客服会话控制-关闭会话
				String KfAccount = requestMap.get("KfAccount"); 
				log.info(LogUtils.format("KfAccount") + KfAccount);
				// TODO: handle biz
			}else if(Constants.Event.KF_SWITCH_SESSION.equals(Event)) {//  多客服会话控制-转接会话
				String FromKfAccount = requestMap.get("FromKfAccount"); 
				String ToKfAccount = requestMap.get("ToKfAccount"); 
				
				log.info(LogUtils.format("FromKfAccount") + FromKfAccount);
				log.info(LogUtils.format("ToKfAccount") + ToKfAccount);
				// TODO: handle biz
			}else if (Constants.Event.MASSSENDJOBFINISH.equals(Event)) {//群发任务  
				String MsgID = requestMap.get("MsgID");  //群发的消息ID
				String Status = requestMap.get("Status"); //群发的结构，为“send success”或“send fail”或“err(num)”。
														    //但send success时，也有可能因用户拒收公众号的消息、
														  	//系统错误等原因造成少量用户接收失败。
															//err(num)是审核失败的具体原因，可能的情况如下：
															//		err(10001) - 涉嫌广告 
															//		err(20001) - 涉嫌政治 
															//		err(20004) - 涉嫌社会 
															//		err(20002) - 涉嫌色情 
															//		err(20006) - 涉嫌违法犯罪 
															//		err(20008) - 涉嫌欺诈 
															//		err(20013) - 涉嫌版权 
															//		err(22000) - 涉嫌互推(互相宣传) 
															//		err(21000) - 涉嫌其他
				String TotalCount = requestMap.get("TotalCount"); 	//group_id下粉丝数；或者openid_list中的粉丝数
				String FilterCount = requestMap.get("FilterCount"); //过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，
																	//用户接收已超4条的过滤）后，准备发送的粉丝数，原则上，
																	//FilterCount = SentCount + ErrorCount
				String SentCount = requestMap.get("SentCount"); //发送成功的粉丝数
				String ErrorCount = requestMap.get("ErrorCount"); //发送失败的粉丝数
				
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
			}else if(Constants.Event.TEMPLATESENDJOBFINISH.equals(Event)) {// 模版消息发送任务完成后 
				String MsgID = requestMap.get("MsgID"); //消息id
				String Status = requestMap.get("Status"); //发送状态为成功
				
				log.info(LogUtils.format("MsgID") 	+ MsgID);
				log.info(LogUtils.format("Status") 	+ Status);
				
				//----yancunling weixinTemplateMessageDao.updateSendStatus(Long.valueOf(MsgID), Status);
				
				if("success".equals(Status)){
//					1、送达成功时，推送的XML如下：
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
//						2、送达由于用户拒收（用户设置拒绝接收公众号消息）而失败时，推送的XML如下：
//						 <xml>
//				           <ToUserName><![CDATA[gh_7f083739789a]]></ToUserName>
//				           <FromUserName><![CDATA[oia2TjuEGTNoeX76QEjQNrcURxG8]]></FromUserName>
//				           <CreateTime>1395658984</CreateTime>
//				           <MsgType><![CDATA[event]]></MsgType>
//				           <Event><![CDATA[TEMPLATESENDJOBFINISH]]></Event>
//				           <MsgID>200163840</MsgID>
//				           <Status><![CDATA[failed:user block]]></Status>
//				           </xml>
					
//						3、送达由于其他原因失败时，推送的XML如下：
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
			
		}else{//接收普通消息
			if (Constants.MsgType.TEXT.equals(MsgType)) {// 接收普通消息 - 文本消息
				String Content = requestMap.get("Content");// 文本消息内容
				MsgId = requestMap.get("MsgId");// 消息id，64位整型
				
				log.info(LogUtils.format("Content") 	+ Content);
				log.info(LogUtils.format("MsgId") 	+ MsgId);
				
				Text textMsg = new Text(appInfo.getSysId(), MsgId, FromUserName, ToUserName, CreateTime, MsgType, Content);
				textMsg.setId(idService.generateWxResponseMessage());
				insertTextMsg(textMsg);
				log.info("inserted....");
				
				//判断是否将消息转发至多客服
				//----YANCUNLING CheckDkf checkDkf = weixinCustomService.checkIsUseDkf(appInfo, Content);
				//----YANCUNLING if(checkDkf.isDkf()){//使用多客服系统
				if(false){
					log.info("...");
					/*log.info("<<<<<<<<forword to dkf>>>>>>>>");
					log.info("is auto=>" + checkDkf.isAuto() + ", kf_aacount=>" + checkDkf.getKf_account());
					log.info("<<<<<<<<forword to dkf>>>>>>>>");
					
					if(checkDkf.isAuto()){//消息转发到多客服
						String responseMsg =  XmlTemplete.transferCustomerService(FromUserName, ToUserName, time);
						log.info("将消息转发至多客服");
						log.info("transfer_customer_service1===" + responseMsg);
						if(pc != null){
							return pc.encryptMsg(responseMsg, time, nonce);
						}else{
							return responseMsg;
						}
					}else{//消息转发到指定客服
						String responseMsg =  XmlTemplete.transferCustomerService(FromUserName, ToUserName, time, checkDkf.getKf_account());
						log.info("消息转发到指定客服");
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
			}else if (Constants.MsgType.IMAGE.equals(MsgType)) {// 接收普通消息 - 图片消息
				String PicUrl = requestMap.get("PicUrl");// 图片链接
				String MediaId = requestMap.get("MediaId");// 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
				MsgId = requestMap.get("MsgId");// 消息id，64位整型
				
				log.info(LogUtils.format("PicUrl") 	+ PicUrl);
				log.info(LogUtils.format("MediaId") 	+ MediaId);
				log.info(LogUtils.format("MsgId") 	+ MsgId);
				
				Image image = new Image(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, MediaId,PicUrl);
//					insertImageMsg(image);
				
				Thread msgThread = new Thread(new DownloadImageMsgThread(request,appInfo, image));
				msgThread.start();
				
			}else if (Constants.MsgType.VOICE.equals(MsgType)) {// 接收普通消息 - 语音消息
				String MediaId = requestMap.get("MediaId");// 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。。
				String Format = requestMap.get("Format");// 语音格式，如amr，speex等
				@SuppressWarnings("unused")
				String Recognition = requestMap.get("Recognition");// 语音识别结果，UTF8编码
				MsgId = requestMap.get("MsgId");// 消息id，64位整型
				
				log.info(LogUtils.format("MediaId") 		+ MediaId);
				log.info(LogUtils.format("Format") 		+ Format);
				log.info(LogUtils.format("Recognition") 	+ Recognition);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				 
				Voice vocie = new Voice(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, MediaId,Format);
				insertVoiceMsg(vocie);
				
			}else if (Constants.MsgType.VIDEO.equals(MsgType)) {// 接收普通消息 - 视频消息
				String MediaId = requestMap.get("MediaId");// 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
				String ThumbMediaId = requestMap.get("ThumbMediaId");// 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
				MsgId = requestMap.get("MsgId");// 消息id，64位整型
			
				log.info(LogUtils.format("MediaId") 		+ MediaId);
				log.info(LogUtils.format("ThumbMediaId") + ThumbMediaId);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				
				Video video = new Video(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, MediaId,ThumbMediaId);
				insertVideoMsg(video);
			}else if (Constants.MsgType.SHORTVIDEO.equals(MsgType)) {// 接收普通消息 - 小视频消息
				String MediaId = requestMap.get("MediaId");// 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
				String ThumbMediaId = requestMap.get("ThumbMediaId");// 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
				MsgId = requestMap.get("MsgId");// 消息id，64位整型
			
				log.info(LogUtils.format("MediaId") 		+ MediaId);
				log.info(LogUtils.format("ThumbMediaId") + ThumbMediaId);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				
				Video video = new Video(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, MediaId,ThumbMediaId);
				insertVideoMsg(video);
			}else if (Constants.MsgType.LOCATION.equals(MsgType)) {// 接收普通消息 -
				// 地理位置消息
				String Location_X = requestMap.get("Location_X");// 地理位置维度
				String Location_Y = requestMap.get("Location_Y");// 地理位置经度
				String Scale = requestMap.get("Scale");// 地图缩放大小
				String Label = requestMap.get("Label");// 地理位置信息
				MsgId = requestMap.get("MsgId");// 消息id，64位整型
			 
				log.info(LogUtils.format("Location_X") 	+ Location_X);
				log.info(LogUtils.format("Location_Y") 	+ Location_Y);
				log.info(LogUtils.format("Scale") 		+ Scale);
				log.info(LogUtils.format("Label") 		+ Label);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				
				Location location = new Location(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, Location_X,Location_Y,Scale,Label);
				insertLocationMsg(location);
			}else if (Constants.MsgType.LINK.equals(MsgType)) {// 接收普通消息 - 链接消息
				String Title = requestMap.get("Title");// 消息标题
				String Description = requestMap.get("Description");// 消息描述
				String Url = requestMap.get("Url");// 消息链接
				MsgId = requestMap.get("MsgId");// 消息id，64位整型
				
				log.info(LogUtils.format("Title") 		+ Title);
				log.info(LogUtils.format("Description") 	+ Description);
				log.info(LogUtils.format("Url") 			+ Url);
				log.info(LogUtils.format("MsgId") 		+ MsgId);
				
				Link link = new Link(appInfo.getSysId(),MsgId,FromUserName,ToUserName,CreateTime, MsgType, Title,Description,Url);
				insertLinkMsg(link);
				
			} 
		}
		
		//记录执行时间日志
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
