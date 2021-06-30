package com.basoft.core.ware.wechat.domain.group;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 查询所有分组-response
 */
public class GetAllGroupReturn extends WeixinReturn {
	/**
	 * 公众平台分组信息列表
	 */
	private List<Group> groups;

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
}
