package com.basoft.core.ware.wechat.domain.material;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取永久素材Response - 图文
 */
public class NewsMaterialReturn extends WeixinReturn {
	/**
	 * news列表
	 */
	private List<NewsItem> news_item;

	public List<NewsItem> getNews_item() {
		return news_item;
	}

	public void setNews_item(List<NewsItem> news_item) {
		this.news_item = news_item;
	}
}