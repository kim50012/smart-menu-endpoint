package com.basoft.core.ware.wechat.domain;

/**
 * 微信分享信息封装类
 */
public class ShareInfo {
	private String title; // 分享标题
	private String desc; // 分享描述
	private String link; // 分享链接
	private String imgUrl; // 分享图标
	private String type = "link"; // 分享类型,music、video或link，不填默认为link
	private String dataUrl = "";// 如果type是music或video，则要提供数据链接，默认为空

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
}