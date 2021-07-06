package com.basoft.eorder.batch.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import lombok.extern.slf4j.Slf4j;

//@Slf4j
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public String port;

    @Value("${spring.redis.instanceId}")
    public String instanceId;

    @Value("${spring.redis.password}")
    public String password;

    @Value("${spring.redis.pool.max-active}")
    public String maxActive;

    @Value("${spring.redis.pool.max-wait}")
    public String maxWait;

    @Value("${spring.redis.pool.max-idle}")
    public String maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    public String minIdle;

    @Value("${spring.redis.timeout}")
    public String timeout;

    @Bean("redissonClient")
    public RedissonClient getRedisson() {
//        log.info("开始配置Redisson客户端......");
//        log.info("Redisson连接信息之host和instanceId：：" + host + "和" + instanceId);
        Config config = new Config();
//        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(instanceId + ":" + password);
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        //添加主从配置
        // config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});
//        log.info("配置Redisson客户端结束......");
        return Redisson.create(config);
    }
}