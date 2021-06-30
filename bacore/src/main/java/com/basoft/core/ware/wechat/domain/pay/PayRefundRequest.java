package com.basoft.core.ware.wechat.domain.pay;

/**
 *       <h2>申请退款 Reqeust</h2> <h3>应用场景</h3>
 *       当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，微信支付将在收到退款请求并且验证成功之后，
 *       按照退款规则将支付款按原路退到买家帐号上。 <br>
 *       注意：<br>
 *       1.交易时间超过半年的订单无法提交退款；<br>
 *       2.微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。一笔退款失败后重新提交，
 *       要采用原来的退款单号。总退款金额不能超过用户实际支付金额。 <h3>接口地址</h3> 接口链接：
 *       <code>https://api.mch.weixin.qq.com/secapi/pay/refund</code> <br>
 *       <h3>是否需要证书</h3> 请求需要双向证书。
 * 
 *       <h3>请求参数</h3>
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
 *       <td style="border: 1px solid;">微信支付分配的终端设备号，<br>
 *       与下单一致</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">随机字符串</td>
 *       <td style="border: 1px solid;">nonce_str</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">5K8264ILTKCH16CQ2502SI8ZNMTM67VS</td>
 *       <td style="border: 1px solid;">随机字符串，不长于32位。<br>
 *       推荐随机数生成算法</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">签名</td>
 *       <td style="border: 1px solid;">sign</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">C380BEC2BFD727A4B6845133519F3AD6</td>
 *       <td style="border: 1px solid;">签名，详见签名生成算法</td>
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
 *       <td style="border: 1px solid;">商户系统内部的订单号,<br>
 *       transaction_id、out_trade_no二选一，<br>
 *       如果同时存在优先级：<br>
 *       transaction_id> out_trade_no</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">商户退款单号</td>
 *       <td style="border: 1px solid;">out_refund_no</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">1217752501201407033233368018</td>
 *       <td style="border: 1px solid;">商户系统内部的退款单号，商户系统内部唯一，<br>
 *       同一退款单号多次请求只退一笔</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">总金额</td>
 *       <td style="border: 1px solid;">total_fee</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">Int</td>
 *       <td style="border: 1px solid;">100</td>
 *       <td style="border: 1px solid;">订单总金额，单位为分，<br>
 *       只能为整数，详见支付金额</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">退款金额</td>
 *       <td style="border: 1px solid;">refund_fee</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">Int</td>
 *       <td style="border: 1px solid;">100</td>
 *       <td style="border: 1px solid;">退款总金额，订单总金额，单位为分，<br>
 *       只能为整数，详见支付金额</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">货币种类</td>
 *       <td style="border: 1px solid;">refund_fee_type</td>
 *       <td style="border: 1px solid;">否</td>
 *       <td style="border: 1px solid;">String(8)</td>
 *       <td style="border: 1px solid;">CNY</td>
 *       <td style="border: 1px solid;">货币类型，符合ISO 4217标准的三位字母代码，<br>
 *       默认人民币：CNY，其他值列表详见货币类型</td>
 *       </tr>
 *       <tr>
 *       <td style="border: 1px solid;">操作员</td>
 *       <td style="border: 1px solid;">op_user_id</td>
 *       <td style="border: 1px solid;">是</td>
 *       <td style="border: 1px solid;">String(32)</td>
 *       <td style="border: 1px solid;">1900000109</td>
 *       <td style="border: 1px solid;">操作员帐号, 默认为商户号</td>
 *       </tr>
 *       </table>
 *
 *       <h3>举例如下：</h3> &lt;xml><br>
 *       &nbsp;&nbsp;&lt;appid>wx2421b1c4370ec43b&lt;/appid><br>
 *       &nbsp;&nbsp;&lt;mch_id>10000100&lt;/mch_id><br>
 *       &nbsp;&nbsp;&lt;nonce_str>6cefdb308e1e2e8aabd48cf79e546a02&lt;/
 *       nonce_str><br>
 *       &nbsp;&nbsp;&lt;op_user_id>10000100&lt;/op_user_id><br>
 *       &nbsp;&nbsp;&lt;out_refund_no>1415701182&lt;/out_refund_no><br>
 *       &nbsp;&nbsp;&lt;out_trade_no>1415757673&lt;/out_trade_no><br>
 *       &nbsp;&nbsp;&lt;refund_fee>1&lt;/refund_fee><br>
 *       &nbsp;&nbsp;&lt;total_fee>1&lt;/total_fee><br>
 *       &nbsp;&nbsp;&lt;transaction_id>&lt;/transaction_id><br>
 *       &nbsp;&nbsp;&lt;sign>FE56DD4AA85C0EECA82C35595A69E153&lt;/sign><br>
 *       &lt;/xml>
 */
public class PayRefundRequest {
	private String appid;
	private String mch_id;
	private String device_info;
	private String nonce_str;
	private String sign;
	private String transaction_id;
	private String out_trade_no;
	private String out_refund_no;
	private Integer total_fee;
	private Integer refund_fee;
	private String refund_fee_type;
	private String op_user_id;

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

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
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

	public String getOp_user_id() {
		return op_user_id;
	}

	public void setOp_user_id(String op_user_id) {
		this.op_user_id = op_user_id;
	}
}