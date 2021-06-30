package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/** 
 *   获取图文分享转发分时数据（getusersharehour）
 *   获取图文分享转发数据（getusershare）
 */
public class GetUserShareReturn extends WeixinReturn {
	private List<UserShare> list;

	public List<UserShare> getList() {
		return list;
	}

	public void setList(List<UserShare> list) {
		this.list = list;
	}
}