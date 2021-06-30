package com.basoft.core.ware.wechat.domain.msg.resp;

public class Image {
	// 通过上传多媒体文件，得到的id。
	private String mediaId;

	public Image() {
		super();
	}

	public Image(String mediaId) {
		super();
		this.mediaId = mediaId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
}