package com.basoft.core.ware.wechat.domain.user;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 批量获取用户基本信息 - response
 */
public class UserBatchReturn extends WeixinReturn {
	private List<UserReturn> user_info_list;

	public List<UserReturn> getUser_info_list() {
		return user_info_list;
	}

	public void setUser_info_list(List<UserReturn> user_info_list) {
		this.user_info_list = user_info_list;
	}
}