package com.basoft.api;

import com.basoft.service.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

@Slf4j
@SpringBootApplication
@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "com.basoft" })
public class BAAwsSoftWechatApplication {
    public static void main(String...args){
        /*System.out.println("\n\n\n starting main");
        final URL resource = BAAwsSoftWechatApplication.class.getResource("/conf/application.properties");
        System.out.println(resource.getPath());*/
        SpringApplication.run(BAAwsSoftWechatApplication.class, args);
    }

    /*
     * 文件上传临时路径
     */
    // @Bean
    /*public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //org.springframework.web.multipart.MultipartException:
        // Could not parse multipart servlet request; nested exception
        // is java.io.IOException: The temporary upload location
        // [C:\Users\Administrator\AppData\Local\Temp\tomcat.2657793286419673969.8003\work\Tomcat\localhost\bawx\app\pttms\tmp] is not valid
        factory.setLocation("/app/pttms/tmp");
        return factory.createMultipartConfig();
    }*/

    /**
     * Spring容器实例化Redis操作类
     *
     * @param environment
     * @return
     */
    @Bean("redisUtils")
    public RedisUtil newRedisUtils(Environment environment){
        final RedisConnectionFactory rcf = newRedisConnectionFactory(environment);
        //JedisConnectionFactory jcf = new JedisConnectionFactory();
        RedisTemplate<String,Object> tt = new RedisTemplate<>();
        tt.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        tt.setConnectionFactory(rcf);
        tt.afterPropertiesSet();

        /*final RedisConnection connection = rcf.getConnection();
        System.out.println("\n\n\n\n start Redis Test");
        System.out.println(connection);*/

        return new RedisUtil(tt);
    }

    public RedisConnectionFactory newRedisConnectionFactory(Environment environment) {
//        log.info("\n\n\n RedisConfig........................");
        String redisHost = environment.getProperty("spring.redis.host");
        String instanceId = environment.getProperty("spring.redis.instanceId");
        String password = environment.getProperty("spring.redis.password");
        int maxActive = environment.getProperty("spring.redis.pool.max-active", Integer.class);
        int maxIdle = environment.getProperty("spring.redis.pool.max-idle",Integer.class);
        int minIdle = environment.getProperty("spring.redis.pool.min-idle",Integer.class);
        int timeOut = environment.getProperty("spring.redis.timeout",Integer.class);
        long maxWait = environment.getProperty("spring.redis.pool.max-wait",Long.class);
        int port = environment.getProperty("spring.redis.port",Integer.class);

//        log.info("RedisHost:"+redisHost);
//        log.info("instanceId:"+instanceId);
//        log.info("password:"+password);
//        log.info("maxActive:"+maxActive);
//        log.info("maxIdle:"+maxIdle);
//        log.info("MinIdle:"+minIdle);
//        log.info("TimeOut:"+timeOut);
//        log.info("MaxWait:"+maxWait);
//        log.info("port:"+port);

        /*  텐센트
        JedisShardInfo jsi = new JedisShardInfo(redisHost);
//        jsi.setPassword(instanceId+":"+password);
        jsi.setPassword(password);
        jsi.setConnectionTimeout(timeOut);

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWait);

        JedisConnectionFactory jcf = new JedisConnectionFactory(jsi);
        jcf.setPoolConfig(config);
        jcf.setPort(port);
        */
        

        JedisShardInfo jsi = new JedisShardInfo(redisHost, port);
        JedisConnectionFactory jcf = new JedisConnectionFactory(jsi);
        
        System.out.println("--------------------------------");
        System.out.println("--------------------------------");
        System.out.println("AWS REDIS OK");
        System.out.println("--------------------------------");
        System.out.println("--------------------------------");


        return jcf;
    }


    /*protected Resource[] initResources(Environment env){*/

        /*System.out.println(env.getActiveProfiles());
        final SimpleCommandLinePropertySource commandLineArgs = env.getProperty("commandLineArgs", SimpleCommandLinePropertySource.class);
        System.out.println("\n\n\n Print CommandLine Args:"+commandLineArgs);*/

        /*String stage = env.getProperty("spring.profile.active");
        if("prod".equalsIgnoreCase(stage)){
             Resource[] res =  new Resource[2];
             res[0]=new ClassPathResource("/conf/application.properties");
             res[1] = new ClassPathResource("/conf/application-dev.properties");
             return res;
        }
        else if("test".equalsIgnoreCase("stage")){
            Resource[] res =  new Resource[2];
            res[0]=new ClassPathResource("/conf/application.properties");
            res[1] = new ClassPathResource("/conf/application-test.properties");
            return res;
        }
        else if("dev".equalsIgnoreCase("stage")){
            Resource[] res =  new Resource[2];
            res[0]=new ClassPathResource("/conf/application.properties");
            res[1] = new ClassPathResource("/conf/application-dev.properties");
            return res;
        }
        throw new IllegalArgumentException("Please input stage parameter");
    }


    @Bean
    public PropertySourcesPlaceholderConfigurer placeholderConfigurer(Environment env) {
        System.out.println("\n\n\n Start print ENV");
        System.out.println(env.toString());

        PropertySourcesPlaceholderConfigurer pspc =  new PropertySourcesPlaceholderConfigurer();
*//*        pspc.setLocations(
                new ClassPathResource("/conf/application.properties"),
                new ClassPathResource("/conf/application-dev.properties")
        );*//*
        pspc.setLocations(initResources(env));
        return pspc;
    }*/

    /*@Bean("redisConnectionFactory")
    public RedisConnectionFactory newFactory(){
        System.out.println("\n\n\n start jedisConnectioNFactory");
        JedisShardInfo jsi = new JedisShardInfo("192.168.0.81");
        JedisConnectionFactory jcf = new JedisConnectionFactory(jsi);
        return jcf;
    }*/
}
