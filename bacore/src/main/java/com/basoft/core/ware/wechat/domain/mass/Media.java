package com.basoft.core.ware.wechat.domain.mass;

/**
 * 根据OpenID列表群发 POST数据封装类
 */
public class Media {
	/**
	 * 用于群发的图文消息的media_id 是否必须 是
	 */
	private String media_id;

	public Media() {
		super();
	}

	public Media(String media_id) {
		super();
		this.media_id = media_id;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
}