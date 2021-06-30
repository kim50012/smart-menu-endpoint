package com.basoft.service.definition.user;

import java.util.List;

import com.basoft.service.entity.user.User;
import com.basoft.service.entity.user.UserDTO;
import com.basoft.service.entity.user.UserListDTO;
import com.github.pagehelper.PageInfo;

public interface UserService {
	
	/**
	 * 根据查询条件查询用户列表
	 * 
	 * @param userListDTO
	 * @return
	 */
	public PageInfo<User> getUsers(UserListDTO userListDTO);
	
	/**
	 * 根据查询条件查询用户列表(含用户负责的公众号ID列表)
	 * 
	 * @param userListDTO
	 * @return
	 */
	public PageInfo<User> getUsersWithShops(UserListDTO userListDTO);
	
	/**
	 * 根据账号查询用户信息(该用户状态必须可用)
	 * 
	 * @param account
	 * @return
	 */
	public User getUserByAccount(String account);
	
	/**
	 * 根据用户userId查询用户信息
	 * 
	 * @param userId
	 * @return
	 */
	User getUserByUserId(String userId);

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	public int addUser(User user);

	/**
	 * 根据userId修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateUserById(User user);

	/**
	 * 根据userId删除用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public int deleteUserById(String userId);

	/**
	 * 根据userId修改用户权限
	 * 
	 * @param userDTO
	 * @return
	 */
	public int authUser(UserDTO userDTO);

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
	 * @param userId
	 * @param shopList
	 * @return
	 */
	public int insertUserShop(String userId, List<Long> shopList);

	/**
	 * 登录用户自修改密码信息
	 *
	 * @param user
	 * @return
	 */
	public int updateUserPasswordById(User user);
}