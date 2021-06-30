package com.basoft.eorder.interfaces.query.agent;

import java.util.Date;

public class AgentSettlementDTO {
	//columns START
	private Long sid;  //结算编号

	private Long agtId;  //所属代理商ID

	private String agtCode;  //所属代理商编码

	private String settleYearMonth;  //结算年月

	private Integer settleYear;  //结算年

	private Integer settleMonth;  //结算月

	private String startDt;  //结算起始日期

	private String endDt;  //结算结束日期

	private Integer orderCount;  //结算订单数量

	private Long settleSum;  //结算总金额

	private Long agtFee;  //代理商结算金额

	private Long agtVatFee;  //VAT金额

	private String plDate;  //平台结算日期

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

	public Long getAgtId() {
		return agtId;
	}

	public void setAgtId(Long agtId) {
		this.agtId = agtId;
	}

	public String getAgtCode() {
		return agtCode;
	}

	public void setAgtCode(String agtCode) {
		this.agtCode = agtCode;
	}

	public String getSettleYearMonth() {
		return settleYearMonth;
	}

	public void setSettleYearMonth(String settleYearMonth) {
		this.settleYearMonth = settleYearMonth;
	}

	public Integer getSettleYear() {
		return settleYear;
	}

	public void setSettleYear(Integer settleYear) {
		this.settleYear = settleYear;
	}

	public Integer getSettleMonth() {
		return settleMonth;
	}

	public void setSettleMonth(Integer settleMonth) {
		this.settleMonth = settleMonth;
	}

	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
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

	public Long getAgtVatFee() {
		return agtVatFee;
	}

	public void setAgtVatFee(Long agtVatFee) {
		this.agtVatFee = agtVatFee;
	}

	public String getPlDate() {
		return plDate;
	}

	public void setPlDate(String plDate) {
		this.plDate = plDate;
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

