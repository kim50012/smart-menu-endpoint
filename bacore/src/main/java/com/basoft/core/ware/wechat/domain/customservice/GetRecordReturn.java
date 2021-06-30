package com.basoft.core.ware.wechat.domain.customservice;

import java.util.ArrayList;
import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class GetRecordReturn extends WeixinReturn {
	private List<Record> recordlist = new ArrayList<Record>();
	private Integer retcode;

	public List<Record> getRecordlist() {
		return recordlist;
	}

	public void setRecordlist(List<Record> recordlist) {
		this.recordlist = recordlist;
	}

	public Integer getRetcode() {
		return retcode;
	}

	public void setRetcode(Integer retcode) {
		this.retcode = retcode;
	}
}
