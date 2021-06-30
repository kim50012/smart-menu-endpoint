package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取累计用户数据（getusercumulate） - response
 */
public class GetUserCumulateReturn extends WeixinReturn {
	private List<UserCumulate> list;

	public List<UserCumulate> getList() {
		return list;
	}

	public void setList(List<UserCumulate> list) {
		this.list = list;
	}
}