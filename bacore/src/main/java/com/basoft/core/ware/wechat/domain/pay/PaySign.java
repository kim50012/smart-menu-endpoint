package com.basoft.core.ware.wechat.domain.pay;

import java.util.Arrays;

public class PaySign {
	public static void main(String[] args) {
		String[] array = {
			"appid"
			,"mch_id"	
			,"device_info"	
			,"nonce_str"	
			,"body"	
			,"detail"	
			,"attach"	
			,"out_trade_no"	
			,"fee_type"	
			,"total_fee"	
			,"spbill_create_ip"	
			,"time_start"	
			,"time_expire"	
			,"goods_tag"	
			,"notify_url"	
			,"limit_pay"
			,"trade_type"	
			,"product_id"	
			,"openid"	
		};
		
		Arrays.sort(array);
		for (String item : array) {
			System.out.println(item);
		}
	}
	
	private String appid;  				//公众账号ID 	是 	微信分配的公众账号ID
	private String mch_id;  			//商户号 		是 	微信支付分配的商户号
	private String device_info;  		//设备号 		否  	微信支付分配的终端设备号，商户自定义
	private String nonce_str;  			//随机字符串 		是 	随机字符串，不长于32位。推荐随机数生成算法
	private String sign;  				//签名 			是 	签名，详见签名生成算法
	private String body;  				//商品描述 		是 	商品或支付单简要描述
	private String detail;  			//商品详情 		否 	商品名称明细列表 商品或支付单简要描述
	private String attach;  			//附加数据 		否 	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	private String out_trade_no;  		//商户订单号 		是 	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	private String fee_type;  			//货币类型 		否 	符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	private Integer total_fee;  		//总金额 		是 	订单总金额，只能为整数，详见支付金额
	private String spbill_create_ip;  	//终端IP 		是 	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	private String time_start;  		//交易起始时间 	否 	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	private String time_expire;  		//交易结束时间 	否 	订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
	private String goods_tag;  			//商品标记 		否 	商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
	private String notify_url;  		//通知地址 		是 	接收微信支付异步通知回调地址
	private String trade_type;  		//交易类型 		是 	取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
	private String product_id;  		//商品ID 		否	trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
	private String limit_pay;  			//指定支付方式 	否	 no_credit--指定不能使用信用卡支付
	private String openid;  			//用户标识 		否	 rade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid

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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}
}