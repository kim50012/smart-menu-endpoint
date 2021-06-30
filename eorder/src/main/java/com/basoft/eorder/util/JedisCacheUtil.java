/*
package com.basoft.eorder.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

*/
/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午7:18 2018/12/6
 **//*


@Component
public class JedisCacheUtil {

    @Autowired
    private JedisPool jedisPool;



    */
/**
     * 获取指定key的值,如果key不存在返回null，如果该Key存储的不是字符串，会抛出一个错误
     *
     * @param key
     * @return
     *//*

    public String get(String key) {
        Jedis jedis = getJedis();
        String value = null;
        value = jedis.get(key);
        return value;
    }

    */
/**
     * 设置key的值为value
     *
     * @param key
     * @param value
     * @return
     *//*

    public String set(String key, String value) {
        Jedis jedis = getJedis();
        return jedis.set(key, value);
    }

    */
/**
     * 通过key获取所有的field和value
     *
     * @param key
     * @return
     *//*

    public Map<String, String> hgetall(String key) {
        Jedis jedis = getJedis();
        return jedis.hgetAll(key);
    }


    private Jedis getJedis() {
        return jedisPool.getResource();
    }

}
*/
