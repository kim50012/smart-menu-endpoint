package com.basoft.service.impl.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basoft.service.dao.group.GroupMapper;
import com.basoft.service.definition.group.GroupService;
import com.basoft.service.entity.group.Group;

@Service
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupMapper groupMapper;

	/**
	 * 根据用户ID查询可展示（管理）的公司列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Group> getGroupListByUserId(String userId) {
		return groupMapper.getGroupListByUserId(userId);
	}
	
	/**
	 * 根据id获取公司信息
	 * 
	 * @param groupId
	 * @return
	 */
	public Group getGroupById(String groupId) {
		return groupMapper.getGroupById(groupId);
	}
	
	/**
	 * 根据公司id修改公司信息
	 * 
	 * @param group
	 * @return
	 */
	public int updateGroupById(Group group) {
		return groupMapper.updateGroupById(group);
	}
}