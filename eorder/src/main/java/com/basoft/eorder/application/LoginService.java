package com.basoft.eorder.application;

/**
 * LoginService
 *
 * @author Dong Xifu
 * @date 2018/12/17 下午3:25
 */
public interface LoginService {
    /**
     * 登录，查缓存
     *
     * @param account
     * @return UserSession
     */
    UserSession login(String account);

    /**
     * 从缓存中获取user
     *
     * @param cmsToken
     * @return UserSession
     */
    UserSession getCurrentUser(String cmsToken);

    /**
     * Admin CMS中单点登录Manager CMS时对Admin Access Token的合法性验证
     *
     * @param adminAccessToken
     * @return
     */
    boolean checkAdminAccessToken(String adminAccessToken);

    /**
     * Admin CMS中单点登录Manager CMS时对storeId和managerId匹配性进行检查
     *
     * @param storeId
     * @param managerId
     * @return
     */
    boolean checkStoreManager(String storeId, String managerId);

    /**
     * Admin CMS中单点登录Manager CMS
     *
     * @param managerId
     * @return UserSession
     */
    UserSession loginManagerCMSByManagerId(String managerId);
}