package com.basoft.eorder;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.QRCodeGenerateService;
import com.basoft.eorder.application.file.FileService;
import com.basoft.eorder.application.file.HttpFileSerivce;
import com.basoft.eorder.application.framework.ComponentProvider;
import com.basoft.eorder.application.framework.EventProducer;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.foundation.jdbc.eventhandler.EventHandler;
import com.basoft.eorder.foundation.qrcode.ZXingQRCodeGenerateService;
import com.basoft.eorder.interfaces.query.ghraphql.DefaultGraphqlQueryInit;
import com.basoft.eorder.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootApplication(scanBasePackages="com.basoft.eorder")
//@ComponentScan(basePackages = "com.basoft.eorder")
@EnableTransactionManagement
@Configuration

@SuppressWarnings("all")
@Slf4j
@EnableScheduling
public class SpringEOrderApplicationBoot {
    private AtomicBoolean started = new AtomicBoolean(false);
    public static void main(String...args){
        //通过假如此部分来可以接受到 GraphQuery 的参数 {Store(id:1232123){id,name,created}} 格式
        /*System.out.println("=================================================== Start on ===========================================================");
        System.out.println(Paths.get("./").toAbsolutePath());
        System.out.println("RootPath:"+Paths.get("./").toAbsolutePath());

        System.out.println("\n\n");
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");*/

//        log.info("=================================================== Start on ===========================================================");
//        log.info(Paths.get("./").toAbsolutePath().toString());
//        log.info("RootPath:"+Paths.get("./").toAbsolutePath());

//        log.info("\n\n");
//        log.info("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
        //SpringApplicationBuilder builder  = new SpringApplicationBuilder().
        new SpringEOrderApplicationBoot().run(args);
    }

    protected final ConfigurableApplicationContext run(String...args){
        if(!started.compareAndSet(false,true)){
            throw new IllegalStateException("already running");
        }
        ConfigurableApplicationContext cac =  SpringApplication.run(this.getClass(), args);
        return cac;
    }

    /*static String getEnvVal(Environment env,String name){
        String val = env.getProperty(name);
        if(StringUtils.isEmpty(val)){
            val = System.getProperty(name);
        }
        return val;
    }*/

    /**************************AppConfigure Bean Definition and Instantiation**************************/
    /**
     * AppConfigure
     *
     * @param env
     * @return
     */
    @Bean
    public AppConfigure getAppConfigure(Environment env){
        Map<String,Object> configMap = new HashMap<>();
        // 定制属性
        configMap.put(AppConfigure.TOKEN_KEY_PREFIX_PROP,"token-");
        configMap.put(AppConfigure.BASOFT_BROWSER_TOKEN_HEADER_PROP,"auth");
        configMap.put(AppConfigure.BASOFT_TOKEN_EXPIRED_TIME_PROP,"20");
        configMap.put(AppConfigure.BASOFT_CLIENT_LANGUAGE_PROP,"Accept-Language");
        configMap.put(AppConfigure.BASOFT_REGION_ID_PROP,new Long(10));
        configMap.put(AppConfigure.BASOFT_WORKER_ID_PROP,new Long(5));
        // configMap.put(AppConfigure.BASOFT_BROWSER_TOKEN_HEADER,"auth");

        // 配置文件属性
        Map<String,Object> appConfigMap = this.getConfigMap(env);

        // 合并定制属性和配置文件属性
        configMap.putAll(appConfigMap);

        return new AppConfigure() {
            @Override
            public Optional<Object> getObject(String key) {
                return Optional.of(configMap.get(key));
            }

            @Override
            public String get(String key) {
                return (String) configMap.get(key);
            }
        };
    }

    /**
     * 读取配置文件属性
     *
     * @param env
     * @return
     */
    protected Map<String,Object> getConfigMap(Environment env){
        String configPath = getConfigFileName(env);
        // System.out.println("\n\n\n Load config file from :"+configPath);
        try {
            // String appConfigPath = getConfigFileName(configure);
            File file = ResourceUtils.getFile(configPath);
            if(!file.exists()){
                throw new RuntimeException("not found config file on: classpath:templates");
            }
            ObjectMapper json = new ObjectMapper();
            return json.readValue(file,HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("not found config file",e);
        }
    }

    /**
     * 配置文件名称设定
     *
     * @param env
     * @return
     */
    protected String getConfigFileName(Environment env){
        String configFilePath = env.getProperty("config");
        // System.out.println("config="+configFilePath);
//        log.info("config="+configFilePath);
        if(!org.apache.commons.lang3.StringUtils.isEmpty(configFilePath)){
            return configFilePath;
        }

        if("test".equalsIgnoreCase(env.getProperty("spring.profiles.active"))){
            return "conf/app-config-test.json";
        }

        return "conf/app-config-prod.json";
    }



    /**************************Bean Definition and Instantiation*******DOWNWARD->*******************/
    /**
     * 数据源定义和初始化
     *
     * @param ac
     * @return
     */
    @Bean("dataSource")
    public DataSource getDataSource(AppConfigure ac) {
        Map<String,Object> mysqlConfigMap = ac.getObject("mysqlDataSource-config").map((o) -> (Map<String,Object>)o ).orElseGet(()-> null);

        String mysqlUrl = (String) mysqlConfigMap.get("url");
        String msyqlUser = (String)mysqlConfigMap.get("user");
        String pass = (String)mysqlConfigMap.get("password");
        // System.out.println("mysqlUrl="+mysqlUrl);
//        log.info("mysqlUrl="+mysqlUrl);

        MysqlDataSource mds = new MysqlDataSource();
        mds.setUrl(mysqlUrl);
        mds.setUser(msyqlUser);
        mds.setPassword(pass);
        mds.setUseSSL(true);

        return mds;
    }

    /**
     *DataSourceTransactionManager/PlatformTransactionManager bean definition and instantiation
     * 事务定义和初始化
     *
     * @param dataSource
     * @return
     */
    @Bean
    public PlatformTransactionManager getTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager= new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    /**
     * QueryHandler bean definition and instantiation
     *
     * @param appContext
     * @return
     */
    @Bean("queryHandler")
    public QueryHandler newQueryHandler(ApplicationContext appContext){
        return new DefaultGraphqlQueryInit().build(wrapComponentFunc(appContext));
    }

    /**
     * 对象Provider
     *
     * @author woonill
     * @param context
     * @return T 所需要的对象实例
     */
    private ComponentProvider wrapComponentFunc(ApplicationContext context){
        return new ComponentProvider() {
            @Override
            public <T> T getComponent(Class<T> type) {
                return context.getBean(type);
            }
        };
    }

    /**
     * RedisConnectionFactory bean definition and instantiation
     *
     * @param appConfigure
     * @return
     */
    public static final String REDIS_CONFIG_PROP = "redis-config";
    @Bean("jediConnectionFactory")
    public RedisConnectionFactory newRedisConnectionFactory(AppConfigure appConfigure) {
        Map<String,Object> redisConfig = appConfigure.getObject(REDIS_CONFIG_PROP).map((o) -> (Map<String,Object>)o ).orElseGet(()-> null);
        String redisServer = (String)redisConfig.get("server");
        Integer port = (Integer)redisConfig.get("port");
        String password = (String)redisConfig.get("password");
        String instanceId = (String)redisConfig.get("instanceId");
        Integer maxIdle = (Integer)redisConfig.get("pool.max-idle");
        Integer minIdle = (Integer)redisConfig.get("pool.min-idle");
        Integer timeOut = (Integer)redisConfig.get("timeout");
        Integer maxWait = (Integer) redisConfig.get("pool.max-wait");

        /*System.out.println("\n\n");
        System.out.println("--------------------------------------- RedisConfig ---------------------------------------------");
        System.out.println("Redis Server ->"+redisServer);
        System.out.println("\n\n\n RedisConfig");
        System.out.println("RedisHost:"+redisServer);
        System.out.println("maxIdle:"+maxIdle);
        System.out.println("MinIdle:"+minIdle);
        System.out.println("TimeOut:"+timeOut);
        System.out.println("MaxWait:"+maxWait);
        System.out.println("port:"+port);*/
//        log.info("\n\n");
//        log.info("--------------------------------------- RedisConfig ---------------------------------------------");
//        log.info("Redis Server ->"+redisServer);
//        log.info("\n\n\n RedisConfig");
//        log.info("maxIdle:"+maxIdle);
//        log.info("MinIdle:"+minIdle);
//        log.info("TimeOut:"+timeOut);
//        log.info("MaxWait:"+maxWait);
//        log.info("port:"+port);

        //String redisServer = "192.168.0.81";
        //String redisServer = "bawechat-redis-srv.dxve9i.0001.apn2.cache.amazonaws.com";
        JedisShardInfo jsi = new JedisShardInfo(redisServer, port);
//        jsi.setPassword(instanceId+":"+password);
//        jsi.setPassword(password);
//        jsi.setConnectionTimeout(timeOut);

        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxIdle(maxIdle);
//        config.setMinIdle(minIdle);
        //config.setMaxTotal();
//        config.setMaxWaitMillis(maxWait);

        JedisConnectionFactory jcf = new JedisConnectionFactory(jsi);
        jcf.setPoolConfig(config);
        jcf.setPort(port);
        
        return jcf;
    }

    /**
     * RedisUtil bean definition and instantiation
     * Redis操作工具类
     *
     * @param appConfigure
     * @return
     */
    @Bean("redisUtils")
    public RedisUtil newRedisUtils(AppConfigure appConfigure){
        final RedisConnectionFactory rcf = newRedisConnectionFactory(appConfigure);
        // JedisConnectionFactory jcf = new JedisConnectionFactory();
        RedisTemplate<String,Object> tt = new RedisTemplate<>();
        tt.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        tt.setConnectionFactory(rcf);
        tt.afterPropertiesSet();

        /*final RedisConnection connection = rcf.getConnection();
        System.out.println("\n\n\n\n start Redis Test");
        System.out.println(connection);*/

        return new RedisUtil(tt);
    }

    public static final int DEFUALT_CPU_RATE = 2;

    /**
     * Spring线程池ThreadPoolTaskExecutor的配置定义初始化
     * ThreadPoolTaskExecutor bean configuration,definition and instantiation
     * 用在spring内部异步使用，现在只有EventHandler里使用了此部分
     *
     * @param appConfigure
     * @return
     */
    @Bean(name="threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(AppConfigure appConfigure){
        int processorSize = DEFUALT_CPU_RATE;

        Map<String,Object> threadPool = (Map<String, Object>) appConfigure.getObject("externThreadPool").get();

        /*System.out.println("\n\n\n ");
        System.out.println(threadPool.toString());*/
//        log.info("\n\n\n ");
//        log.info(threadPool.toString());

        Integer multipleOfCore = (Integer) threadPool.get("multipleOfCore") ;
        Integer keepAliveSeconds = (Integer) threadPool.get("keepAliveSeconds");
        Integer maxPoolSize = (Integer)threadPool.get("maxPoolOfCore");
        Integer queueCapacity = (Integer) threadPool.get("queueCapacity");

        if(multipleOfCore != null){
            processorSize = multipleOfCore;
        }

        if(maxPoolSize == null){
            maxPoolSize = processorSize * 4;
        }

        /*System.out.println("\n Thread Option " +
                " \t core:"+processorSize+"\n"+
                " \t maxProcess:"+maxPoolSize+"\n"+
                " \t keepAliveSeconds:"+keepAliveSeconds+"\n"+
                " \t PoolQueue:"+queueCapacity+"\n");*/
//        log.info("\n Thread Option " +
//                " \t core:"+processorSize+"\n"+
//                " \t maxProcess:"+maxPoolSize+"\n"+
//                " \t keepAliveSeconds:"+keepAliveSeconds+"\n"+
//                " \t PoolQueue:"+queueCapacity+"\n");

        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setKeepAliveSeconds(keepAliveSeconds);
        pool.setCorePoolSize(processorSize);//核心线程池数
        pool.setMaxPoolSize(maxPoolSize); // 最大线程
        pool.setQueueCapacity(queueCapacity);//队列容量
        pool.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()); //队列满，线程被拒绝执行策略
        return pool;
    }

    /**
     * EventProducer bean definition and instantiation
     *
     * @param appConfigure
     * @param taskExecutor
     * @return
     */
    @Bean("EventProducer")
    public EventProducer newEventProducer(AppConfigure appConfigure, ThreadPoolTaskExecutor taskExecutor){
        Map<String,Object> pushConfig = (Map<String, Object>) appConfigure.getObject("jpushConfig").get();
        String appKey = (String)pushConfig.get("appKey");
        String secretKey = (String)pushConfig.get("secretKey");

        //JPushClient jpushClient = new JPushClient("38b8a757964781ca42c62473", "96a4e078fc06a27c63cfcd7d", null, ClientConfig.getInstance());
        JPushClient jpushClient = new JPushClient(secretKey, appKey, null, ClientConfig.getInstance());
        EventHandler eventHandler = new EventHandler(jpushClient,taskExecutor.getThreadPoolExecutor());
        return eventHandler;
    }

    /**
     * QRCodeGenerateService/ZXingQRCodeGenerateService bean definition and instantiation
     *
     * @param acc
     * @return
     */
    @Bean("QRCodeGenerateService")
    public QRCodeGenerateService newQRCodeGenerateService(FileService acc){
        return new ZXingQRCodeGenerateService(acc);
    }

    /**
     * FileService/HttpFileSerivce bean definition and instantiation
     *
     * @param ds
     * @param appConfigure
     * @return
     */
    @Bean("FileService")
    public FileService newFileService(DataSource ds, AppConfigure appConfigure){
        String uploadAppRemoteServer = appConfigure.get("uploadFileRemoteServer");
        if(StringUtils.isEmpty(uploadAppRemoteServer)){
            throw new NullPointerException("uploadFileRemoteServer is null");
        }

        return new HttpFileSerivce(uploadAppRemoteServer);
    }

    /**
     * MultipartConfigElement and ObjectMapper bean definition and instantiation
     * 1.MultipartConfigElement-配置文件上传临时路径和上传文件的大小限制
     * 2.ObjectMapper-json转换规则设置
     */
    @Configuration
    public static class InnerSpringBootServletInitializer {
        /**
         * 配置文件上传临时路径和上传文件的大小限制
         */
        @Bean
        MultipartConfigElement multipartConfigElement() {
            // System.out.println("\n\n\n start MultipartConfigElement");
//            log.info("\n\n\n start MultipartConfigElement");

            MultipartConfigFactory config = new MultipartConfigFactory();
            // config.setLocation("D:\\temp");
            config.setMaxFileSize("100MB");
            config.setMaxRequestSize("200MB");
            return config.createMultipartConfig();
        }

        /*@Bean
        public EmbeddedServletContainerCustomizer threadPoolConfigure(AppConfigure appConfigure) {
            String runThread  = appConfigure.get("runThread");
            String maxConnection  = appConfigure.get("maxConnection");

            int inRunThread = StringUtils.isBlank(runThread) ? Runtime.getRuntime().availableProcessors()*4 : Integer.parseInt(runThread);
            int sp = inRunThread > 100 ? 1 : 3;
            int inMaxConnection = StringUtils.isBlank(maxConnection) ? inRunThread << sp : Integer.parseInt(maxConnection);

            System.out.println("\n\n ===========================================   Tomcat Configure =========================================================");
            System.out.println("Server Processor :"+Runtime.getRuntime().availableProcessors());
            System.out.println("Configure tomcat ThreadPool");
            System.out.println("Tomcat maxThreadPool:"+inRunThread);
            System.out.println("Tomcat maxConnectionSize:"+inMaxConnection);

            return new EmbeddedServletContainerCustomizer() {
                @Override
                public void customize(ConfigurableEmbeddedServletContainer container) {
                    if (container instanceof TomcatEmbeddedServletContainerFactory) {
                        TomcatEmbeddedServletContainerFactory tomcat = ((TomcatEmbeddedServletContainerFactory) container);
                        tomcat.addConnectorCustomizers(connector -> {
                            connector.getService();
                            ProtocolHandler handler = connector.getProtocolHandler();
                            if (handler instanceof Http11NioProtocol) {
                                Http11NioProtocol http = (Http11NioProtocol) handler;
                                http.setMaxConnections(inMaxConnection);
                                http.setMaxThreads(inRunThread);*/

                                /*http.setSelectorTimeout(3000);
                                http.setSessionTimeout(3000);
                                http.setConnectionTimeout(3000);*/
                            /*}
                        });
                    }
                }

            };
        }*/

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            mapper.registerModule(simpleModule);
            return mapper;
        }
    }
}