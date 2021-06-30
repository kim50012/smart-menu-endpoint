package com.basoft.core.ware.wechat.domain.user;

import java.util.List;

/**
 * 获取用户列表 response- 列表数据，OPENID的列表
 */
public class Data {
	private List<String> openid;

	public List<String> getOpenid() {
		return openid;
	}

	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}

	@Override
	public String toString() {
		return "Data [openid=" + openid + "]";
	}
}