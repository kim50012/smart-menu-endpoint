package com.basoft.eorder.application.wx.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 二维码 ticket
 */
public class QrcodeTicket extends BaseResult {
	
	private String ticket;
	
	private Integer expire_seconds;

	private String url;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(Integer expireSeconds) {
		expire_seconds = expireSeconds;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() { return ReflectionToStringBuilder.toString(this); }
}
