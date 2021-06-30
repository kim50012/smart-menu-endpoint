package com.basoft.core.ware.wechat.domain.material;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class MaterialReturn extends WeixinReturn {
	/**
	 * 永久素材的media_id
	 */
	private String media_id;
	
	/**
	 * 图片类型永久素材的url
	 */
	private String url;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	
	public String getUrl() {
		return url;
	}
	
	 public void setUrl(String url) {
		this.url = url;
	}
}