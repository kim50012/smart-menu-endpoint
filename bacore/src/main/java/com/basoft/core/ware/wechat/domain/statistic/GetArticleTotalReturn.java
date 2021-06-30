package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取图文群发总数据（getarticletotal） - response
 */
public class GetArticleTotalReturn extends WeixinReturn {
	private List<ArticleTotal> list;

	public List<ArticleTotal> getList() {
		return list;
	}

	public void setList(List<ArticleTotal> list) {
		this.list = list;
	}
}