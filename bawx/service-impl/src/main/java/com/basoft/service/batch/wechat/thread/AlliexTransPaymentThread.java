package com.basoft.service.batch.wechat.thread;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.core.ware.wechat.util.HttpClientUtils;
import com.basoft.core.ware.wechat.util.XmlUtils;
import com.basoft.service.definition.wechatPay.alliexTrans.WechatPayAlliexTransService;
import com.basoft.service.util.SocketForHttp;
import com.basoft.service.util.WechatPayAlliexUtils;
import com.google.gson.Gson;

/**
 * 获取关注用户线程
 */
public class AlliexTransPaymentThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private WechatPayAlliexTransService wechatPayAlliexTransService;

	/**
	 * 알리엑스로 payment interface
	 * 
	 * @param AlliexTransPaymentThread
	 * @param wechatPayAlliexTransService
	 */
	public AlliexTransPaymentThread(WechatPayAlliexTransService wechatPayAlliexTransService) {
		this.wechatPayAlliexTransService = wechatPayAlliexTransService;
	}

	@Override
	public void run() {
		
		logger.info("++++++++++++++++ AlliexTransPaymentThread.run() ++++++++++++++++");
		
		Date ifStartDate = new Date(new Date().getTime() - 5 * 60 * 1000);
		System.out.println(ifStartDate);

		Map<String,Object> map = new HashMap<String, Object>();


		logger.info("++++++++++++++++ AlliexTransPaymentThread - STEP 0100 : Create Batch Header Id ++++++++++++++++");
		// order_pay_trans 배치 작업 실행을 위한 Batch Header Id 생성
		map.clear();
		map.put("batch_type", "order_pay_trans");	//★★★★★★ Batch job 유형
		Map<String, Object> batchHeader = wechatPayAlliexTransService.insertSelect_batch_header(map);
		BigInteger batch_id = (BigInteger) batchHeader.get("batch_id");
		
		try {
			logger.info("++++++++++++++++ AlliexTransPaymentThread - STEP 0200 : Create OrderPayTrans Data ++++++++++++++++");
			// 알리엑스 I/F 대상이 되는 데이터 생성
			map.clear();
			wechatPayAlliexTransService.insertBatchInitial_order_pay_trans(map);

			
			logger.info("++++++++++++++++ AlliexTransPaymentThread - STEP 0300 : Update Waite status Max 100 low ++++++++++++++++");
			// 알리엑스 I/F 대기 상태로 데이터 업데이트 한번에 100건
			map.clear();
			map.put("batch_id", batch_id);
			wechatPayAlliexTransService.updateTargetWait_order_pay_trans(map);

			
			logger.info("++++++++++++++++ AlliexTransPaymentThread - STEP 0400 : Get I/F Data ++++++++++++++++");
			// 알리엑스 I/F 되는 대상 데이터 조회
			map.clear();
			map.put("batch_id", batch_id);
			List<Map<String, Object>> alliPaymentList = wechatPayAlliexTransService.selectTargetList_order_pay_trans(map);

			logger.info("++++++++++++++++ AlliexTransPaymentThread - STEP 0410 : I/F Data Total Count ▶▶▶▶ " + alliPaymentList.size());
			for(int i = 0; i < alliPaymentList.size(); i++){
				Map<String,Object> alliPaymentData = alliPaymentList.get(i);
				int x = i + 1;
				logger.info("++++++++++++++++ AlliexTransPaymentThread - STEP 0500 : Post I/F Data  ▶▶ " + x + " th ◀◀");
				
				// 알리엑스로 post
				trasToAlliex(alliPaymentData);
				
			}
			
			
			logger.info("++++++++++++++++ AlliexTransPaymentThread - Update Batch Run Result ++++++++++++++++");
			// BATCH Header 결과 저장 (정상처리)
			map.clear();
			map.put("status", 2);
			map.put("batch_id", batch_id);
			wechatPayAlliexTransService.updateBatchResult_batch_header(map);
			
			
		} catch (Exception e) {
			logger.error("++++++++++++++++ AlliexTransPaymentThread - Error : " + e.getMessage());
			//
			// BATCH Header 결과 저장 (오류처리)
			map.clear();
			map.put("status", 9);
			map.put("batch_id", batch_id);
			wechatPayAlliexTransService.updateBatchResult_batch_header(map);
			e.printStackTrace();
		}
	}
	

	private void trasToAlliex(Map<String,Object> alliPaymentData){

		Map<String,Object> map = new HashMap<String, Object>();

		long trans_id = (long) alliPaymentData.get("trans_id");
		String transid_type = alliPaymentData.get("transid_type").toString();
		
		String secretValue = WechatPayAlliexUtils.getSecretValue(alliPaymentData);
		logger.info("++++++++++++++++ AlliexTransPaymentThread -STEP 0510 : secretValue : " + secretValue);
		logger.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶  AlliexTransPaymentThread -STEP 0511 : trans_id : " + alliPaymentData.get("trans_id").toString());
		
		try {
			
	        Map<String,Object> params = new LinkedHashMap<>();
	        params.put("Version", alliPaymentData.get("version").toString());
	        params.put("ProcessType", alliPaymentData.get("processtype").toString());
	        params.put("MerchantId", alliPaymentData.get("merchantid").toString());
	        params.put("TransType", alliPaymentData.get("transtype").toString());
	        params.put("GoodsName", alliPaymentData.get("goodsname").toString());
	        params.put("MerchantTransid", alliPaymentData.get("merchanttransid").toString());
	        params.put("Transid", alliPaymentData.get("transid").toString());
	        params.put("MerchantCurrcd", alliPaymentData.get("merchantcurrcd").toString());
	        params.put("MerchantAmount", alliPaymentData.get("merchantamount").toString());
	        params.put("MerchantDefine", alliPaymentData.get("merchantdefine").toString());
	        params.put("WechatCurrcd", alliPaymentData.get("wechatcurrcd").toString());
	        params.put("WechatAmount", alliPaymentData.get("wechatamount").toString());
	        params.put("WechatFxrate", alliPaymentData.get("wechatfxrate").toString());
	        params.put("ConnectIp", alliPaymentData.get("connectip").toString());
	        params.put("TransState", alliPaymentData.get("transstate").toString());
	        params.put("ReqDt", alliPaymentData.get("reqdt").toString());
	        params.put("ResDt", alliPaymentData.get("resdt").toString());
	        params.put("WechatTransdt", alliPaymentData.get("wechattransdt").toString());
	        params.put("WechatTransid", alliPaymentData.get("wechattransid").toString());
	        params.put("CashCurrcd", alliPaymentData.get("cashcurrcd").toString());
	        params.put("CashAmount", alliPaymentData.get("cashamount").toString());
	        params.put("CashBanktype", alliPaymentData.get("cashbanktype").toString());
	        params.put("Usertag", alliPaymentData.get("usertag").toString());
	        params.put("PrepayId", alliPaymentData.get("prepayid").toString());
	        params.put("CodeUrl", alliPaymentData.get("codeurl").toString());
	        params.put("OsType", alliPaymentData.get("ostype").toString());
	        params.put("MobileType", alliPaymentData.get("mobiletype").toString());
	        params.put("PayerEmail", alliPaymentData.get("payeremail").toString());
	        params.put("PayerMemberid", alliPaymentData.get("payermemberid").toString());
	        params.put("SecretValue", secretValue);
	        
	        String statusRetarans = alliPaymentData.get("status").toString();
	        if ("6".equals(statusRetarans)) {
		        params.put("RetransState", "R");
	        }

			String alliexIfUrl = WechatPayAlliexUtils.ALLIEX_PAYMENT_URL;
			if ("BA".equals(transid_type)) {
				alliexIfUrl = WechatPayAlliexUtils.ALLIEX_PAYMENT_URL_LIVE;
			}
	        
			String postData = SocketForHttp.requestPost(alliexIfUrl, params);
//			String postData = HttpClientUtils.requestPost(WechatPayAlliexUtils.ALLIEX_PAYMENT_URL, postStringFormat);
			logger.info("++++++++++++++++ AlliexTransPaymentThread -STEP 0520 : Result postData : \n" + postData);

			
			//==============================================================================================================
			//==============================================================================================================
			// TEST BY DIKIM
			//==============================================================================================================
			//==============================================================================================================
			/*
			postData = "<TransactionResponse>\n" + 
					"<Version>0100</Version>\n" + 
					"<ProcessType>PAYMENT</ProcessType>\n" + 
					"<MerchantId>KR18000000</MerchantId> \n" + 
					"<MerchantTransid>180101000001</MerchantTransid>\n" + 
					"<Transid>KO0000000001</Transid>\n" + 
					"<MerchantCurrcd>USD</MerchantCurrcd>\n" + 
					"<MerchantAmount>123.45</MerchantAmount>\n" + 
					"<ResponseCode>0000</ResponseCode>\n" + 
					"<ResponseMsg>SUCCESS</ResponseMsg>\n" + 
					"<SecretValue>93b42aa0b9819e0b987dec461a55c85b0a5332a19ea4768d57a91ed9f6d872c3</SecretValue>\n" + 
					"</TransactionResponse>";
			*/
			//==============================================================================================================
			//==============================================================================================================
			// TEST BY DIKIM
			//==============================================================================================================
			//==============================================================================================================

			//Result data xml parsing
//			Map<String,String> requestMap = new Gson().fromJson(postData, Map.class);
			Map<String,String> requestMap = XmlUtils.parseXml2Map(new String(postData.getBytes("gbk"), "utf-8"));
			
			String r_version = requestMap.get("Version");
			String r_processtype = requestMap.get("ProcessType");
			String r_merchantid = requestMap.get("MerchantId");
			String r_merchanttransid = requestMap.get("MerchantTransid");
			String r_merchantcurrcd = requestMap.get("MerchantCurrcd");
			String r_merchantamount = requestMap.get("MerchantAmount");
			String r_transid = requestMap.get("Transid");
			String r_responsecode = requestMap.get("ResponseCode");
			String r_responsemsg = requestMap.get("ResponseMessage");
			String r_secretvalue = requestMap.get("SecretValue");
			String result = "";

			logger.info("++++++++++++++++ AlliexTransPaymentThread -STEP 0530 : Result Data : ");
			logger.info("trans_id===============" + trans_id);
			logger.info("r_version===============" + r_version);
			logger.info("r_processtype===========" + r_processtype);
			logger.info("r_merchantid============" + r_merchantid);
			logger.info("r_merchanttransid=======" + r_merchanttransid);
			logger.info("r_merchantcurrcd========" + r_merchantcurrcd);
			logger.info("r_merchantamount========" + r_merchantamount);
			logger.info("r_transid===============" + r_transid);
			logger.info("r_responsecode==========" + r_responsecode);
			logger.info("r_responsemsg===========" + r_responsemsg);
			logger.info("r_secretvalue===========" + r_secretvalue);

			int status = 2;

			logger.info("++++++++++++++++ AlliexTransPaymentThread -STEP 0540 : Check ResponseCode ++++++++++++++++");
			if (!r_responsecode.equals("0000")) {
				status = 9;
			}
			

			logger.info("++++++++++++++++ AlliexTransPaymentThread -STEP 0550 : Save Result Data : ++++++++++++++++");
			// I/F 결과 저장 (정상처리)
			map.clear();
			map.put("trans_id", trans_id);
			map.put("status", status);
			map.put("secretvalue", secretValue);
			map.put("r_version", r_version);
			map.put("r_processtype", r_processtype);
			map.put("r_merchantid", r_merchantid);
			map.put("r_merchanttransid", r_merchanttransid);
			map.put("r_merchantcurrcd", r_merchantcurrcd);
			map.put("r_merchantamount", r_merchantamount);
			map.put("r_transid", r_transid);
			map.put("r_responsecode", r_responsecode);
			map.put("r_responsemsg", r_responsemsg);
			map.put("r_secretvalue", r_secretvalue);
			map.put("result", result);
			try {
				wechatPayAlliexTransService.updateBatchResult_order_pay_trans(map);
			}
			catch (Exception e) {
				logger.error("++++++++++++++++ AlliexTransPaymentThread - STEP 0551 trasToAlliex Update Error : " + e.getMessage());
			}
			
		} catch (Exception e) {
			logger.error("++++++++++++++++ AlliexTransPaymentThread - trasToAlliex Error : " + e.getMessage());
			// I/F 결과 저장 (오류처리)
			map.clear();
			map.put("trans_id", trans_id);
			map.put("status", 9);
			map.put("result", "trasToAlliex Batch job Error");
			map.put("secretvalue", secretValue);
			wechatPayAlliexTransService.updateBatchResult_order_pay_trans(map);
			e.printStackTrace();
		}
	}
	
	
}
