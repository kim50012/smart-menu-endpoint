package com.basoft.core.ware.wechat.domain.pay;

/**
 *  	 <h2>申请退款 Response</h2> <h3>应用场景</h3>
 *       当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，微信支付将在收到退款请求并且验证成功之后，
 *       按照退款规则将支付款按原路退到买家帐号上。 <br>
 *       注意：<br>
 *       1.交易时间超过半年的订单无法提交退款；<br>
 *       2.微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。一笔退款失败后重新提交，
 *       要采用原来的退款单号。总退款金额不能超过用户实际支付金额。 <h3>接口地址</h3> 接口链接：
 *       <code>https://api.mch.weixin.qq.com/secapi/pay/refund</code> <br>
 *       <h3>是否需要证书</h3> 请求需要双向证书。
 *
 *       <h3>返回结果</h3>
 * 
 *       <table style="border:1px solid black;border-collapse:collapse;border-spacing:0;">
 *       <tr>
 *       <th style="border: 1px solid;">字段名</th>
 *       <th style="border: 1px solid;">变量名</th>
 *       <th style="border: 1px solid;">必填</th>
 *       <th style="border: 1px solid;">类型</th>
 *       <th style="border: 1px solid;">示例值</th>
 *       <th style="border: 1px solid;">描述</th>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">返回状态码</td>
 *       <td style="border: 1px solid;">return_code</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(16)</td>
 *       <td style="border: 1px solid;">SUCCESS</td>
 *       <td style="border: 1px solid;">SUCCESS/FAIL</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">返回信息</td>
 *       <td style="border: 1px solid;">return_msg</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">String(128)</td>
 *       <td style="border: 1px solid;">签名失败</td>
 *       <td style="border: 1px solid;">返回信息，如非空，为错误原因<br>
 *       签名失败<br>
 *       参数格式校验错误</td>
 *       </tr>
 *       </table>
 * 
 *       <h3>以下字段在return_code为SUCCESS的时候有返回</h3>
 * 
 *       <table style="border:1px solid black;border-collapse:collapse;border-spacing:0;">
 *       <tr>
 *       <th style="border: 1px solid;">字段名</th>
 *       <th style="border: 1px solid;">变量名</th>
 *       <th style="border: 1px solid;">必填</th>
 *       <th style="border: 1px solid;">类型</th>
 *       <th style="border: 1px solid;">示例值</th>
 *       <th style="border: 1px solid;">描述</th>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">业务结果</td>
 *       <td style="border: 1px solid;">result_code</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(16)</td>
 *       <td style="border: 1px solid;">SUCCESS</td>
 *       <td style="border: 1px solid;">SUCCESS/FAIL<br>
 *       SUCCESS退款申请接收成功，结果通过退款查询接口查询<br>
 *       FAIL 提交业务失败</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">错误代码</td>
 *       <td style="border: 1px solid;">err_code</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">SYSTEMERROR</td>
 *       <td style="border: 1px solid;">列表详见第6节</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">错误代码描述</td>
 *       <td style="border: 1px solid;">err_code_des</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">String(128)</td>
 *       <td style="border: 1px solid;">系统超时</td>
 *       <td style="border: 1px solid;">结果信息描述</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">公众账号ID</td>
 *       <td style="border: 1px solid;">appid</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">wx8888888888888888</td>
 *       <td style="border: 1px solid;">微信分配的公众账号ID</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">商户号</td>
 *       <td style="border: 1px solid;">mch_id</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">1900000109</td>
 *       <td style="border: 1px solid;">微信支付分配的商户号</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">设备号</td>
 *       <td style="border: 1px solid;">device_info</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">013467007045764</td>
 *       <td style="border: 1px solid;">微信支付分配的终端设备号，与下单一致</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">随机字符串</td>
 *       <td style="border: 1px solid;">nonce_str</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">5K8264ILTKCH16CQ2502SI8ZNMTM67VS</td>
 *       <td style="border: 1px solid;">随机字符串，不长于32位</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">签名</td>
 *       <td style="border: 1px solid;">sign</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">5K8264ILTKCH16CQ2502SI8ZNMTM67VS</td>
 *       <td style="border: 1px solid;">签名，详见签名算法</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">微信订单号</td>
 *       <td style="border: 1px solid;">transaction_id</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(28)</td>
 *       <td style="border: 1px solid;">1217752501201407033233368018</td>
 *       <td style="border: 1px solid;">微信订单号</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">商户订单号</td>
 *       <td style="border: 1px solid;">out_trade_no</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">1217752501201407033233368018</td>
 *       <td style="border: 1px solid;">商户系统内部的订单号</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">商户退款单号</td>
 *       <td style="border: 1px solid;">out_refund_no</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">1217752501201407033233368018</td>
 *       <td style="border: 1px solid;">商户退款单号</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">微信退款单号</td>
 *       <td style="border: 1px solid;">refund_id</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(28)</td>
 *       <td style="border: 1px solid;">1217752501201407033233368018</td>
 *       <td style="border: 1px solid;">微信退款单号</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">退款金额</td>
 *       <td style="border: 1px solid;">refund_fee</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">Int</td>
 *       <td style="border: 1px solid;">100</td>
 *       <td style="border: 1px solid;">退款总金额,单位为分,可以做部分退款</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">退款货币种类</td>
 *       <td style="border: 1px solid;">refund_fee_type</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">String(8)</td>
 *       <td style="border: 1px solid;">CNY</td>
 *       <td style="border: 1px solid;">退款货币类型，符合ISO
 *       4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">订单总金额</td>
 *       <td style="border: 1px solid;">total_fee</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">Int</td>
 *       <td style="border: 1px solid;">100</td>
 *       <td style="border: 1px solid;">订单总金额，单位为分，<br>
 *       只能为整数，详见支付金额</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">订单金额货币种类</td>
 *       <td style="border: 1px solid;">fee_type</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">String(8)</td>
 *       <td style="border: 1px solid;">CNY</td>
 *       <td style="border: 1px solid;">订单金额货币类型，符合ISO
 *       4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">现金支付金额</td>
 *       <td style="border: 1px solid;">cash_fee</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">Int</td>
 *       <td style="border: 1px solid;">100</td>
 *       <td style="border: 1px solid;">现金支付金额，单位为分，<br>
 *       只能为整数，详见支付金额</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">货币种类</td>
 *       <td style="border: 1px solid;">cash_fee_type</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">String(8)</td>
 *       <td style="border: 1px solid;">CNY</td>
 *       <td style="border: 1px solid;">货币类型，符合ISO
 *       4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">现金退款金额</td>
 *       <td style="border: 1px solid;">cash_refund_fee</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">Int</td>
 *       <td style="border: 1px solid;">100</td>
 *       <td style="border: 1px solid;">现金退款金额，单位为分，<br>
 *       只能为整数，详见支付金额</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">现金退款货币类型</td>
 *       <td style="border: 1px solid;">cash_refund_fee_type</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">String(8)</td>
 *       <td style="border: 1px solid;">CNY</td>
 *       <td style="border: 1px solid;">货币类型，符合ISO 4217标准的三位字母代码， <br>
 *       默认人民币：CNY，其他值列表详见货币类型</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">代金券或立减优惠退款金额</td>
 *       <td style="border: 1px solid;">coupon_refund_fee</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">Int</td>
 *       <td style="border: 1px solid;">100</td>
 *       <td style="border: 1px solid;">代金券或立减优惠退款金额=订单金额-现金退款金额，<br>
 *       注意：立减优惠金额不会退回</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">代金券或立减优惠使用数量</td>
 *       <td style="border: 1px solid;">coupon_count</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">Int</td>
 *       <td style="border: 1px solid;">1</td>
 *       <td style="border: 1px solid;">代金券或立减优惠使用数量</td>
 *       </tr>
 *       </table>
 * 
 *       <h3>返回结果</h3>
 * 
 *       <xml> 
 *       	<return_code><![CDATA[SUCCESS]]></return_code>
 *       	<return_msg><![CDATA[OK]]></return_msg>
 *       	<appid><![CDATA[wx51d650117c444974]]></appid>
 *      	<mch_id><![CDATA[10019315]]></mch_id>
 *       	<nonce_str><![CDATA[pzGgc7cLH9zvoMRE]]></nonce_str>
 *       	<sign><![CDATA[80019D53CF8A1B7FA600D7AE609D8452]]></sign>
 *       	<result_code><![CDATA[SUCCESS]]></result_code>
 *       	<transaction_id><![CDATA[1008960499201503170032740450]]></transaction_id>
 *       	<out_trade_no><![CDATA[10055]]></out_trade_no>
 *       	<out_refund_no><![CDATA[10055]]></out_refund_no>
 *       	<refund_id><![CDATA[2008960499201503180001951935]]></refund_id>
 *       	<refund_channel><![CDATA[]]></refund_channel>
 *       	<refund_fee>1</refund_fee> <coupon_refund_fee>0</coupon_refund_fee>
 *       	<total_fee>1</total_fee> <cash_fee>1</cash_fee>
 *       	<coupon_refund_count>0</coupon_refund_count>
 *       	<cash_refund_fee>1</cash_refund_fee> 
 *       </xml>
 */
public class PayRefundResponse {
	private String return_code;
	private String return_msg;
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
	private String out_refund_no;
	private String refund_id;
	private Integer refund_fee;
	private String refund_fee_type;
	private Integer total_fee;
	private String fee_type;
	private Integer cash_fee;
	private String cash_fee_type;
	private Integer cash_refund_fee;
	private String cash_refund_fee_type;
	private Integer coupon_refund_fee;
	private String coupon_count;
	private Integer coupon_refund_count;
	private String coupon_refund_id;
	private String refund_channel;

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

	public Integer getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(Integer refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getRefund_fee_type() {
		return refund_fee_type;
	}

	public void setRefund_fee_type(String refund_fee_type) {
		this.refund_fee_type = refund_fee_type;
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

	public Integer getCash_refund_fee() {
		return cash_refund_fee;
	}

	public void setCash_refund_fee(Integer cash_refund_fee) {
		this.cash_refund_fee = cash_refund_fee;
	}

	public String getCash_refund_fee_type() {
		return cash_refund_fee_type;
	}

	public void setCash_refund_fee_type(String cash_refund_fee_type) {
		this.cash_refund_fee_type = cash_refund_fee_type;
	}

	public Integer getCoupon_refund_fee() {
		return coupon_refund_fee;
	}

	public void setCoupon_refund_fee(Integer coupon_refund_fee) {
		this.coupon_refund_fee = coupon_refund_fee;
	}

	public String getCoupon_count() {
		return coupon_count;
	}

	public void setCoupon_count(String coupon_count) {
		this.coupon_count = coupon_count;
	}

	public Integer getCoupon_refund_count() {
		return coupon_refund_count;
	}

	public void setCoupon_refund_count(Integer coupon_refund_count) {
		this.coupon_refund_count = coupon_refund_count;
	}

	public String getRefund_channel() {
		return refund_channel;
	}

	public void setRefund_channel(String refund_channel) {
		this.refund_channel = refund_channel;
	}

	public String getCoupon_refund_id() {
		return coupon_refund_id;
	}

	public void setCoupon_refund_id(String coupon_refund_id) {
		this.coupon_refund_id = coupon_refund_id;
	}

	@Override
	public String toString() {
		return "PayRefundResponse [return_code=" + return_code + ", return_msg=" + return_msg + ", result_code="
				+ result_code + ", err_code=" + err_code + ", err_code_des=" + err_code_des + ", appid=" + appid
				+ ", mch_id=" + mch_id + ", device_info=" + device_info + ", nonce_str=" + nonce_str + ", sign=" + sign
				+ ", transaction_id=" + transaction_id + ", out_trade_no=" + out_trade_no + ", out_refund_no="
				+ out_refund_no + ", refund_id=" + refund_id + ", refund_fee=" + refund_fee + ", refund_fee_type="
				+ refund_fee_type + ", total_fee=" + total_fee + ", fee_type=" + fee_type + ", cash_fee=" + cash_fee
				+ ", cash_fee_type=" + cash_fee_type + ", cash_refund_fee=" + cash_refund_fee
				+ ", cash_refund_fee_type=" + cash_refund_fee_type + ", coupon_refund_fee=" + coupon_refund_fee
				+ ", coupon_count=" + coupon_count + ", coupon_refund_count=" + coupon_refund_count
				+ ", coupon_refund_id=" + coupon_refund_id + ", refund_channel=" + refund_channel + "]";
	}
}