package com.basoft.service.dto.login;

import java.io.Serializable;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId;

	private String userNickNm;

	private String userRealNm;

	private String wxOpenidU;

	private String wxIdU;

	private String email;

	private String mobileNo;

	private String qqId;
	
    private Integer compId;

	private String loginToken;
	
	private String userAuth;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNickNm() {
		return userNickNm;
	}

	public void setUserNickNm(String userNickNm) {
		this.userNickNm = userNickNm;
	}

	public String getUserRealNm() {
		return userRealNm;
	}

	public void setUserRealNm(String userRealNm) {
		this.userRealNm = userRealNm;
	}

	public String getWxOpenidU() {
		return wxOpenidU;
	}

	public void setWxOpenidU(String wxOpenidU) {
		this.wxOpenidU = wxOpenidU;
	}

	public String getWxIdU() {
		return wxIdU;
	}

	public void setWxIdU(String wxIdU) {
		this.wxIdU = wxIdU;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getQqId() {
		return qqId;
	}

	public void setQqId(String qqId) {
		this.qqId = qqId;
	}

	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(String userAuth) {
		this.userAuth = userAuth;
	}
}