package com.basoft.eorder.application.wx.sdk;

import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WXPayConfigImpl implements WXPayConfig {
    private byte[] certData;
    private byte[] certDataTest;
    private byte[] certDataProd;
    private static WXPayConfigImpl INSTANCE;



    private WXPayConfigImpl() throws Exception {
        //获取证书
    	
        try {
			File file = new ClassPathResource("apiclient_cert.p12").getFile();
			InputStream certStream = new FileInputStream(file);
			this.certData = new byte[(int) file.length()];
			certStream.read(this.certData);
			certStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
        
		try {
			File fileTest = new ClassPathResource("conf/apiclient_cert.p12").getFile();
			InputStream certStreamTest = new FileInputStream(fileTest);
			this.certDataTest = new byte[(int) fileTest.length()];
			certStreamTest.read(this.certDataTest);
			certStreamTest.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			File fileProd = new ClassPathResource("conf/apiclient_cert.p12").getFile();
			InputStream certStreamProd = new FileInputStream(fileProd);
			this.certDataProd = new byte[(int) fileProd.length()];
			certStreamProd.read(this.certDataProd);
			certStreamProd.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
    }

    public static WXPayConfigImpl getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl();
                }
            }
        }
        return INSTANCE;
    }

//    public String getAppID() {
//        return "wx169f9463dac237ee";
//    }


//    public String getMchID() {
//        return "1413386802";
//    }


//    public String getSubMchID() {
//        return "272399448";
//    }


//    public String getKey() {
//        return "xmd1820gd3fjbj1837soeif5620fjtuf";
//    }


    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }
    
    public InputStream getCertStreamTest() {
        return new ByteArrayInputStream(this.certDataTest);
    }
    
    public InputStream getCertStreamProd() {
        return new ByteArrayInputStream(this.certDataProd);
    }

    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    public IWXPayDomain getWXPayDomain() {
        return WXPayDomainImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

}
