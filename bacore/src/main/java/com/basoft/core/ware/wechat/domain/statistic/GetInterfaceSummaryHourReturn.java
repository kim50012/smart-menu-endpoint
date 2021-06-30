package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取接口分析分时数据（getinterfacesummaryhour） - response
 */
public class GetInterfaceSummaryHourReturn extends WeixinReturn {
	private List<InterfaceSummaryHour> list;

	public List<InterfaceSummaryHour> getList() {
		return list;
	}

	public void setList(List<InterfaceSummaryHour> list) {
		this.list = list;
	}
}