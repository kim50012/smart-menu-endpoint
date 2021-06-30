package com.basoft.core.ware.wechat.domain.pay;

public class PayRefundInsert {
	private Integer custSysId;
	private Integer orderId;
	private Integer shopId;

	private PayRefundResponse refundInfo;

	public Integer getCustSysId() {
		return custSysId;
	}

	public void setCustSysId(Integer custSysId) {
		this.custSysId = custSysId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public PayRefundResponse getRefundInfo() {
		return refundInfo;
	}

	public void setRefundInfo(PayRefundResponse refundInfo) {
		this.refundInfo = refundInfo;
	}
}