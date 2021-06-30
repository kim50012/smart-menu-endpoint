package com.basoft.eorder.interfaces.query.agent;

import java.util.Date;

public class AgentSettlementDetailDTO {
	//columns START
	private Long sid;  //结算编号

	private Long storeId;  //商户ID

	private Integer settleType;  //结算类型 1-按营业额百分比 2-定额，当前业务设定永远按照百分比进行结算

	private Integer plRate;  //平台结算百分比

	private Integer agtRate;  //代理商结算百分比

	private Integer agtPercent;  //代理商结算百分比占平台结算百分比的百分数，例平台结算3%，即订单额的3%给平台，其中一半即订单总额的1.5%给代理商，则该字段值为50，即表示平台结算的50%给代理商

	private Integer orderCount;  //结算订单数量

	private Long settleSum;  //结算总金额

	private Long agtFee;  //代理商结算金额

	private Date createTime;  //结算时间

	private String createUser;  //结算人

	private Date updateTime;  //修改时间

	private String updateUser;  //修改人

	//columns END


	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getSettleType() {
		return settleType;
	}

	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}

	public Integer getPlRate() {
		return plRate;
	}

	public void setPlRate(Integer plRate) {
		this.plRate = plRate;
	}

	public Integer getAgtRate() {
		return agtRate;
	}

	public void setAgtRate(Integer agtRate) {
		this.agtRate = agtRate;
	}

	public Integer getAgtPercent() {
		return agtPercent;
	}

	public void setAgtPercent(Integer agtPercent) {
		this.agtPercent = agtPercent;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Long getSettleSum() {
		return settleSum;
	}

	public void setSettleSum(Long settleSum) {
		this.settleSum = settleSum;
	}

	public Long getAgtFee() {
		return agtFee;
	}

	public void setAgtFee(Long agtFee) {
		this.agtFee = agtFee;
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

