package com.basoft.core.ware.wechat.domain.pay;

import java.util.Arrays;
public class QrCodeRequest {
	public static void main(String[] args) {
		String[] array = {
			"appid"
			,"openid"	
			,"time_stamp"	
			,"nonce_str"	
			,"product_id"	
		};
		
		Arrays.sort(array);
		for (String item : array) {
			System.out.println(item);
		}
	}

	private String appid;
	private String mch_id;
	private String time_stamp;
	private String nonce_str;
	private String product_id;
	private String sign;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}