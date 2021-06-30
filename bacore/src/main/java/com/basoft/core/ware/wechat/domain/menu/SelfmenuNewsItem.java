package com.basoft.core.ware.wechat.domain.menu;

/**
 * 图文消息的信息
 */
public class SelfmenuNewsItem {
	/**
	 * 图文消息的标题
	 */
	private String title;

	/**
	 * 摘要
	 */
	private String digest;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 是否显示封面，0为不显示，1为显示
	 */
	private Integer show_cover;

	/**
	 * 封面图片的URL
	 */
	private String cover_url;

	/**
	 * 正文的URL
	 */
	private String content_url;

	/**
	 * 原文的URL，若置空则无查看原文入口
	 */
	private String source_url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getShow_cover() {
		return show_cover;
	}

	public void setShow_cover(Integer show_cover) {
		this.show_cover = show_cover;
	}

	public String getCover_url() {
		return cover_url;
	}

	public void setCover_url(String cover_url) {
		this.cover_url = cover_url;
	}

	public String getContent_url() {
		return content_url;
	}

	public void setContent_url(String content_url) {
		this.content_url = content_url;
	}

	public String getSource_url() {
		return source_url;
	}

	public void setSource_url(String source_url) {
		this.source_url = source_url;
	}

	@Override
	public String toString() {
		return "SelfmenuNewsItem [title=" + title + ", digest=" + digest + ", author=" + author + ", show_cover="
				+ show_cover + ", cover_url=" + cover_url + ", content_url=" + content_url + ", source_url="
				+ source_url + "]";
	}
}
