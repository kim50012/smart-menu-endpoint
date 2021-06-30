package com.basoft.core.ware.wechat.domain.pay;

public class WeixinOrderInfo {
	private Integer orderId; // 订单号
	private String custOrderId; // 客户可见订单号
	private String out_trade_no;// 商户订单号
	private String transaction_id;// 微信订单号
	private String body;// 商品描述
	private String detail;// 商品详情
	private Integer total_fee;// 总金额 单位为分 例如 100
	private String total_fee2;// 总金额 可见金额 例如 1.00
	private String shopNm; // 店铺名称
	private String out_refund_no;// 商户退款单号
	private String refund_id;// 微信退款单号
	private Integer refund_fee;// 退款金额
	private Integer statusId; // 我们系统的订单状态
	private Integer isRefundReq; // 我们系统的字段

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getCustOrderId() {
		return custOrderId;
	}

	public void setCustOrderId(String custOrderId) {
		this.custOrderId = custOrderId;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
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

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public String getTotal_fee2() {
		return total_fee2;
	}

	public void setTotal_fee2(String total_fee2) {
		this.total_fee2 = total_fee2;
	}

	public String getShopNm() {
		return shopNm;
	}

	public void setShopNm(String shopNm) {
		this.shopNm = shopNm;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getIsRefundReq() {
		return isRefundReq;
	}

	public void setIsRefundReq(Integer isRefundReq) {
		this.isRefundReq = isRefundReq;
	}

	@Override
	public String toString() {
		return "WeixinOrderInfo [orderId=" + orderId + ", custOrderId=" + custOrderId + ", out_trade_no=" + out_trade_no
				+ ", transaction_id=" + transaction_id + ", body=" + body + ", detail=" + detail + ", total_fee="
				+ total_fee + ", total_fee2=" + total_fee2 + ", shopNm=" + shopNm + ", out_refund_no=" + out_refund_no
				+ ", refund_id=" + refund_id + ", refund_fee=" + refund_fee + ", statusId=" + statusId
				+ ", isRefundReq=" + isRefundReq + "]";
	}
}