package com.basoft.service.dto.login;

public class LoginParam {
	private String account;

	private String password;

	private String verificationCode;

	private String loginType;
	
	private String loginChannel;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getLoginChannel() {
		return loginChannel;
	}

	public void setLoginChannel(String loginChannel) {
		this.loginChannel = loginChannel;
	}
}