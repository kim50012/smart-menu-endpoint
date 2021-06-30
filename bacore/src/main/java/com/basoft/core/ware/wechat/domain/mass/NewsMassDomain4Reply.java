package com.basoft.core.ware.wechat.domain.mass;

/**
 * 根据OpenID单个回复，用于自动回复（关键字、关注和自动回复） -图文消息 POST数据封装类
 */
public class NewsMassDomain4Reply {
	/**
	 * 图文消息为mpnews
	 */
	private String msgtype;

	/**
	 * 用于设定即将发送的图文消息 是否必须 - 是
	 */
	private Media mpnews;

	private String touser;

	public NewsMassDomain4Reply() {
		setMsgtype("mpnews");
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public Media getMpnews() {
		return mpnews;
	}

	public void setMpnews(Media mpnews) {
		this.mpnews = mpnews;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	@Override
	public String toString() {
		return "NewsMassDomain [mpnews=" + mpnews + ", getMpnews()=" + getMpnews() + "]";
	}
}