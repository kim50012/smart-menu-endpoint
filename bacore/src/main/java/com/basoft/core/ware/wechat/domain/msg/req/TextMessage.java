package com.basoft.core.ware.wechat.domain.msg.req;

/**
 * 文本消息
 */
public class TextMessage extends BaseMessage {
	// 消息内容
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
