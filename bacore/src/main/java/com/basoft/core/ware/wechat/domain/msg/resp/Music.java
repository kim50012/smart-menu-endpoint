package com.basoft.core.ware.wechat.domain.msg.resp;

public class Music {
	// 音乐标题
	private String title;
	// 音乐描述
	private String description;
	// 音乐链接
	private String musicUrl;
	// 高质量音乐链接，WIFI环境优先使用该链接播放音乐
	private String HQMusicUrl;
	// 缩略图的媒体id，通过上传多媒体文件，得到的id
	private String thumbMediaId;

	public String getTitle() {
		return null == title ? "" : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return null == description ? "" : description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMusicUrl() {
		return null == musicUrl ? "" : musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	public String getHQMusicUrl() {
		return null == HQMusicUrl ? "" : HQMusicUrl;
	}

	public void setHQMusicUrl(String musicUrl) {
		HQMusicUrl = musicUrl;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
}