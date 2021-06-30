package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取图文群发每日数据（getarticlesummary） - response
 */
public class GetArticleSummaryReturn extends WeixinReturn {
	private List<ArticleSummary> list;

	public List<ArticleSummary> getList() {
		return list;
	}

	public void setList(List<ArticleSummary> list) {
		this.list = list;
	}
}