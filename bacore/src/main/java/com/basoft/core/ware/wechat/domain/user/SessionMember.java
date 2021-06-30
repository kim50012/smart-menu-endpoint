package com.basoft.core.ware.wechat.domain.user;

public class SessionMember implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer custSysId;
	private String custLoginId;
	private String openid;
	private String sysId;
	private Integer shopId;
	private String custImgUrl;
	private String nickname;

	public Integer getCustSysId() {
		return custSysId;
	}

	public void setCustSysId(Integer custSysId) {
		this.custSysId = custSysId;
	}

	public String getCustLoginId() {
		return custLoginId;
	}

	public void setCustLoginId(String custLoginId) {
		this.custLoginId = custLoginId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getCustImgUrl() {
		return custImgUrl;
	}

	public void setCustImgUrl(String custImgUrl) {
		this.custImgUrl = custImgUrl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "SessionMember [custSysId=" + custSysId + ", custLoginId=" + custLoginId + ", openid=" + openid
				+ ", sysId=" + sysId + ", shopId=" + shopId + ", custImgUrl=" + custImgUrl + ", nickname=" + nickname
				+ "]";
	}
}