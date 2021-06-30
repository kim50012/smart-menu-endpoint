package com.basoft.core.ware.wechat.domain.customservice;

import java.util.ArrayList;
import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class GetOnlineKflistReturn extends WeixinReturn {

	private List<OnlineKflist> kf_online_list = new ArrayList<OnlineKflist>();

	public List<OnlineKflist> getKf_online_list() {
		return kf_online_list;
	}

	public void setKf_online_list(List<OnlineKflist> kf_online_list) {
		this.kf_online_list = kf_online_list;
	}
}
