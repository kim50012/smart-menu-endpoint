package com.basoft.core.ware.wechat.domain.material;

import java.util.List;

/**
 * 一个图文条目下的news列表
 */
public class NewsItemContent {
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