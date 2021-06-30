package com.basoft.service.util;

import java.util.Map;


import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 */
public class WechatPayAlliexUtils {

	/**
	 * 알리엑스 Secet Key 고정 값
	 */
	public static final String ALLIEX_SECRET_KEY = "T3NPAH0247";

	/**
	 * 알리엑스 Payment API URL -	Transaction Authorization(Non-UI Interface)
	 */
	public static final String ALLIEX_PAYMENT_URL = "https://testkrpay2.alliexpay.com/gateway/wechatOA/transaction.jsp";
	public static final String ALLIEX_PAYMENT_URL_LIVE = "https://krpay2.alliexpay.com/gateway/wechatOA/transaction.jsp";

	/**
	 * 알리엑스 Payment Cancel API URL -	Refund(Non-UI Interface)
	 */
	public static final String ALLIEX_PAYMENT_CANCEL_URL = "https://testkrpay2.alliexpay.com/gateway/wechatOA/transaction.jsp";
	public static final String ALLIEX_PAYMENT_CANCEL_URL_LIVE = "https://krpay2.alliexpay.com/gateway/wechatOA/transaction.jsp";

	/**
	 * 알리엑스 Payment 조회 API URL -	Transaction Query(Non-UI Interface)
	 */
	public static final String ALLIEX_PAYMENT_QUERY_URL = "https://testkrpay2.alliexpay.com/gateway/wechatOA/query.jsp";
	public static final String ALLIEX_PAYMENT_QUERY_URL_LIVE = "https://krpay2.alliexpay.com/gateway/wechatOA/query.jsp";

	/**
	 * 알리엑스 환율 API URL -	Exchange Rate Query(Non-UI Interface)
	 */
	public static final String ALLIEX_EXCHANGE_URL = "https://testkrpay2.alliexpay.com/gateway/wechatOA/directrate.jsp";
	public static final String ALLIEX_EXCHANGE_URL_LIVE = "https://krpay2.alliexpay.com/gateway/wechatOA/directrate.jsp";

	/**
	 * 알리엑스 거래대사 API URL -	Transaction History API (Non-UI Interface)
	 */
	public static final String ALLIEX_BILLCOMPARE_URL = "https://testkrpay2.alliexpay.com/gateway/wechatOA/billcompare.jsp";

	public static String FTP_TARGET_SERVER_BASE_DIRECTORY = "/home"; 		//테스트서버 Base 디렉토리
	public static String FTP_TARGET_SERVER_DIRECTORY = "/bacommercetest/"; 		//테스트서버 디렉토리

//	public static String FTP_TARGET_SERVER_BASE_DIRECTORY = "/home"; 		//운영서버 Base 디렉토리
//	public static String FTP_TARGET_SERVER_DIRECTORY = "/bacommercelivelive/"; 		//운영서버 디렉토리
	
	public static String FTP_FROM_SERVER_DIRECTORY  = "D:/filedownload/";  			// 로컬디렉토리
	public static String FTP_FILE_NAME = "BA_";			// 파일명
	
	
	/**
	 * 알리엑스 거래 검증
	 * 
	 * @param alliPaymentData 아리엑스 파라미터 맵
	 * @return String 결과 secret value
	 */
	public static String getSecretValue(Map<String,Object> alliPaymentData) {

		String secretValue = "";
		String orgParameter = "";

		if (!alliPaymentData.get("merchantid").equals("")) {//  
			orgParameter = "MerchantId=" + alliPaymentData.get("merchantid");
		}
		
		if (!alliPaymentData.get("merchanttransid").equals("")) {//  
			orgParameter += "&MerchantTransid=" + alliPaymentData.get("merchanttransid");
		}
		
		if (!alliPaymentData.get("merchantcurrcd").equals("")) {//  
			orgParameter += "&MerchantCurrcd=" + alliPaymentData.get("merchantcurrcd");
		}
		
		if (!alliPaymentData.get("merchantamount").equals("")) {//  
			orgParameter += "&MerchantAmount=" + alliPaymentData.get("merchantamount");
		}
		
		orgParameter += "&SecretKey=" + alliPaymentData.get("gateway_pw");

		secretValue = DigestUtils.sha256Hex(orgParameter);
		
		return secretValue;
	}
	    
    

}
