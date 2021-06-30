package com.basoft.core.ware.wechat.domain.mass;

import java.util.List;

/**
 * 上传图文消息素材 POST Domain
 */
public class NewsData {
	/**
	 * 图文消息，一个图文消息支持1到10条图文
	 */
	private List<Article> articles;

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
}
