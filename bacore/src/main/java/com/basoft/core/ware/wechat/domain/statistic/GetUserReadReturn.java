package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/** 
 *   获取图文统计数据（getuserread）
 *   获取图文统计分时数据（getuserreadhour）
 */
public class GetUserReadReturn extends WeixinReturn {
	private List<UserRead> list;

	public List<UserRead> getList() {
		return list;
	}

	public void setList(List<UserRead> list) {
		this.list = list;
	}
}