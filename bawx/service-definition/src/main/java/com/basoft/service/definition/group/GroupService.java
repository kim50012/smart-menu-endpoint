package com.basoft.service.definition.group;

import java.util.List;


import com.basoft.service.entity.group.Group;

public interface GroupService {

	/**
	 * 根据用户ID查询可展示（管理）的公司列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Group> getGroupListByUserId(String userId);

	/**
	 * 根据id获取公司信息
	 * 
	 * @param groupId
	 * @return
	 */
	public Group getGroupById(String groupId);

	/**
	 * 根据公司id修改公司信息
	 * 
	 * @param group
	 * @return
	 */
	public int updateGroupById(Group group);
}