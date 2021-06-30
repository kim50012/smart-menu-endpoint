package com.basoft.core.ware.wechat.domain.menu;

/**
 * view类型的菜单
 */
public class ViewButton extends Button {
	/**
	 * 菜单的响应动作类型: view类型
	 */
	private String type;

	/**
	 * 网页链接，用户点击菜单可打开链接，不超过256字节
	 */
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}