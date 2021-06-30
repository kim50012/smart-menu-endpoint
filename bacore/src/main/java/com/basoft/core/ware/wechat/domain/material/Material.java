package com.basoft.core.ware.wechat.domain.material;

/**
 * 获取永久素材列表（图片、语音、视频）明细
 */
public class Material {
	/**
	 * media id
	 */
	private String media_id;

	/**
	 * 图文消息素材名称
	 */
	private String name;

	/**
	 * 这篇图文消息素材的最后更新时间
	 */
	private int update_time;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "Material [media_id=" + media_id + ", name=" + name + ", update_time=" + update_time + "]";
	}
}
