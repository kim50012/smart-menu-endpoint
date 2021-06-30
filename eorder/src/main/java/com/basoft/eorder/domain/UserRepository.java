package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.User;
import com.basoft.eorder.interfaces.command.UpdateUserStatus;

public interface UserRepository {
    /**
     * @param user
     * @return
     * @method insertUser
     */
    int insertUser(User user);

    /**
     * 代理商用户信息插入
     *
     * @param user
     * @return
     */
    public int insertUserForAgent(User user);

    /**
     *
     * @param user
     * @return
     */
    // long insertUserReturnPkValue(User user);

    /**
     * @param user
     * @return
     */
    long insertUserNamedPkValue(User user);

    /**
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 修改代理商的用户信息
     *
     * @param user
     * @return
     */
    int updateUserForAgent(User user);

    /**
     * @param user
     * @return
     */
    int deleteUser(User user);

    User getUser(Long managerId);

    /**
     * 根据账号获取用户
     *
     * @param account
     * @return com.basoft.eorder.domain.model.User
     * @author Dong Xifu
     * @date 2018/12/17 下午3:24
     */
    User getUserByAccount(String account);

    /**
     * 根据ID获取用户
     *
     * @param managerId
     * @return UserSession
     */
    User getUserByManagerId(String managerId);

    void deleteUser(Long id);

    int updateUserStatus(UpdateUserStatus userStatus);

    int delUserByAccount(String account);

    /**
     * 根据账号获取用户
     *
     * @param account
     * @return
     */
    User checkAccountIsExist(String account);

    /**
     * Manager端-门店用户信息
     *
     * @param user
     * @return
     */
    int insertUserInManager(User user);

    /**
     * Manager端-更新用户信息
     *
     * @param user
     * @return
     */
    int updateUserInManager(User user);

    /**
     * 修改Manager密码
     * （1）SA/CA修改密码
     * （2）Manager用户的密码
     *
     *
     * @param account
     * @param password
     * @return
     */
    int updateUser(String account, String password);
}