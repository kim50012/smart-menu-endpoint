package com.basoft.service.impl.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basoft.service.dao.user.UserMapper;
import com.basoft.service.definition.user.UserService;
import com.basoft.service.entity.user.User;
import com.basoft.service.entity.user.UserDTO;
import com.basoft.service.entity.user.UserListDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author basoft
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据查询条件查询用户列表
     *
     * @param userListDTO
     * @return
     */
    @Override
    public PageInfo<User> getUsers(UserListDTO userListDTO) {
        if (userListDTO == null) {
            return null;
        }
        PageHelper.startPage(userListDTO.getPageNum(), userListDTO.getPageSize());
        List<User> list = userMapper.getUsers(userListDTO);
        return new PageInfo<>(list);
    }

    /**
     * 根据查询条件查询用户列表(含用户负责的公众号ID列表)
     *
     * @param userListDTO
     * @return
     */
    @Override
    public PageInfo<User> getUsersWithShops(UserListDTO userListDTO) {
        if (userListDTO == null) {
            return null;
        }
        PageHelper.startPage(userListDTO.getPageNum(), userListDTO.getPageSize());
        List<User> list = userMapper.getUsersWithShops(userListDTO);
        return new PageInfo<>(list);
    }

    /**
     * 根据账号查询用户信息(该用户状态必须可用)
     *
     * @param account
     * @return
     */
    @Override
    public User getUserByAccount(String account) {
        return userMapper.getUserByAccount(account);
    }

    /**
     * 根据用户userId查询用户信息
     *
     * @param userId
     * @return
     */
    public User getUserByUserId(String userId) {
        return userMapper.getUserByUserId(userId);
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    public int addUser(User user) {
        return userMapper.insert(user);
    }

    /**
     * 根据userId修改用户信息
     *
     * @param user
     * @return
     */
    public int updateUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据userId删除用户信息
     *
     * @param userId
     * @return
     */
    @Transactional
    public int deleteUserById(String userId) {
        // 删除用户的微信公众号的关联关系
        userMapper.deleteUserShop(userId);
        // 删除用户
        return userMapper.deleteByPrimaryKey(userId);
    }

    /**
     * 根据userId修改用户权限
     *
     * @param userDTO
     * @return
     */
    public int authUser(UserDTO userDTO) {
        return userMapper.authUser(userDTO);
    }

    /**
     * 根据userId清除所负责公众号
     *
     * @param userId
     * @return
     */
    public int deleteUserShop(String userId) {
        return userMapper.deleteUserShop(userId);
    }

    /**
     * 根据userId和公众号列表新增所负责公众号
     *
     * @param userId
     * @param shopList
     * @return
     */
    public int insertUserShop(String userId, List<Long> shopList) {
        Map<String, Object> us = new HashMap<String, Object>();
        us.put("userId", userId);
        us.put("shopList", shopList);
        return userMapper.insertUserShop(us);
    }

    /**
     * 登录用户自修改密码信息
     *
     * @param user
     * @return
     */
    public int updateUserPasswordById(User user) {
        return userMapper.updateUserPasswordById(user);
    }
}