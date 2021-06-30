package com.basoft.eorder.interfaces.query.agent;

import java.util.Date;

public class AgentDTO {
	//columns START
	private Long agtId;  //agtId
	private Integer agtType;  //代理商类型：1-SA   2-CA
	private String agtName;  //代理商名称
	private String agtCode;  //编码
	private String agtAccount;  //账号
	private Integer caChargeRate;  //手续费百分比-CA才有用
	private String agtPassword;  //密码
	private String agtMobile;  //手机号
	private String agtEmail;  //邮件
	private String agtBankCode;  //银行账号
	private String agtQrcodeUrl;  //代理商二维码地址
	private String agtTicket;
	private String agtQrcodeId;
	private String contractSt;  //合同起始时间
	private String contractEd;  //合同结束时间
	private String renewal;// -- 1正常 2 还有一个月到期  3过期
	private Integer status;  //状态：1-正常   2-禁用   3-删除
	private Date createTime;  //创建时间
	private String createUser;  //创建人
	private Date updateTime;  //修改时间
	private String modifiedUserId;  //修改人
	//columns END


	public Long getAgtId() {
		return agtId;
	}

	public void setAgtId(Long agtId) {
		this.agtId = agtId;
	}

	public Integer getAgtType() {
		return agtType;
	}

	public void setAgtType(Integer agtType) {
		this.agtType = agtType;
	}

	public String getAgtName() {
		return agtName;
	}

	public void setAgtName(String agtName) {
		this.agtName = agtName;
	}

	public String getAgtCode() {
		return agtCode;
	}

	public void setAgtCode(String agtCode) {
		this.agtCode = agtCode;
	}

	public String getAgtAccount() {
		return agtAccount;
	}

	public void setAgtAccount(String agtAccount) {
		this.agtAccount = agtAccount;
	}

	public Integer getCaChargeRate() {
		return caChargeRate;
	}

	public void setCaChargeRate(Integer caChargeRate) {
		this.caChargeRate = caChargeRate;
	}

	public String getAgtPassword() {
		return agtPassword;
	}

	public void setAgtPassword(String agtPassword) {
		this.agtPassword = agtPassword;
	}

	public String getAgtMobile() {
		return agtMobile;
	}

	public void setAgtMobile(String agtMobile) {
		this.agtMobile = agtMobile;
	}

	public String getAgtEmail() {
		return agtEmail;
	}

	public void setAgtEmail(String agtEmail) {
		this.agtEmail = agtEmail;
	}

	public String getAgtBankCode() {
		return agtBankCode;
	}

	public void setAgtBankCode(String agtBankCode) {
		this.agtBankCode = agtBankCode;
	}

	public String getAgtQrcodeUrl() {
		return agtQrcodeUrl;
	}

	public void setAgtQrcodeUrl(String agtQrcodeUrl) {
		this.agtQrcodeUrl = agtQrcodeUrl;
	}

	public String getAgtTicket() {
		return agtTicket;
	}

	public void setAgtTicket(String agtTicket) {
		this.agtTicket = agtTicket;
	}

	public String getAgtQrcodeId() {
		return agtQrcodeId;
	}

	public void setAgtQrcodeId(String agtQrcodeId) {
		this.agtQrcodeId = agtQrcodeId;
	}

	public String getContractSt() {
		return contractSt;
	}

	public void setContractSt(String contractSt) {
		this.contractSt = contractSt;
	}

	public String getContractEd() {
		return contractEd;
	}

	public void setContractEd(String contractEd) {
		this.contractEd = contractEd;
	}

	public String getRenewal() {
		return renewal;
	}

	public void setRenewal(String renewal) {
		this.renewal = renewal;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getModifiedUserId() {
		return modifiedUserId;
	}

	public void setModifiedUserId(String modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}
}