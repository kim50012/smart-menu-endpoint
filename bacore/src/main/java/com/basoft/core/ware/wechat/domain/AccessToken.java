package com.basoft.core.ware.wechat.domain;

public class AccessToken {
	private String SYS_ID = "";
	private String ACCESS_TOKEN = "";

	public String getSYS_ID() {
		return SYS_ID;
	}

	public void setSYS_ID(String sYS_ID) {
		SYS_ID = sYS_ID;
	}

	public String getACCESS_TOKEN() {
		return ACCESS_TOKEN;
	}

	public void setACCESS_TOKEN(String aCCESS_TOKEN) {
		ACCESS_TOKEN = aCCESS_TOKEN;
	}

	public AccessToken() {
		super();
	}

	public AccessToken(String sYS_ID, String aCCESS_TOKEN) {
		super();
		SYS_ID = sYS_ID;
		ACCESS_TOKEN = aCCESS_TOKEN;
	}
}
