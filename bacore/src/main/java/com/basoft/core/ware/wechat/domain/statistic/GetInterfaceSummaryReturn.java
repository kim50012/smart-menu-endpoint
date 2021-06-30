package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取接口分析数据（getinterfacesummary） - response
 */
public class GetInterfaceSummaryReturn extends WeixinReturn {
	private List<InterfaceSummary> list;

	public List<InterfaceSummary> getList() {
		return list;
	}

	public void setList(List<InterfaceSummary> list) {
		this.list = list;
	}
}