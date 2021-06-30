package com.basoft.core.ware.wechat.domain.customservice;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class GetWaitCaseReturn extends WeixinReturn {
	private Integer count; // 未接入会话数量
	private List<WaitCase> waitcaselist; // 未接入会话列表，最多返回100条数据

	public List<WaitCase> getWaitcaselist() {
		return waitcaselist;
	}

	public void setWaitcaselist(List<WaitCase> waitcaselist) {
		this.waitcaselist = waitcaselist;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
