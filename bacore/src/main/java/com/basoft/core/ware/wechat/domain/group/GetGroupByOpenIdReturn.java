package com.basoft.core.ware.wechat.domain.group;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 查询用户所在分组 - response
 */
public class GetGroupByOpenIdReturn extends WeixinReturn {
	/**
	 * 用户所属的groupid
	 */
	private Integer groupid;

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
}
