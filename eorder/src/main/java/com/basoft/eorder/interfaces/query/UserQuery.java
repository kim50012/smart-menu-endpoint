package com.basoft.eorder.interfaces.query;

import java.util.List;
import java.util.Map;

/**
 * 门店用户管理-admin分管用户和manager自管用户
 */
public interface UserQuery {
    public UserDTO getUserById(long id);

    /**
     * admin分管用户-查询用户数量
     *
     * @param param
     * @return
     */
    public int getUserCount(Map<String, Object> param);

    /**
     * admin分管用户-查询用户列表
     *
     * @param param
     * @return
     */
    public List<UserDTO> getUserListByMap(Map<String, Object> param);

    /**
     * manager分管用户-查询用户数量
     *
     * @param param
     * @return
     */
    public int getUserInManagerCount(Map<String, Object> param);

    /**
     * manager分管用户-查询用户列表
     *
     * @param param
     * @return
     */
    public List<UserInManagerDTO> getUserInManagerListByMap(Map<String, Object> param);
}