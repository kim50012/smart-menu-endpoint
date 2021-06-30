package com.basoft.core.ware.wechat.domain.mass;

import java.util.List;

/**
 * 根据OpenID列表群发 - POST Data Base Domain
 */
public class MassBase {
	/**
	 * 填写图文消息的接收者，一串OpenID列表，OpenID最少个，最多10000个 是否必须 是
	 */
	private List<String> touser;

	/**
	 * 群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为video 是否必须 是
	 */
	private String msgtype;

	public List<String> getTouser() {
		return touser;
	}

	public void setTouser(List<String> touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
}
