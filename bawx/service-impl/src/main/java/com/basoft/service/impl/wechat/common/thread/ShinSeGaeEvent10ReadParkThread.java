package com.basoft.service.impl.wechat.common.thread;

import com.basoft.core.ware.common.framework.utilities.IntegerUtils;
import com.basoft.core.ware.wechat.domain.MchInfo;
import com.basoft.core.ware.wechat.domain.pay.SendRedPackRequest;
import com.basoft.core.ware.wechat.domain.pay.SendRedPackResponse;
import com.basoft.core.ware.wechat.util.PaymentUtils;
import com.basoft.core.ware.wechat.util.WeixinSignUtils;
import com.basoft.core.ware.wechat.util.XmlUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/** 
 * 新世界-活动10 发红包
 */
public class ShinSeGaeEvent10ReadParkThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());
	
	private HttpServletRequest request;
	
	private WechatService wechatService;
	
	private AppInfo appInfo;
	private String openid;

	public ShinSeGaeEvent10ReadParkThread(HttpServletRequest request, WechatService wechatService, AppInfo appInfo, String openid) {
		super();
		this.request = request;
		this.wechatService = wechatService;
		this.appInfo = appInfo;
		this.openid = openid;
	}

	@Override
	public void run() {
		logger.info("=============ShinSeGaeEvent10ReadParkThread================");
		logger.info("=============ShinSeGaeEvent10ReadParkThread start================");
		try {
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
			//EventService eventService = wac.getBean(EventService.class);
			//WeixinTemplateMessageService weixinTemplateMessageService = wac.getBean(WeixinTemplateMessageService.class);
			
			Map<String,Object> searchMap = new HashMap<String,Object>();
			
			searchMap.put("SHOP_ID", appInfo.getShopId());
			searchMap.put("OPENID", openid);
			
			//Map<String,Object> redParkInfo = eventService.selectEvent10RedParkInfo(searchMap);
			Map<String,Object> redParkInfo = null;
			if(redParkInfo != null && !redParkInfo.isEmpty()){
				
				String mch_billno 	=	 (String)redParkInfo.get("MCH_BILLNO") ;
				String nick_name 	=	 (String)redParkInfo.get("NICK_NAME") ;
				String send_name 	=	 (String)redParkInfo.get("SEND_NAME") ;
				String openid2 		=	 (String)redParkInfo.get("FROM_OPENID") ;
				Integer total_amount = IntegerUtils.valueOf((String)redParkInfo.get("TOTAL_AMOUNT")) ;
				String wishing 		= (String)redParkInfo.get("WISHING") ;
				String client_ip 	= "118.145.2.17";
				String act_name 	= (String)redParkInfo.get("ACT_NAME") ;
				String remark 		= (String)redParkInfo.get("REMARK") ;
				String cust_nick_nm  = (String)redParkInfo.get("CUST_NICK_NM") ;
				String prize_desc 	=(String)redParkInfo.get("PRIZE_DESC") ;
				String op_user_id 	= "SYSTEM";
				
				MchInfo mchInfo = wechatService.selectMchInfoByShopId(appInfo.getShopId());
//				MchInfo mchInfo = weixinService.selectMchInfoByKey(appInfo.getSysId());
				
				logger.info("appid=========" + appInfo.getAppId());
				logger.info("mch_id========" + mchInfo.getMchId());
				logger.info("device_info===" + "");
				logger.info("mch_billno====" + mch_billno);
				logger.info("nick_name=====" + nick_name);
				logger.info("send_name=====" + send_name);
				logger.info("openid========" + openid2);
				logger.info("total_amount==" + total_amount);
				logger.info("wishing=======" + wishing);
				logger.info("client_ip=====" + client_ip);

				logger.info("act_name======" + act_name);
				logger.info("remark========" + remark);
				logger.info("cust_nick_nm==" + cust_nick_nm);
				logger.info("prize_desc====" + prize_desc);
				
				SendRedPackRequest interfaceRequest = new SendRedPackRequest(); 
				
				interfaceRequest.setMch_billno(mch_billno) ; //String(28)
				interfaceRequest.setMch_id(mchInfo.getMchId());
				interfaceRequest.setWxappid(appInfo.getAppId());
				interfaceRequest.setNick_name(nick_name); //String(32)
				interfaceRequest.setSend_name(send_name); //String(32)
				interfaceRequest.setRe_openid(openid2);
				interfaceRequest.setTotal_amount(total_amount);
				interfaceRequest.setMin_value(total_amount);
				interfaceRequest.setMax_value(total_amount);
				interfaceRequest.setTotal_num(1);
				interfaceRequest.setWishing(wishing);//String(128)
				interfaceRequest.setClient_ip(client_ip);
				interfaceRequest.setAct_name(act_name); //String(32)
				interfaceRequest.setRemark(remark); //String(256)
//				sendRedPackRequest.setLogo_imgurl("商户logo的url");
//				sendRedPackRequest.setShare_content("分享文案");
//				sendRedPackRequest.setShare_url("分享链接");
//				sendRedPackRequest.setShare_imgurl("分享的图片");
				
				
				
				
				
				String xml = WeixinSignUtils.sendRedPackRequestSign(interfaceRequest , mchInfo); 
				
				CloseableHttpClient httpclient = null;
				CloseableHttpResponse response = null;
				
				try {
					httpclient = PaymentUtils.getCloseableHttpClient(mchInfo);
					logger.info("-----------------sendredpark request start-----------------------");
					HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");
					logger.info("executing request:" + httppost.getRequestLine());
					
					logger.info("POST Data=\n" + xml);
					
					httppost.setEntity(new StringEntity(xml, "utf-8"));
					
					response = httpclient.execute(httppost);
					
					logger.info("-----------------sendredpark request end-----------------------");
					HttpEntity entity = response.getEntity();
		 			StringBuffer results = new StringBuffer();
				 
		 			logger.info("-----------------sendredpark response start-----------------------");
		 			logger.info("Response Status: " + response.getStatusLine());
		 			if (entity != null) {
						logger.info("Response content length: " + entity.getContentLength());
						 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
						String text;
						while ((text = bufferedReader.readLine()) != null) {
							results.append(text);
							logger.info(text);
						}

						logger.info("Response content:\n" + results.toString());
						logger.info("-----------------sendredpark response end-----------------------");
					}
		 			EntityUtils.consume(entity);
		 			
		 			logger.info("--sendredpark 111111111111111111111111111-");
		 			
		 			SendRedPackResponse interfaceResponse = null;
		 			try {
						interfaceResponse = XmlUtils.parseXml2Obj(results.toString(), SendRedPackResponse.class);
					} catch (Exception e) {
						logger.error("sendredpark0000011:" + e.getMessage());
						try {
							String source = results.toString();
							byte[] bs = source.getBytes("gbk");
							//用新的字符编码生成字符串
							String dist = new String(bs, "utf8");
							interfaceResponse = XmlUtils.parseXml2Obj(dist, SendRedPackResponse.class);
						} catch (Exception e2) {
							// TODO: handle exception
							logger.error("sendredpark0000012:" + e2.getMessage());
							
							interfaceResponse = new SendRedPackResponse();
							interfaceResponse.setErr_code(e2.getMessage());
						}
					}
		 			logger.info("--sendredpark 2222222222222222222222222222-");
//		 			SendRedPackResponse interfaceResponse = XmlUtils.parseXml2Obj(results.toString(), SendRedPackResponse.class);
		 			logger.info("interfaceRespons==========" + interfaceResponse);
		 			
		 			
		 			
		 			if ("SUCCESS".equals(interfaceResponse.getReturn_code()) && "SUCCESS".equals(interfaceResponse.getResult_code())) {
		 				logger.info("---------red park send sccesss----------");
		 				logger.info("interfaceRespons=" + interfaceResponse);
		 				logger.info("openid001==" +  openid);
		 				logger.info("openid002==" +  openid2);
		 				
		 				
		 				searchMap.clear();
		 				searchMap.put("SHOP_ID", appInfo.getShopId());
						searchMap.put("OPENID", openid);
						searchMap.put("EVENT_ID", 10);
						searchMap.put("SEQID", redParkInfo.get("SEQID"));
						searchMap.put("ERRORNO", null);
		 				//eventService.saveEvent10RedParkResult(searchMap);
		 				
		 				logger.info("---------red park send sccesss--------1--");
		 				try {
							
							//Long messageId = weixinTemplateMessageService.sendRedPackNotice(appInfo, openid2, act_name, cust_nick_nm, prize_desc,  wishing,remark);
							
							//weixinTemplateMessageService.updateSendStatus(messageId, "success");
							logger.info("---------red park send advice message sccesss-------openid2=" + openid2);
						} catch (Exception e) {
							logger.error("---------red park send advice message error-------openid2=" + openid2);
							logger.error(e.getMessage());
							e.printStackTrace();
						}
		 			}else{
		 				//error
						logger.error("-----------------sendredpark error-----------------------");
						
						logger.info("---------red park send error----------");
		 				logger.info(interfaceResponse.toString());
		 				logger.info("openid001==" +  openid);
		 				logger.info("openid002==" +  openid2);
		 				
		 				
		 				searchMap.clear();
		 				searchMap.put("SHOP_ID", appInfo.getShopId());
						searchMap.put("OPENID", openid);
						searchMap.put("EVENT_ID", 10);
						searchMap.put("SEQID", redParkInfo.get("SEQID"));
						searchMap.put("ERRORNO", interfaceResponse.getErr_code());
		 				//eventService.saveEvent10RedParkResult(searchMap);
		 				
		 				logger.info("---------red park send error--------1--");
						
						
						String errorMsg = "发送失败!\\n";
						
						if (interfaceResponse.getReturn_code().equals("FAIL")){
							errorMsg += "错误原因=" + interfaceResponse.getReturn_msg();
						}else{
							String err_code =  interfaceResponse.getErr_code();
							String err_code_des =  interfaceResponse.getErr_code_des();
							errorMsg += "错误码=" + err_code + ", 错误描述=" + err_code_des;
						}
						
						logger.error("error00001=" + errorMsg);
		 			}
		 			
				}catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (response != null) {
						try {
							response.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (httpclient != null) {
						try {
							httpclient.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("reaprak error 0000000000" + e.getMessage());
		}
		
		
	}
	
	

}
