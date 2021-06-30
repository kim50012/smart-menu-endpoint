package com.basoft.service.batch.wechat.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.core.ware.wechat.domain.template.DataItem;
import com.basoft.core.ware.wechat.util.HttpClientUtils;
import com.basoft.core.ware.wechat.util.WeixinTemplateUtils;
import com.basoft.core.ware.wechat.util.XmlUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechatPay.alliexTrans.WechatPayAlliexTransService;
import com.basoft.service.util.SocketForHttp;
import com.basoft.service.util.WechatPayAlliexUtils;
import com.google.gson.Gson;

/**
 * 获取关注用户线程
 */
public class ServerOrderCheckThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private WechatPayAlliexTransService wechatPayAlliexTransService;
	private WechatService wechatService;
	private Long shopId;
	private static final String DEFAUT_COLOR = "#173177";
	private static final String RED_COLOR = "#df1414";
	private static final String BLUE_COLOR = "#2714df";

	/**
	 * 알리엑스에 환율 interface
	 * 
	 * @param AlliexTransExchangeThread
	 * @param wechatPayAlliexTransService
	 */
	public ServerOrderCheckThread(WechatPayAlliexTransService wechatPayAlliexTransService, WechatService wechatService, Long shopId) {
		this.wechatPayAlliexTransService = wechatPayAlliexTransService;
		this.wechatService = wechatService;
		this.shopId = shopId;
	}

	@Override
	public void run() {
		
		logger.info("++++++++++++++++ ServerOrderCheckThread.run() ++++++++++++++++");

		Map<String,Object> map = new HashMap<String, Object>();

		try {

			logger.info("++++++++++++++++ ServerOrderCheckThread - STEP 0100 : Get I/F Data ++++++++++++++++");
			// 알리엑스 I/F 되는 대상 데이터 조회

			List<Map<String, Object>> checkOrderList = wechatPayAlliexTransService.selectServerCheckOrderList(map);

			logger.info("++++++++++++++++ ServerOrderCheckThread - STEP 0200 : I/F Data Total Count ▶▶▶▶ " + checkOrderList.size());
			for(int i = 0; i < checkOrderList.size(); i++){
				Map<String,Object> checkOrderData = checkOrderList.get(i);
				int x = i + 1;
				logger.info("++++++++++++++++ ServerOrderCheckThread - STEP 3500 : Post I/F Data  ▶▶ " + x + " th ◀◀");

				String token = wechatService.getAccessToken(shopId);
				
				String open_id = checkOrderData.get("open_id").toString();
				String openid1 = checkOrderData.get("openid1").toString();
				String templateId = checkOrderData.get("template_msg_id").toString();
				String orderId = checkOrderData.get("out_trade_no").toString();
				String orderdt = checkOrderData.get("orderdt").toString();
				String nick = checkOrderData.get("nick").toString();
				String transactionId = checkOrderData.get("transaction_id").toString();
				String payAmtUsd = checkOrderData.get("pay_amt_usd").toString();
				String remark = transactionId + "\n" + 
						"金额 : $ "+payAmtUsd+"\n" + 
						"今天下班之前退款!";

				Map<String, DataItem> data = new HashMap<String, DataItem>();
				
				data.put("first", new DataItem("测试服务器订单必须当天退款", RED_COLOR));		//详细内容
				
				data.put("keyword1", new DataItem(orderId, BLUE_COLOR));	//异常信息
				data.put("keyword2", new DataItem(orderdt, DEFAUT_COLOR));		//发生时间
				data.put("keyword3", new DataItem(nick, DEFAUT_COLOR));	//用户信息
				data.put("remark", new DataItem(remark, DEFAUT_COLOR));
				
				

				try {
					WeixinTemplateUtils.sendTemplateMessage(token, open_id, templateId, null, data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					WeixinTemplateUtils.sendTemplateMessage(token, openid1, templateId, null, data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}			
			
			
		} catch (Exception e) {
			logger.error("++++++++++++++++ ServerOrderCheckThread - Error : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
