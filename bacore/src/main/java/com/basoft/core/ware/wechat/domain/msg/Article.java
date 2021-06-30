package com.basoft.core.ware.wechat.domain.msg;

/**
 * 回复图文消息详细
 * 参数			是否必须	说明
	Title			否	图文消息标题
	Description		否	图文消息描述
	PicUrl			否	图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	Url				否	点击图文消息跳转链接
 *
 */
public class Article {
	private String title = "";
	private String description = "";
	private String picurl = "";
	private String url = "";

	public Article() {
		super();
	}

	public Article(String title) {
		super();
		this.title = title;
	}

	public Article(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public Article(String title, String description, String picurl) {
		super();
		this.title = title;
		this.description = description;
		this.picurl = picurl;
	}

	public Article(String title, String description, String picurl, String url) {
		super();
		this.title = title;
		this.description = description;
		this.picurl = picurl;
		this.url = url;
	}

	@Override
	public String toString() {
		String format = "<item>\n" + "<Title><![CDATA[%1$s]]></Title>\n"
				+ "<Description><![CDATA[%2$s]]></Description>\n" + "<PicUrl><![CDATA[%3$s]]></PicUrl>\n"
				+ "<Url><![CDATA[%4$s]]></Url>\n" + "</item>\n";
		return String.format(format, title, description, picurl, url);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}