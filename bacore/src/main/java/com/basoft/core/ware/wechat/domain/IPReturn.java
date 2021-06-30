package com.basoft.core.ware.wechat.domain;

import java.util.List;

public class IPReturn extends WeixinReturn {
	private List<String> ip_list;

	public List<String> getIp_list() {
		return ip_list;
	}

	public void setIp_list(List<String> ip_list) {
		this.ip_list = ip_list;
	}

	@Override
	public String toString() {
		return "IPReturn [ip_list=" + ip_list + ", errcode=" + getErrcode() + ", errmsg=" + getErrmsg() + "]";
	}
}