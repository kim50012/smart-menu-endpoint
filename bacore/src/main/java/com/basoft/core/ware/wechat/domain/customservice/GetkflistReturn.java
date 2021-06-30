package com.basoft.core.ware.wechat.domain.customservice;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

import java.util.ArrayList;
import java.util.List;

public class GetkflistReturn extends WeixinReturn {
	private List<Kflist> kf_list = new ArrayList<Kflist>();

	public List<Kflist> getKf_list() {
		return kf_list;
	}

	public void setKf_list(List<Kflist> kf_list) {
		this.kf_list = kf_list;
	}
}