package com.basoft.core.ware.wechat.domain.menu;

import java.util.List;

/**
 * 子菜单列表
 */
public class SelfmenuSubButtonList {
	/**
	 * 子菜单列表
	 */
	private List<SelfmenuSubButton> list;

	public List<SelfmenuSubButton> getList() {
		return list;
	}

	public void setList(List<SelfmenuSubButton> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "SelfmenuSubButtonList [list=" + list + "]";
	}
}