package com.basoft.core.ware.wechat.domain.qrcode;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class ShortUrlReturn extends WeixinReturn {
	/**
	 * 短链接
	 */
	private String short_url;

	public String getShort_url() {
		return short_url;
	}

	public void setShort_url(String short_url) {
		this.short_url = short_url;
	}
}