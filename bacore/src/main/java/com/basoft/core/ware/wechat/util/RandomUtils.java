package com.basoft.core.ware.wechat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RandomUtils {
	public static boolean regphone(String value) {
		String regExp = "^[1][0-9]{10}$";
		Pattern rp = Pattern.compile(regExp);
		Matcher m = rp.matcher(value);
		return m.find();
	}

	public static String generateNumberRandom(int length) {
		int max = new Double(Math.pow(10, length)).intValue();
		int min = new Double(Math.pow(10, length - 1)).intValue();
		Random r = new Random();
		int xx = r.nextInt(max);
		while (xx < min) {
			xx = r.nextInt(max);
		}
		return String.valueOf(xx);
	}
	
	/**
	 * @param length 表示生成字符串的长度
	 * @return
	 */
	public static String generateRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * length表示生成字符串的长度
	 * 
	 * @return
	 */
	public static String generateRandomString() {
		return generateRandomString(32);
	}

	/**
	 * length表示生成字符串的长度
	 * 
	 * @return
	 */
	public static String generateRandomDateString() {
		String random = generateNumberRandom(5);
		String data = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return data + random;
	}

	/**
	 * @return
	 */
	public static Integer generateRandomSceneId() {
		return Integer.valueOf(generateNumberRandom(9));
	}
	
	/**
	 * @param shopId
	 * @param compNm
	 * @param originalAppId
	 * @param appid
	 * @param appSecret
	 * @param account_type
	 * @param mchId
	 * @param secretKey
	 */
	public static void generateAppInfo(Integer shopId, String compNm, String originalAppId, String appid,
			String appSecret, Integer account_type, String mchId, String secretKey) {
		String domain = "www.hanzhimeng.com";
		String sysId = generateRandomString(32).toUpperCase();
		String url = "http://" + domain + "/wx/valid.htm?key=" + sysId;
		String token = generateRandomString();
		String EncodingAESKey = generateRandomString(43);
		if (StringUtils.isEmpty(secretKey) || secretKey.length() != 32) {
			secretKey = generateRandomString(32);
		}
		
		System.out.println("/***************************************************/");
		System.out.println("/******************* 公众账号信息  *******************/");
		System.out.println("/***************************************************/");
		System.out.println("--url               = " + url);
		System.out.println("--token             = " + token);
		System.out.println("--EncodingAESKey    = " + EncodingAESKey);
		System.out.println("--安全域名                     = " + domain);
		System.out.println("--secretKey         = " + secretKey);
		System.out.println("/***************************************************/");
		System.out.println("/***************  APP INFO INSERT   ****************/");
		System.out.println("/***************************************************/");
		 
		System.out.println("USE [HZM]");
		System.out.println("GO");
		System.out.println("");
		System.out.println("INSERT INTO [dbo].[WX_APP_INFO]	(");
		System.out.println("		[SYS_ID]");
		System.out.println("		,[SHOP_ID]");
		System.out.println("		,[ORIGINAL_APP_ID]");
		System.out.println("		,[COMP_NM]");
		System.out.println("		,[APP_ID]");
		System.out.println("		,[APP_SECRET]");
		System.out.println("		,[URL]");
		System.out.println("		,[TOKEN]");
		System.out.println("		,[ENCORDING_AES_KEY]");
		System.out.println("		,[WECHAT_NO]");
		System.out.println("		,[ACCOUNT_TYPE]");
		System.out.println("		,[TRANSFER_CUSTOMER_SERVICE]");
		System.out.println("		,[ACCOUNT_STATUS]");
		System.out.println("		,[OPEN_BATCH_JOB]");
		System.out.println("	) VALUES (");
		System.out.println("		'" + sysId + "'");
		System.out.println("		," + shopId );
		System.out.println("		,'" + originalAppId + "'");
		System.out.println("		,'" + compNm + "'");
		System.out.println("		,'" + appid + "'");
		System.out.println("		,'" + appSecret + "'");
		System.out.println("		,'" + "http://" + domain + "'");
		System.out.println("		,'" + token + "'");
		System.out.println("		,'" + EncodingAESKey + "'");
		System.out.println("		," + "''" );
		System.out.println("		," + account_type );
		System.out.println("		," + "0" );
		System.out.println("		," + "1" );
		System.out.println("		," + "1" );
		System.out.println("	)" );
		System.out.println("GO");
		System.out.println("");
				
		System.out.println("/****************************************************/");
		System.out.println("/****************  SHOP AUTH SUCC.   ****************/");
		System.out.println("/****************************************************/");
		System.out.println("UPDATE SHOP SET STS_ID = 1, AUTH_IS_SUCC = 2 WHERE SHOP_ID = " + shopId);
		System.out.println("GO");
		System.out.println(""); 
		System.out.println("/****************************************************/");
		System.out.println("/****************  MCH INFO INSERT   ****************/");
		System.out.println("/****************************************************/");
		System.out.println("INSERT INTO [dbo].[WX_MCH_INFO] (");
		System.out.println("		[SYS_ID]");
		System.out.println("		,[MCH_ID]");
		System.out.println("		,[SECRET_KEY]");
		System.out.println("		,[CERT_EXPIRY_DATE]");
		System.out.println("	) VALUES (");
		System.out.println("		'" + sysId + "'");
		System.out.println("		,'" + mchId + "'");
		System.out.println("		,'" + secretKey + "'");
		System.out.println("		, " + "NULL" + "");
		System.out.println("	)" );
		System.out.println("GO");
		
		System.out.println("/***********************************************/");
	}
	
	/**
	 * @param shopId
	 * @param compNm
	 * @param originalAppId
	 * @param appid
	 * @param appSecret
	 * @param account_type
	 * @param mchId
	 * @param secretKey
	 * @return
	 */
	public static Map<String, Object> generateData(Integer shopId, String compNm, String originalAppId, String appid,
			String appSecret, Integer account_type, String mchId, String secretKey) {
		String domain = "www.hanzhimeng.com";
		String sysId = generateRandomString(32).toUpperCase();
		//String url = "http://" + domain + "/wx/valid.htm?key=" + sysId;
		String token = generateRandomString();
		String EncodingAESKey = generateRandomString(43);
		if (StringUtils.isEmpty(secretKey) || secretKey.length() != 32) {
			secretKey = generateRandomString(32);
		}
		Map<String, Object> DateMap = new HashMap<String, Object>();
		DateMap.put("shopId", shopId);
		DateMap.put("originalAppId", originalAppId);
		DateMap.put("compNm", compNm);
		DateMap.put("appid", appid);
		DateMap.put("appSecret", appSecret);
		DateMap.put("account_type", account_type);
		DateMap.put("mchId", mchId);
		DateMap.put("secretKey", secretKey);
		DateMap.put("sysId", sysId);
		DateMap.put("domain", domain);
		DateMap.put("token", token);
		DateMap.put("EncodingAESKey", EncodingAESKey);
		return DateMap;
	}
	
	public static void main(String[] args) {
		System.out.println(generateRandomString(32));
		System.out.println(generateRandomString(32));
		System.out.println(generateRandomString(32));
		System.out.println(generateRandomString(32));
		System.out.println(generateRandomString(32));
		// generateAppInfo(194,"公众号名称", "orgappid", "appid", "appSecret", 1,"mchId","secretKey");
		generateAppInfo(26, "XXX科技有限公司 -XXX店", "gh_2007ac3f5401", "wxe14501b04f2f5dd9", "9b5622294a499a3b564c7b28b5a3b940", 1, "mchId", "secretKey");
	}
}