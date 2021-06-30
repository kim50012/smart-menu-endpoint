package com.basoft.core.ware.wechat.domain.pay;

public class PaymentNotify extends Result {
	private String appid; // 公众账号ID
	private String attach;
	private String bank_type;
	private String cash_fee;
	private String fee_type;
	private String is_subscribe;
	private String key;
	private String mch_id; // 商户号
	private String nonce_str;// 随机字符串
	private String openid;
	private String out_trade_no;
	private String result_code;// 业务结果
	private String sign;// 签名
	private String time_end;
	private String total_fee;
	private String trade_type;
	private String transaction_id;

	@Override
	public String toString() {
		return "PaymentNotify [appid=" + appid + ", attach=" + attach + ", bank_type=" + bank_type + ", cash_fee="
				+ cash_fee + ", fee_type=" + fee_type + ", is_subscribe=" + is_subscribe + ", key=" + key + ", mch_id="
				+ mch_id + ", nonce_str=" + nonce_str + ", openid=" + openid + ", out_trade_no=" + out_trade_no
				+ ", result_code=" + result_code + ", sign=" + sign + ", time_end=" + time_end + ", total_fee="
				+ total_fee + ", trade_type=" + trade_type + ", transaction_id=" + transaction_id
				+ ", getReturn_code()=" + getReturn_code() + ", getReturn_msg()=" + getReturn_msg() + "]";
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public String getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(String cash_fee) {
		this.cash_fee = cash_fee;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
}