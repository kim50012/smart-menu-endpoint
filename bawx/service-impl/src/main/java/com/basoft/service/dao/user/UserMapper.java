package com.basoft.service.dao.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.basoft.service.entity.user.User;
import com.basoft.service.entity.user.UserDTO;
import com.basoft.service.entity.user.UserExample;
import com.basoft.service.entity.user.UserListDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(String userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExampleWithBLOBs(UserExample example);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(String userId);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExampleWithBLOBs(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKeyWithBLOBs(User record);

    int updateByPrimaryKey(User record);
    
	/**
	 * 根据用户userId查询用户信息(该用户状态必须可用)
	 * 
	 * @param account
	 * @return
	 */
	User getUserByAccount(String account);
	
	/**
	 * 根据用户userId查询用户信息
	 * 
	 * @param userId
	 * @return
	 */
	User getUserByUserId(String userId);

	/**
	 * 根据查询条件查询用户列表
	 * 
	 * @param userListDTO
	 * @return
	 */
	List<User> getUsers(UserListDTO userListDTO);
	
	/**
	 * 根据查询条件查询用户列表(含用户负责的公众号ID列表)
	 * 
	 * @param userListDTO
	 * @return
	 */
	List<User> getUsersWithShops(UserListDTO userListDTO);

	/**
	 * 根据userId修改用户权限
	 * 
	 * @param userDTO
	 * @return
	 */
	int authUser(UserDTO userDTO);
	
	/**
	 * 根据userId清除所负责公众号
	 * 
	 * @param userId
	 * @return
	 */
	public int deleteUserShop(String userId);

	/**
	 * 根据userId和公众号列表新增所负责公众号
	 * 
	 * @param usMap
	 * @return
	 */
	public int insertUserShop(Map<String,Object> usMap);

	/**
	 * 登录用户自修改密码信息
	 *
	 * @param user
	 * @return
	 */
	public int updateUserPasswordById(User user);
}