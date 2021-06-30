package com.basoft.core.ware.wechat.domain.statistic;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

import java.util.List;

public class WxIfStreamMsgStatsDataReturn extends WeixinReturn {
	
   private List<WxIfStreamMsgStatsData> list;

public List<WxIfStreamMsgStatsData> getList() {
	return list;
}

public void setList(List<WxIfStreamMsgStatsData> list) {
	this.list = list;
}
   
   
}