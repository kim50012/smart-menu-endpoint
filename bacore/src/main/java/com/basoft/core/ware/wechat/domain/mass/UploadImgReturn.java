package com.basoft.core.ware.wechat.domain.mass;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class UploadImgReturn extends WeixinReturn {
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "UploadImgReturn [url=" + url + ", getErrcode()=" + getErrcode() + ", getErrmsg()=" + getErrmsg() + "]";
	}
}
