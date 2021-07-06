package com.basoft.service.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
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
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
//        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(instanceId + ":" + password);
        //添加主从配置
        // config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});
//        log.info("配置Redisson客户端结束......");
        return Redisson.create(config);
    }

    /*@Bean
    Redisson redissonSentinel() {
        Config config = new Config();
        config.useClusterServers()
                // 集群状态扫描间隔时间，单位是毫秒
                .setScanInterval(2000)
                //可以用"rediss://"来启用SSL连接
                .addNodeAddress("redis://10.82.0.100:7000")
                .addNodeAddress("redis://10.82.0.101:7001")
                .addNodeAddress("redis://10.82.0.102:7002")
                .addNodeAddress("redis://10.82.0.103:7003")
                .addNodeAddress("redis://10.82.0.104:7004")
                .addNodeAddress("redis://10.82.0.105:7005");
        return (Redisson) Redisson.create(config);
    }*/
}