package com.basoft.core.ware.wechat.domain.msg.req;

/**
 * 视频消息
 */
public class VideoMessage extends BaseMessage {
	// 媒体ID
	private String mediaId;
	// 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	private String thumbMediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
}