package com.basoft.core.ware.wechat.domain.msg.resp;

/**
 * 回复音乐消息
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music music;

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}
}