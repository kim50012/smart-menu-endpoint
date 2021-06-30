package com.basoft.service.dao.group;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.basoft.service.entity.group.Group;
import com.basoft.service.entity.group.GroupExample;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMapper {
    long countByExample(GroupExample example);

    int deleteByExample(GroupExample example);

    int deleteByPrimaryKey(Integer gCorpId);

    int insert(Group record);

    int insertSelective(Group record);

    List<Group> selectByExample(GroupExample example);

    Group selectByPrimaryKey(Integer gCorpId);

    int updateByExampleSelective(@Param("record") Group record, @Param("example") GroupExample example);

    int updateByExample(@Param("record") Group record, @Param("example") GroupExample example);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);

	List<Group> getGroupListByUserId(String userId);

	/**
	 * 根据公司id获取公司信息
	 * 
	 * @param groupId
	 * @return
	 */
	Group getGroupById(String groupId);

	/**
	 * 根据公司id修改公司信息
	 * 
	 * @param group
	 * @return
	 */
	int updateGroupById(@Param("record") Group group);
}