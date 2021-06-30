package com.basoft.core.ware.wechat.domain.material;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取永久素材Response - 视频消息素材
 */
public class VideoMaterialReturn extends WeixinReturn {
	private String title;
	private String description;
	private String down_url;
	private String filepath;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDown_url() {
		return down_url;
	}

	public void setDown_url(String down_url) {
		this.down_url = down_url;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	@Override
	public String toString() {
		return "VideoMaterialReturn [title=" + title + ", description=" + description + ", down_url=" + down_url
				+ ", filepath=" + filepath + "]";
	}
}