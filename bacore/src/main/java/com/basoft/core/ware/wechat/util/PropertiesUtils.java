package com.basoft.core.ware.wechat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertiesUtils {
	private static final transient Log logger = LogFactory.getLog(PropertiesUtils.class);

	private static Properties prop;
	static {
		logger.info("=============PropertiesUtils. init =================");
		InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("basoft-core.properties");
		prop = new Properties();
		try {
			prop.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private PropertiesUtils() {
	}

	public static String getUploadBaseDir() {
		return getValue("upload.base.dir");
	}

	
	//Test
	public static String getAlliexFtpTestUserName() {
		return getValue("alliex.ftpTest.username");
	}

	public static String getAlliexFtpTestPassword() {
		return getValue("alliex.ftpTest.password");
	}

	public static String getAlliexFtpTestServerIp() {
		return getValue("alliex.ftpTest.server.ip");
	}

	public static String getAlliexFtpTestServerPort() {
		return getValue("alliex.ftpTest.server.port");
	}

	
	//Real
	public static String getAlliexFtpRealUserName() {
		return getValue("alliex.ftpReal.username");
	}

	public static String getAlliexFtpRealPassword() {
		return getValue("alliex.ftpReal.password");
	}

	public static String getAlliexFtpRealServerIp() {
		return getValue("alliex.ftpReal.server.ip");
	}

	public static String getAlliexFtpRealServerPort() {
		return getValue("alliex.ftpReal.server.port");
	}
	

	
	public static String getAlliexFtpPrivateKeyUse() {
		return getValue("alliex.ftp.private.key.use");
	}

	public static String getAlliexFtpPrivateKey() {
		return getValue("alliex.ftp.private.key");
	}

	public static String getAlliexFtpUploadRetryCount() {
		return getValue("alliex.ftp.upload.retry.count");
	}

	public static String getAlliexFtpUploadSleepTime() {
		return getValue("alliex.ftp.upload.sleep.time");
	}

	public static String getAlliexFtpDownloadRetryCount() {
		return getValue("alliex.ftp.download.retry.count");
	}

	public static String getAlliexFtpDownloadSleepTime() {
		return getValue("alliex.ftp.download.sleep.time");
	}
	
	public static String getValue(String key) {
		return prop.getProperty(key);
	}
}
