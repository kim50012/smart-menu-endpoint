package com.basoft.core.ware.wechat.domain.menu;

import java.util.List;

public class SelfmenuNewsInfo {
	/**
	 * 图文消息的信息列表
	 */
	private List<SelfmenuNewsItem> list;

	public List<SelfmenuNewsItem> getList() {
		return list;
	}

	public void setList(List<SelfmenuNewsItem> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "SelfmenuNewsInfo [list=" + list + "]";
	}
}