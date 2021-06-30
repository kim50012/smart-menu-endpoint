package com.basoft.core.ware.wechat.domain.material;

/**
 * 永久图文消息素材列表 - News 明细
 */
public class NewsItem {
	/**
	 * 图文消息的标题
	 */
	private String title;

	/**
	 * 图文消息的封面图片素材id（必须是永久mediaID）
	 */
	private String thumb_media_id;

	/**
	 * 是否显示封面，0为false，即不显示，1为true，即显示
	 */
	private String show_cover_pic;

	/**
	 * 否 图文消息的作者
	 */
	private String author;

	/**
	 * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
	 */
	private String digest;

	/**
	 * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
	 */
	private String content;

	/**
	 * 图文消息的原文地址，即点击“阅读原文”后的URL
	 */
	private String content_source_url;

	/**
	 * 这篇图文消息素材的最后更新时间
	 */
	private int update_time;

	/**
	 * 文件名称
	 */
	private String name;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}

	public String getShow_cover_pic() {
		return show_cover_pic;
	}

	public void setShow_cover_pic(String show_cover_pic) {
		this.show_cover_pic = show_cover_pic;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent_source_url() {
		return content_source_url;
	}

	public void setContent_source_url(String content_source_url) {
		this.content_source_url = content_source_url;
	}

	public int getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "NewsItem [title=" + title + ", thumb_media_id=" + thumb_media_id + ", show_cover_pic=" + show_cover_pic
				+ ", author=" + author + ", digest=" + digest + ", content=" + content + ", content_source_url="
				+ content_source_url + ", update_time=" + update_time + ", name=" + name + "]";
	}
}
