package com.basoft.service.impl.base;

import com.basoft.core.util.CacheMap;
import com.basoft.service.definition.base.RedisService;
import com.basoft.service.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.stereotype.Service;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午12:08 2019/6/24
 **/
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Object getTokenSession(String key) {
        Object res = redisUtil.hget(RedisUtil.TOKEN_SESSION, key);
        System.out.println(res);
        return res;
    }

    @Override
    public Boolean setTokenSession(String key, Object value) {
      return redisUtil.hset(RedisUtil.TOKEN_SESSION,key,value,RedisUtil.TOKEN_EXPIRE_TIME);
    }
}
