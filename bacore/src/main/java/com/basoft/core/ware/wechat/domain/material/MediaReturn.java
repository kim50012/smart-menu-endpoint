package com.basoft.core.ware.wechat.domain.material;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 上传图文消息素材 -response
 */
public class MediaReturn extends WeixinReturn {
	/**
	 * 媒体文件/图文消息上传后获取的唯一标识
	 */
	private String media_id;

	/**
	 * 当type为缩略图（thumb）时 返回
	 */
	private String thumb_media_id;

	/**
	 * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb），次数为news，即图文消息
	 */
	private String type;

	/**
	 * 媒体文件上传时间
	 */
	private int created_at;

	public MediaReturn() {
		super();
	}

	public MediaReturn(int errcode, String errmsg) {
		super(errcode, errmsg);
	}

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCreated_at() {
		return created_at;
	}

	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "MediaReturn [media_id=" + media_id + ", thumb_media_id=" + thumb_media_id + ", type=" + type
				+ ", created_at=" + created_at + "]";
	}
}