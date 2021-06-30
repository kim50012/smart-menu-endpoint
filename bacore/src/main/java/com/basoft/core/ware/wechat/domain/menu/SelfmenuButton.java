package com.basoft.core.ware.wechat.domain.menu;

public class SelfmenuButton {
	/**
	 * 菜单的类型，公众平台官网上能够设置的菜单类型有<br>
	 * view（跳转网页）、text（返回文本，下同）、img、photo、video、voice。<br>
	 * 使用API设置的则有8种，详见《自定义菜单创建接口》
	 */
	private String type;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * Text:保存文字到value； Img、voice：保存mediaID到value； Video：保存视频下载链接到value；
	 */
	private String value;

	/**
	 * View：保存链接到url。
	 */
	private String url;

	/**
	 * 使用API设置的自定义菜单： click、scancode_push、
	 * scancode_waitmsg、pic_sysphoto、pic_photo_or_album、
	 * pic_weixin、location_select：保存值到key
	 */
	private String key;

	/**
	 * 图文消息的信息
	 */
	private SelfmenuNewsInfo news_info;

	/**
	 * 子菜单
	 */
	private SelfmenuSubButtonList sub_button;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public SelfmenuNewsInfo getNews_info() {
		return news_info;
	}

	public void setNews_info(SelfmenuNewsInfo news_info) {
		this.news_info = news_info;
	}

	public SelfmenuSubButtonList getSub_button() {
		return sub_button;
	}

	public void setSub_button(SelfmenuSubButtonList sub_button) {
		this.sub_button = sub_button;
	}

	@Override
	public String toString() {
		return "SelfmenuButton [type=" + type + ", name=" + name + ", value=" + value + ", url=" + url + ", key=" + key
				+ ", news_info=" + news_info + ", sub_button=" + sub_button + "]";
	}
}