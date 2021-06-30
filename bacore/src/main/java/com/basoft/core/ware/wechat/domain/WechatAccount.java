package com.basoft.core.ware.wechat.domain;

/**
 * Wechat Account
 * 
 * @author basoft
 *
 */
public class WechatAccount {
	private String sysId;

	private String token;

	private String encordingAesKey;

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEncordingAesKey() {
		return encordingAesKey;
	}

	public void setEncordingAesKey(String encordingAesKey) {
		this.encordingAesKey = encordingAesKey;
	}
}