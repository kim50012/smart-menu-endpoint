package com.basoft.core.ware.wechat.domain;

public class ApiTicket extends WeixinReturn {
	/**
	 * 公众号用于调用微信JS接口的临时票据
	 */
	private String ticket;
	
	/**
	 * 有效期
	 */
	private int expires_in;
	

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
}