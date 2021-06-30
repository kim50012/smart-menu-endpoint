package com.basoft.core.ware.wechat.domain.pay;

import java.util.Date;

public class BillRefund {
	private Date billDate; // 交易时间
	private String appid; // 公众账号ID
	private String mch_id; // 商户号
	private String sub_mch_id; // 子商户号
	private String device_info; // 设备号
	private String transaction_id; // 微信订单号
	private String out_trade_no; // 商户订单号
	private String openid; // 用户标识
	private String trade_type; // 交易类型
	private String trade_status; // 交易状态
	private String bank_type; // 付款银行
	private String fee_type; // 货币种类
	private String total_fee; // 总金额
	private String coupon_fee; // 企业红包金额
	private Date refund_apply_date; // 退款申请时间
	private Date refund_approved_date; // 退款成功时间
	private String refund_id; // 微信退款单号
	private String out_refund_no; // 商户退款单号
	private String refund_fee; // 退款金额
	private String coupon_refund_fee; // 企业红包退款金额
	private String refund_type; // 退款类型
	private String refund_status; // 退款状态
	private String body; // 商品名称
	private String attach; // 商户数据包
	private String handling_fee; // 手续费
	private String rate; // 费率

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
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

	public String getSub_mch_id() {
		return sub_mch_id;
	}

	public void setSub_mch_id(String sub_mch_id) {
		this.sub_mch_id = sub_mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getCoupon_fee() {
		return coupon_fee;
	}

	public void setCoupon_fee(String coupon_fee) {
		this.coupon_fee = coupon_fee;
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

	public String getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getCoupon_refund_fee() {
		return coupon_refund_fee;
	}

	public void setCoupon_refund_fee(String coupon_refund_fee) {
		this.coupon_refund_fee = coupon_refund_fee;
	}

	public String getRefund_type() {
		return refund_type;
	}

	public void setRefund_type(String refund_type) {
		this.refund_type = refund_type;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getHandling_fee() {
		return handling_fee;
	}

	public void setHandling_fee(String handling_fee) {
		this.handling_fee = handling_fee;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Date getRefund_apply_date() {
		return refund_apply_date;
	}

	public void setRefund_apply_date(Date refund_apply_date) {
		this.refund_apply_date = refund_apply_date;
	}

	public Date getRefund_approved_date() {
		return refund_approved_date;
	}

	public void setRefund_approved_date(Date refund_approved_date) {
		this.refund_approved_date = refund_approved_date;
	}
}