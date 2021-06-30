package com.basoft.core.ware.wechat.domain.mass;

/**
 * 根据OpenID列表群发 -图文消息 POST数据封装类
 */
public class NewsMassDomain extends MassBase {
	/**
	 * 用于设定即将发送的图文消息 是否必须 - 是
	 */
	private Media mpnews;

	public NewsMassDomain() {
		super();
		super.setMsgtype("mpnews");
	}

	public Media getMpnews() {
		return mpnews;
	}

	public void setMpnews(Media mpnews) {
		this.mpnews = mpnews;
	}

	@Override
	public String toString() {
		return "NewsMassDomain [mpnews=" + mpnews + ", getMpnews()=" + getMpnews() + "]";
	}
}