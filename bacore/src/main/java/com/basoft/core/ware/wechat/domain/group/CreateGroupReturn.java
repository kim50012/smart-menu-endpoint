package com.basoft.core.ware.wechat.domain.group;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 创建分组-response
 */
public class CreateGroupReturn extends WeixinReturn {
	// 分组信息
	private Group group;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "CreateGroupReturn [group=" + group + "]";
	}
}
