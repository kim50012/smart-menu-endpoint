package com.basoft.core.ware.wechat.domain.pay;

/**
 * http://pay.weixin.qq.com/wiki/doc/api/index.php?chapter=9_5
 */
public class PayRefundQueryRequest {
	private String appid; // 公众账号ID 是
	private String mch_id; // 商户号 是
	private String device_info; // 设备号 否
	private String nonce_str;// 随机字符串 是
	private String sign;// 签名 是
	private String transaction_id; // 微信订单号 否
	private String out_trade_no; // 商户订单号 是
	private String out_refund_no; // 商户退款单号 否
	private String refund_id; // 微信退款单号 否 refund_id、out_refund_no、out_trade_no、transaction_id四个参数必填一个，
								// 如果同时存在优先级为： refund_id>out_refund_no>transaction_id>out_trade_no

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

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}
}