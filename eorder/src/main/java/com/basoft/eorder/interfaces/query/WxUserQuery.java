package com.basoft.eorder.interfaces.query;

import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:01 2019/5/15
 **/
public interface WxUserQuery {
    /**
     * 根据openid本地数据库获取用户信息
     *
     * @param map
     * @return
     */
    WxUserDTO getWxUser(Map<String, Object> map);

    /**
     * 根据openid从公众平台获取用户信息并插入到本地数据库
     *
     * @param map
     * @return
     */
    WxUserDTO reGetWxUser(Map<String, Object> map);
}
