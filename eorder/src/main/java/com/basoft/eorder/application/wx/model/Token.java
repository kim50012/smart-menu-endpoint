package com.basoft.eorder.application.wx.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Token extends BaseResult {

	private String access_token;
	private int expires_in;


	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String accessToken) {
		access_token = accessToken;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expiresIn) {
		expires_in = expiresIn;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
