package com.basoft.core.ware.wechat.domain.msg.resp;

/**
 * 回复视频消息
 */
public class VideoMessage extends BaseMessage {
	private Video video;

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}
}