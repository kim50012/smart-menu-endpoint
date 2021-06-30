package com.basoft.core.ware.wechat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.basoft.core.ware.wechat.domain.MchInfo;

/*import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;*/
/*import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;*/

public class PaymentUtils { 
	private static final String KEY_STORE_TYPE = "PKCS12";

	private static final transient Log logger = LogFactory.getLog(PaymentUtils.class);

	private static Properties prop;
	static {
		// System.out.println("==============================");
		InputStream inputStream = PaymentUtils.class.getClassLoader().getResourceAsStream("config/payment.properties");
		prop = new Properties();
		try {
			prop.load(inputStream);
			inputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.error(e1.getMessage());
		}
	}

	private PaymentUtils() {
	}

	public static String getValue(String key) {
		return prop.getProperty(key);
	}

	public static CloseableHttpClient getCloseableHttpClient(MchInfo mchInfo) throws Exception {
		KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
		String cretPath = PaymentUtils.getValue("weixin.cert.path.prefix") + mchInfo.getMchId() + PaymentUtils.getValue("weixin.cert.path.suffix");
		// logger.info("cretPath=" + cretPath);
		FileInputStream instream = new FileInputStream(new File(cretPath));
		try {
			keyStore.load(instream, mchInfo.getMchId().toCharArray());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw e;
		} finally {
			instream.close();
		}

		 // Trust own CA and all self-signed certs
	      SSLContext sslcontext = SSLContexts.custom()
	              .loadKeyMaterial(keyStore, mchInfo.getMchId().toCharArray())
	              .build();
	      // Allow TLSv1 protocol only
	      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	              sslcontext,
	              new String[] { "TLSv1" },
	              null,
	              SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	      CloseableHttpClient httpclient = HttpClients.custom()
	              .setSSLSocketFactory(sslsf)
	              .build();
		return httpclient;
	}
}