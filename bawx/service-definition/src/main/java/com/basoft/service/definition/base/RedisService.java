package com.basoft.service.definition.base;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午12:08 2019/6/24
 **/
public interface RedisService {

    public Object getTokenSession(String key);

    public Boolean setTokenSession(String key, Object value);

}
