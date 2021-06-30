package com.basoft.eorder.interfaces.query.agent;

import java.util.Date;

public class AgentSettlementOrderDTO {
	//columns START
	private Long soId;  //记录编号

	private Long sid;  //结算编号

	private Long orderId;  //订单编号

	private Date orderDate;  //订单时间

	private Long storeId;  //商户ID

	private String openId;  //订单用户

	private Long orderAmount;  //订单总金额

	private Long plFee;  //平台结算金额

	private Long agtFee;  //代理商结算金额

	private Long vatFee;  //VAT金额

	private Date createTime;  //创建时间

	private String createUser;  //创建人

	private Date updateTime;  //修改时间

	private String updateUser;  //修改人

	//columns END

	public Long getSoId() {
		return soId;
	}

	public void setSoId(Long soId) {
		this.soId = soId;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Long getPlFee() {
		return plFee;
	}

	public void setPlFee(Long plFee) {
		this.plFee = plFee;
	}

	public Long getAgtFee() {
		return agtFee;
	}

	public void setAgtFee(Long agtFee) {
		this.agtFee = agtFee;
	}

	public Long getVatFee() {
		return vatFee;
	}

	public void setVatFee(Long vatFee) {
		this.vatFee = vatFee;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}

