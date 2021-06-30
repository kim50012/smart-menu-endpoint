package com.basoft.core.ware.wechat.domain.msg.resp;

/**
 * 回复文本消息
 */
public class TextMessage extends BaseMessage {
	// 回复的消息内容
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}