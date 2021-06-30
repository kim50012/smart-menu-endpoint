package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取用户增减数据（getusersummary) - response
 */
public class GetUserSummaryReturn extends WeixinReturn {
	private List<UserSummary> list;

	public List<UserSummary> getList() {
		return list;
	}

	public void setList(List<UserSummary> list) {
		this.list = list;
	}
}