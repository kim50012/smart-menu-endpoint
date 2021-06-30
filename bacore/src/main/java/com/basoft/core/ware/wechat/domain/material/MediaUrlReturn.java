package com.basoft.core.ware.wechat.domain.material;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 上传图文消息素材 -response
 */
public class MediaUrlReturn extends WeixinReturn {
	private String url;

	public MediaUrlReturn() {
		super();
	}

	public MediaUrlReturn(int errcode, String errmsg) {
		super(errcode, errmsg);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "MediaUrlReturn [url=" + url + " ]";
	}
}