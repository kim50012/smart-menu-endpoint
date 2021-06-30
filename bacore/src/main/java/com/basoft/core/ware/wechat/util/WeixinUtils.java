package com.basoft.core.ware.wechat.util;

import com.basoft.core.ware.wechat.domain.ApiTicket;
import com.basoft.core.ware.wechat.domain.IPReturn;
import com.basoft.core.ware.wechat.domain.TokenInfo;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;
import com.basoft.core.ware.wechat.servlet.utils.MyX509TrustManager;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.List;

/**
 * 基础接口
 */
@Slf4j
public class WeixinUtils extends WeixinBaseUtils {
	/**
	 * 获取access token
	 */
	private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * 获取微信服务器IP地址URL
	 */
	private static final String GET_CALLBACK_IP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";

	/**
	 * 获取 jsapi_ticket URL
	 */
	private static final String TICKET_GETTICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	/**
	 * 获取access_token
	 * 
	 * @param appid 第三方用户唯一凭证
	 * @param secret 第三方用户唯一凭证密钥，即appsecret
	 * @return String 凭证
	 */
	public static String getAccessToken(String appid, String secret) {
		log.info("GET方式调用微信API获取微信token");
		String url = TOKEN_URL.replaceAll("APPID", appid).replaceAll("APPSECRET", secret);
		String result = HttpClientUtils.requestGet(url);
		TokenInfo returns = new Gson().fromJson(result, TokenInfo.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getAccess_token();
	}

	/**
	 * 获取 JS-SDK ticket
	 *
	 * @param access_token
	 * @return ApiTicket
	 */
	public static ApiTicket getApiTicket(String access_token) {
		String url = getInterfaceUrl(TICKET_GETTICKET_URL, access_token);
		String result = HttpClientUtils.requestGet(url);
		ApiTicket returns = new Gson().fromJson(result, ApiTicket.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns;
	}

	/**
	 * 获取微信服务器IP地址
	 * 
	 * @param access_token 公众号的access_token
	 * @return List<String> 微信服务器IP地址列表
	 */
	public static List<String> getCallbackIPList(String access_token) {
		String url = getInterfaceUrl(GET_CALLBACK_IP_URL, access_token);
		String result = HttpClientUtils.requestGet(url);
		IPReturn returns = new Gson().fromJson(result, IPReturn.class);
		// logger.info(returns);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getIp_list();
	}

	/**
	 * @Description 发起https请求并获取结果
	 * @author temdy
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			ce.printStackTrace();
			// log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("https request error:{}", e);
		}
		return jsonObject;
	}
}
