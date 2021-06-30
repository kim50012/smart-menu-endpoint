package com.basoft.core.ware.wechat.domain;

public class OpenidPair implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String openid;
	private String fromOpenid;

	public OpenidPair(String openid, String fromOpenid) {
		super();
		this.openid = openid;
		this.fromOpenid = fromOpenid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getFromOpenid() {
		return fromOpenid;
	}

	public void setFromOpenid(String fromOpenid) {
		this.fromOpenid = fromOpenid;
	}

	@Override
	public String toString() {
		return "OpenidPair [openid=" + openid + ", fromOpenid=" + fromOpenid + "]";
	}
}