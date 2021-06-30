package com.basoft.core.ware.wechat.domain.pay;

/**
	-----------------Response----------------------- 
	<xml>
	    <appid>
	        <![CDATA[wxf4ff15142f410758]]>
	    </appid>
	    <cash_fee>
	        <![CDATA[1002]]>
	    </cash_fee>
	    <mch_id>
	        <![CDATA[1225226202]]>
	    </mch_id>
	    <nonce_str>
	        <![CDATA[QUDswopQyUPUZhNF]]>
	    </nonce_str>
	    <out_refund_no_0>
	        <![CDATA[10125]]>
	    </out_refund_no_0>
	    <out_trade_no>
	        <![CDATA[10125]]>
	    </out_trade_no>
	    <refund_count>1</refund_count>
	    <refund_fee>1002</refund_fee>
	    <refund_fee_0>1002</refund_fee_0>
	    <refund_id_0>
	        <![CDATA[2008960276201504080002738264]]>
	    </refund_id_0>
	    <refund_status_0>
	        <![CDATA[PROCESSING]]>
	    </refund_status_0>
	    <result_code>
	        <![CDATA[SUCCESS]]>
	    </result_code>
	    <return_code>
	        <![CDATA[SUCCESS]]>
	    </return_code>
	    <return_msg>
	        <![CDATA[OK]]>
	    </return_msg>
	    <sign>
	        <![CDATA[1CD66D1644AC51CC3BD0C6F1801C8C02]]>
	    </sign>
	    <total_fee>
	        <![CDATA[1002]]>
	    </total_fee>
	    <transaction_id>
	        <![CDATA[1008960276201504080052050152]]>
	    </transaction_id>
	</xml>
	 -----------------Response-----------------------
 */
public class PayRefundQueryResponse {
	private String return_code;
	private String return_msg;
	// 以下字段在return_code为SUCCESS的时候有返回
	private String result_code;
	private String err_code;
	private String err_code_des;
	private String appid;
	private String mch_id;
	private String device_info;
	private String nonce_str;
	private String sign;
	private String transaction_id;
	private String out_trade_no;
	private Integer total_fee;
	private String fee_type;
	private Integer cash_fee;
	private String cash_fee_type;
	private Integer refund_fee;
	private Integer coupon_refund_fee;
	private Integer refund_count;

	private String out_refund_no_0;
	private String out_refund_no_1;

	private String refund_id_0;
	private String refund_id_1;

	private String refund_status_0;
	private String refund_status_1;

	private String refund_channel_0;
	private String refund_channel_1;

	private Integer refund_fee_0;
	private Integer refund_fee_1;

	private Integer fee_type_0;
	private Integer fee_type_1;

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

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

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public Integer getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(Integer cash_fee) {
		this.cash_fee = cash_fee;
	}

	public String getCash_fee_type() {
		return cash_fee_type;
	}

	public void setCash_fee_type(String cash_fee_type) {
		this.cash_fee_type = cash_fee_type;
	}

	public Integer getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(Integer refund_fee) {
		this.refund_fee = refund_fee;
	}

	public Integer getCoupon_refund_fee() {
		return coupon_refund_fee;
	}

	public void setCoupon_refund_fee(Integer coupon_refund_fee) {
		this.coupon_refund_fee = coupon_refund_fee;
	}

	public Integer getRefund_count() {
		return refund_count;
	}

	public void setRefund_count(Integer refund_count) {
		this.refund_count = refund_count;
	}

	public String getOut_refund_no_0() {
		return out_refund_no_0;
	}

	public void setOut_refund_no_0(String out_refund_no_0) {
		this.out_refund_no_0 = out_refund_no_0;
	}

	public String getRefund_id_0() {
		return refund_id_0;
	}

	public void setRefund_id_0(String refund_id_0) {
		this.refund_id_0 = refund_id_0;
	}

	public String getRefund_channel_0() {
		return refund_channel_0;
	}

	public void setRefund_channel_0(String refund_channel_0) {
		this.refund_channel_0 = refund_channel_0;
	}

	public Integer getRefund_fee_0() {
		return refund_fee_0;
	}

	public void setRefund_fee_0(Integer refund_fee_0) {
		this.refund_fee_0 = refund_fee_0;
	}

	public Integer getFee_type_0() {
		return fee_type_0;
	}

	public void setFee_type_0(Integer fee_type_0) {
		this.fee_type_0 = fee_type_0;
	}

	public String getOut_refund_no_1() {
		return out_refund_no_1;
	}

	public void setOut_refund_no_1(String out_refund_no_1) {
		this.out_refund_no_1 = out_refund_no_1;
	}

	public String getRefund_id_1() {
		return refund_id_1;
	}

	public void setRefund_id_1(String refund_id_1) {
		this.refund_id_1 = refund_id_1;
	}

	public String getRefund_channel_1() {
		return refund_channel_1;
	}

	public void setRefund_channel_1(String refund_channel_1) {
		this.refund_channel_1 = refund_channel_1;
	}

	public Integer getRefund_fee_1() {
		return refund_fee_1;
	}

	public void setRefund_fee_1(Integer refund_fee_1) {
		this.refund_fee_1 = refund_fee_1;
	}

	public Integer getFee_type_1() {
		return fee_type_1;
	}

	public void setFee_type_1(Integer fee_type_1) {
		this.fee_type_1 = fee_type_1;
	}

	public String getRefund_status_0() {
		return refund_status_0;
	}

	public void setRefund_status_0(String refund_status_0) {
		this.refund_status_0 = refund_status_0;
	}

	public String getRefund_status_1() {
		return refund_status_1;
	}

	public void setRefund_status_1(String refund_status_1) {
		this.refund_status_1 = refund_status_1;
	}

	@Override
	public String toString() {
		return "PayRefundQueryResponse [return_code=" + return_code + ", return_msg=" + return_msg + ", result_code="
				+ result_code + ", err_code=" + err_code + ", err_code_des=" + err_code_des + ", appid=" + appid
				+ ", mch_id=" + mch_id + ", device_info=" + device_info + ", nonce_str=" + nonce_str + ", sign=" + sign
				+ ", transaction_id=" + transaction_id + ", out_trade_no=" + out_trade_no + ", total_fee=" + total_fee
				+ ", fee_type=" + fee_type + ", cash_fee=" + cash_fee + ", cash_fee_type=" + cash_fee_type
				+ ", refund_fee=" + refund_fee + ", coupon_refund_fee=" + coupon_refund_fee + ", refund_count="
				+ refund_count + ", out_refund_no_0=" + out_refund_no_0 + ", refund_id_0=" + refund_id_0
				+ ", refund_status_0=" + refund_status_0 + ", refund_channel_0=" + refund_channel_0 + ", refund_fee_0="
				+ refund_fee_0 + ", fee_type_0=" + fee_type_0 + ", out_refund_no_1=" + out_refund_no_1
				+ ", refund_id_1=" + refund_id_1 + ", refund_status_1=" + refund_status_1 + ", refund_channel_1="
				+ refund_channel_1 + ", refund_fee_1=" + refund_fee_1 + ", fee_type_1=" + fee_type_1 + "]";
	}
}