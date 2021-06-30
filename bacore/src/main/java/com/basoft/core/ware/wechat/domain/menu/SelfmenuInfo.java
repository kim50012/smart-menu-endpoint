package com.basoft.core.ware.wechat.domain.menu;

import java.util.List;

public class SelfmenuInfo {
	/**
	 * 菜单按钮
	 */
	private List<SelfmenuButton> button;

	public List<SelfmenuButton> getButton() {
		return button;
	}

	public void setButton(List<SelfmenuButton> button) {
		this.button = button;
	}

	@Override
	public String toString() {
		return "SelfmenuInfo [button=" + button + "]";
	}
}