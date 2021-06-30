package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 消息分析数据接口 - response
 */
public class GetUpstreamMsgReturn extends WeixinReturn {
	private List<UpstreamMsg> list;

	public List<UpstreamMsg> getList() {
		return list;
	}

	public void setList(List<UpstreamMsg> list) {
		this.list = list;
	}
}