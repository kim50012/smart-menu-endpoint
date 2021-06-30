package com.basoft.service.batch.wechat.thread;

import com.basoft.core.ware.wechat.domain.template.DataItem;
import com.basoft.core.ware.wechat.util.WeixinTemplateUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechatPay.alliexTrans.WechatPayAlliexTransService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取关注用户线程
 */
public class BookingOrderSendMsgThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private WechatPayAlliexTransService wechatPayAlliexTransService;
	private WechatService wechatService;
	private String sysId;
	private Long shopId;
	private static final String DEFAUT_COLOR = "#173177";
	private static final String RED_COLOR = "#df1414";
	private static final String BLUE_COLOR = "#2714df";

	/**
	 * 알리엑스에 환율 interface
	 * 
	 * @param
	 * @param wechatPayAlliexTransService
	 */
	public BookingOrderSendMsgThread(WechatPayAlliexTransService wechatPayAlliexTransService, WechatService wechatService, String sysId, Long shopId) {
		this.wechatPayAlliexTransService = wechatPayAlliexTransService;
		this.wechatService = wechatService;
		this.shopId = shopId;
		this.sysId = sysId;
	}

	@Override
	public void run() {
		
		logger.info("++++++++++++++++ BookingOrderSendMsgThread.run() ++++++++++++++++");

		Map<String,Object> map = new HashMap<String, Object>();

		try {

			logger.info("++++++++++++++++ BookingOrderSendMsgThread - STEP 0100 : Get I/F Data ++++++++++++++++");
			// 알리엑스 I/F 되는 대상 데이터 조회

			map.put("sysId", sysId);
			
			List<Map<String, Object>> checkOrderList = wechatPayAlliexTransService.selectBookingOrderSendMsgList(map);

			logger.info("++++++++++++++++ BookingOrderSendMsgThread - STEP 0200 : I/F Data Total Count ▶▶▶▶ " + checkOrderList.size());
			for(int i = 0; i < checkOrderList.size(); i++){
				Map<String,Object> checkOrderData = checkOrderList.get(i);
				int x = i + 1;
				logger.info("++++++++++++++++ BookingOrderSendMsgThread - STEP 3500 : Post I/F Data  ▶▶ " + x + " th ◀◀");

				//String token = wechatService.getAccessToken(shopId);
				String token = "23_p8NuZ88bOh7tFpWQ-2OB2qIORf1g9yUh_2qszL3Dglj4JxP6LdjGxyU_01a-Sdarht87ErctvaU1Gck_LwjFC9-Ks0ttKEttUPzkBnh3_CGxSo1KtIFPCwAC2OmWHfJo6dVdCBHvT-1rqGh8CJHcACAOVT";
				
				try {

					Map<String, DataItem> data = new HashMap<String, DataItem>();
					
					String orderId = checkOrderData.get("orderId").toString();
					String openId = checkOrderData.get("openId").toString();
					String name = checkOrderData.get("name").toString();
					String storeType = checkOrderData.get("storeType").toString();
					String custNm = checkOrderData.get("custNm").toString();
					String reseveDtfrom = checkOrderData.get("reseveDtfrom").toString();
					String reseveDtto = checkOrderData.get("reseveDtto").toString();
					String confirmTime = checkOrderData.get("confirmTime").toString();
					String productNm = checkOrderData.get("productNm").toString();
					String payAmtCny = checkOrderData.get("payAmtCny").toString();
					String payAmtKrw = checkOrderData.get("payAmtKrw").toString();
					String shippingType = checkOrderData.get("shippingType").toString();
					String shippingDt = checkOrderData.get("shippingDt").toString();
					String shippingAddrNm = checkOrderData.get("shippingAddrNm").toString();
					String status = checkOrderData.get("status").toString();
					String tmpMsgId = checkOrderData.get("tmpMsgId").toString();

	                if ("2".equals(storeType)) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
	                	
	                    String remark = "医院相关问题，请在聊天窗口中输入“3”. \n" + 
	                    		"如客服不在线，您也可到”个人中心“->”投诉，建议“中留言，我们会尽快给您答复。\n" + 
	                    		"如需电话沟通，请拨打热线：+82 64-748-0689.";
	                    
	                    data.put("first", new DataItem("亲爱的"+custNm+" 您好，您的预约已确认。请确认预约时间后到店访问。", DEFAUT_COLOR));
	                    data.put("storeName", new DataItem(name, DEFAUT_COLOR));
	                    data.put("bookTime", new DataItem(reseveDtfrom + " " + confirmTime, DEFAUT_COLOR));
	                    data.put("orderId", new DataItem(orderId, DEFAUT_COLOR));
	                    data.put("orderType", new DataItem(productNm, DEFAUT_COLOR));
	                    data.put("remark", new DataItem(remark, DEFAUT_COLOR));
	                }
	                
	                if ("3".equals(storeType)) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)		//OPENTM414077052
	                    if ("1".equals(shippingType)) {	//自提 - 现场实时自提
	                    	if ("5".equals(status)) {

	                            data.put("first", new DataItem("尊敬的 " + custNm + " 顾客，感谢您的购买。", DEFAUT_COLOR));
	                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
	                            data.put("keyword2", new DataItem(productNm, DEFAUT_COLOR));
	                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
	                            data.put("keyword4", new DataItem(name, DEFAUT_COLOR));
	                            data.put("remark", new DataItem("您的订单已完成.\n客户服务中心 : 064-748-0689", DEFAUT_COLOR));
	                    	}
	                    }
	                    else if ("2".equals(shippingType)) {	//自提 -现场预约时间自提
	                    	if ("5".equals(status)) {

	                            data.put("first", new DataItem("尊敬的 " + custNm + " 顾客，感谢您的购买。", DEFAUT_COLOR));
	                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
	                            data.put("keyword2", new DataItem(productNm, DEFAUT_COLOR));
	                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
	                            data.put("keyword4", new DataItem(name, DEFAUT_COLOR));
	                            data.put("remark", new DataItem("您的订单正在处理，我们会尽快通知处理结果.谢谢支持！", DEFAUT_COLOR));
	                    	}
	                    	else {

	                            data.put("first", new DataItem("尊敬的 " + custNm + " 顾客，感谢您的购买。", DEFAUT_COLOR));
	                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
	                            data.put("keyword2", new DataItem(productNm, DEFAUT_COLOR));
	                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
	                            data.put("keyword4", new DataItem(name, DEFAUT_COLOR));
	                            String remark = "您所订购的商品已完成备货. \n提货日:" + shippingDt + "\n请于提货日到店提取商品. \n门店详细地址可在我的订单明细中查询.\n客户服务中心 : 064-748-0689";
	                            data.put("remark", new DataItem(remark, DEFAUT_COLOR));
	                    	}
	                    }
	                    else if ("3".equals(shippingType)) {	//配送区域收货
	                    	if ("5".equals(status)) {

	                            data.put("first", new DataItem("尊敬的 " + custNm + " 顾客，感谢您的购买。", DEFAUT_COLOR));
	                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
	                            data.put("keyword2", new DataItem(productNm, DEFAUT_COLOR));
	                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
	                            data.put("keyword4", new DataItem(name, DEFAUT_COLOR));
	                            data.put("remark", new DataItem("您的订单正在处理，我们会尽快通知处理结果.谢谢支持！", DEFAUT_COLOR));
	                    	}
	                        else {

	                            data.put("first", new DataItem("尊敬的 " + custNm + " 顾客，您所订购的商品已完成备货。", DEFAUT_COLOR));
	                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
	                            data.put("keyword2", new DataItem(productNm, DEFAUT_COLOR));
	                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
	                            data.put("keyword4", new DataItem(shippingAddrNm, DEFAUT_COLOR));
	                            String remark = "您所订购的商品已发货.\n配送时间大概需要1~2日. 配送信息请咨询客服中心.\n客户服务中心 : 064-748-0689";
	                            data.put("remark", new DataItem(remark, DEFAUT_COLOR));
	                    	}
	                    }
	                    else {

	                        data.put("first", new DataItem("尊敬的 " + custNm + " 顾客，感谢您的购买。", DEFAUT_COLOR));
	                        data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
	                        data.put("keyword2", new DataItem(productNm, DEFAUT_COLOR));
	                        data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
	                        data.put("keyword4", new DataItem(name, DEFAUT_COLOR));
	                        data.put("remark", new DataItem("您的订单正在处理，我们会尽快通知处理结果.谢谢支持！\n客户服务中心 : 064-748-0689", DEFAUT_COLOR));
	                    }
	                }

	                if ("4".equals(storeType)) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)

	                    String remark = "如客服不在线，您也可到”个人中心“->”投诉，建议“中留言，我们会尽快给您答复。\n" +
	                    		"如需电话沟通，请拨打热线：+82 64-748-0689.";
	                    
	                    data.put("first", new DataItem("您预订的"+name+"酒店订单已确认。入住时，请务必携带护照到前台办理入住手续。", DEFAUT_COLOR));
	                    data.put("order", new DataItem(orderId, DEFAUT_COLOR));
	                    data.put("Name", new DataItem(custNm, DEFAUT_COLOR));
	                    data.put("datein", new DataItem(reseveDtfrom, DEFAUT_COLOR));
	                    data.put("dateout", new DataItem(reseveDtto, DEFAUT_COLOR));
	                    data.put("number", new DataItem("1间", DEFAUT_COLOR));
	                    data.put("room type", new DataItem(productNm, DEFAUT_COLOR));
	                    data.put("pay", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
	                    data.put("remark", new DataItem(remark, DEFAUT_COLOR));
	                }	
	                
	                
					WeixinTemplateUtils.sendTemplateMessage(token, openId, tmpMsgId, null, data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}			
			
			
		} catch (Exception e) {
			logger.error("++++++++++++++++ BookingOrderSendMsgThread - Error : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
