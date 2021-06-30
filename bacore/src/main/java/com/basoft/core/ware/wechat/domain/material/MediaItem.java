package com.basoft.core.ware.wechat.domain.material;

/**
 * 获取素材列表-返回数据封装（永久图文消息素）-图文条目
 */
public class MediaItem {
	/**
	 * media id
	 */
	private String media_id;

	/**
	 * 图文条目内容
	 */
	private NewsItemContent content;

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

	public NewsItemContent getContent() {
		return content;
	}

	public void setContent(NewsItemContent content) {
		this.content = content;
	}

	public int getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "MediaItem [media_id=" + media_id + ", content=" + content + ", update_time=" + update_time + "]";
	}
}
