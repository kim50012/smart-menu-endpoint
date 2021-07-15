package com.basoft.file;

import com.basoft.file.application.AppConfigure;
import com.basoft.file.application.DefaultFileService;
import com.basoft.file.application.FileService;
import com.basoft.file.application.FileTransfer;
import com.basoft.file.application.impl.AwsS3FileSerivce;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@SpringBootApplication(scanBasePackages="com.basoft.file")
@EnableTransactionManagement
@Configuration
public class SpringApplicationBoot {
    private AtomicBoolean started = new AtomicBoolean(false);

    static final int DEFUALT_CPU_RATE = 2;

    public static void main(String...args){
        //通过假如此部分来可以接受到 GraphQuery 的参数 {Store(id:1232123){id,name,created}} 格式
//        log.info("=================================================== Start on ===========================================================");
//        log.info(Paths.get("./").toAbsolutePath().toString());
//        log.info("RootPath:"+Paths.get("./").toAbsolutePath());
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
        new SpringApplicationBoot().run(args);
    }

    protected final ConfigurableApplicationContext run(String...args){
        if(!started.compareAndSet(false,true)){
            throw new IllegalStateException("already running");
        }
        // SpringApplication ap = new SpringApplication(this.getClass());
        // ap.run(args);
        ConfigurableApplicationContext cac =  SpringApplication.run(this.getClass(), args);
        return cac;
    }

    /*@Bean(name="threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(AppConfigure appConfigure){
        int processorSize = Runtime.getRuntime().availableProcessors();

        Map<String,Object> threadPool = (Map<String, Object>) appConfigure.getObject("threadPool").get();

        System.out.println("\n\n\n ");
        System.out.println(threadPool.toString());

        Integer multipleOfCore = (Integer) threadPool.get("multipleOfCore") ;
        Integer keepAliveSeconds = (Integer) threadPool.get("keepAliveSeconds");
        Integer maxPoolSize = (Integer)threadPool.get("maxPoolOfCore");
        Integer queueCapacity = (Integer) threadPool.get("queueCapacity");

        String cpuCoreStr = appConfigure.get("cpu-core");
        if(!StringUtils.isEmpty(cpuCoreStr)){
            processorSize = Integer.parseInt(cpuCoreStr);
        }

        if(multipleOfCore == null){
            multipleOfCore = DEFUALT_CPU_RATE;
        }

        processorSize = processorSize*multipleOfCore;
        maxPoolSize = maxPoolSize*processorSize;

        System.out.println("\n Thread Option " +
                " \t core:"+processorSize+"\n"+
                " \t maxProcess:"+maxPoolSize+"\n"+
                " \t keepAliveSeconds:"+keepAliveSeconds+"\n"+
                " \t PoolQueue:"+queueCapacity+"\n");

        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setKeepAliveSeconds(keepAliveSeconds);
        pool.setCorePoolSize(processorSize);//核心线程池数
        pool.setMaxPoolSize(maxPoolSize); // 最大线程
        pool.setQueueCapacity(queueCapacity);//队列容量
        pool.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()); //队列满，线程被拒绝执行策略
        return pool;
    }*/

    /*static String getEnvVal(Environment env,String name){
        String val = env.getProperty(name);
        if(org.apache.commons.lang3.StringUtils.isEmpty(val)){
            val = System.getProperty(name);
        }
        return val;
    }*/



    /********************************个性化配置项加载AppConfigure-Start**************************************/
    @Bean
    public AppConfigure getAppConfigure(Environment env) {
//        log.info("Run Environment Detail Info>>>" +  env.toString());
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(AppConfigure.TOKEN_KEY_PREFIX_PROP, "token-");
        configMap.put(AppConfigure.BASOFT_BROWSER_TOKEN_HEADER_PROP, "auth");
        configMap.put(AppConfigure.BASOFT_TOKEN_EXPIRED_TIME_PROP, "20");
        configMap.put(AppConfigure.BASOFT_CLIENT_LANGUAGE_PROP, "Accept-Language");
        configMap.put(AppConfigure.BASOFT_REGION_ID_PROP, new Long(10));
        configMap.put(AppConfigure.BASOFT_WORKER_ID_PROP, new Long(5));
        // configMap.put(AppConfigure.BASOFT_BROWSER_TOKEN_HEADER,"auth");

        Map<String, Object> appConfigMap = this.getConfigMap(env);
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

    protected Map<String,Object> getConfigMap(Environment configure){
        String fname = getConfigFileName(configure);
        try {
            ClassPathResource cpr = new ClassPathResource(fname);
            File file = cpr.getFile();
//            File file = ResourceUtils.getFile(fname);
            if(!file.exists()){
                throw new RuntimeException("not found config file on: classpath:templates");
            }
            ObjectMapper json = new ObjectMapper();
            return json.readValue(file,HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("not found config file :"+fname);
        }
    }

    /**
     * 从运行参数中获取配置文件名称。如果不存在则取默认路径conf/app-config-prod.json
     * <p>
     * docker run --confing=conf/app-config-prod.json
     *
     * @param env
     * @return
     */
    protected String getConfigFileName(Environment env) {
        String configFilePath = env.getProperty("config");
        if (!org.apache.commons.lang3.StringUtils.isEmpty(configFilePath)) {
//            log.info("<><><><><><><><><><><>配置文件存在，其名称为：<><><><><><><><><><><>" + configFilePath);
            return configFilePath;
        }
//        log.info("<><><><><><><><><><><>配置文件不存在，使用默认路径conf/app-config-prod.json<><><><><><><><><><><>");
        return "conf/app-config-prod.json";
    }
    /********************************个性化配置项加载AppConfigure-End**************************************/



    /********************************构建数据源和事务管理器-Start**************************************/
    /**
     * 构建数据源
     *
     * @param appConfigure
     * @return
     */
    @Bean("dataSource")
    public DataSource getDataSource(AppConfigure appConfigure) {
        Map<String,Object> mysqlConfigMap = appConfigure.getObject("mysqlDataSource-config").map((o) -> (Map<String,Object>)o ).orElseGet(()-> null);
        String mysqlUrl = (String) mysqlConfigMap.get("url");
        String msyqlUser = (String)mysqlConfigMap.get("user");
        String pass = (String)mysqlConfigMap.get("password");

        MysqlDataSource mds = new MysqlDataSource();
        mds.setUrl(mysqlUrl);
        mds.setUser(msyqlUser);
        mds.setPassword(pass);
        mds.setUseSSL(true);
        System.out.println("End set Datasource");
        return mds;
    }

    /**
     * 构建事务管理器
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
    /********************************构建数据源和事务管理器-End**************************************/

    /********************************构建AWS File Service-Start**************************************/
    // AWS File Service Config
    private static final String FILE_TRANSER_FACTORY_PROP = "fileTransferFactory";

    // AWS File Service Config
    private static final String DEFAULT_FILE_TRANSER_FACTORY_NAME = "";

    /**
     * AWS File Service
     *
     * @param ds
     * @param appConfigure
     * @return
     */
    @SuppressWarnings({ "unchecked", "static-access" })
	@Bean("FileService") //20190724停止使用AWS云存储
    public FileService newFileService(DataSource ds, AppConfigure appConfigure){
        final Map<String,Object> factoryConfig = (Map<String, Object>) appConfigure.getObject(FILE_TRANSER_FACTORY_PROP).orElseThrow(() -> new IllegalStateException("error"));
        String clazz = (String) factoryConfig.get("clazz");
        try {
//            final Class<?> aClass = ClassUtils.forName(clazz, Thread.currentThread().getContextClassLoader());
//            final FileTransfer.FileTransferFactory instance = (FileTransfer.FileTransferFactory) aClass.newInstance();
//            final FileTransfer transfer = instance.of((Map<String, Object>) factoryConfig.get("props"));
//            return new DefaultFileService(ds,transfer);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException(e);
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException(e);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException(e);
//        }
        
            final Map<String,Object> aws_s3_config = (Map<String, Object>) appConfigure.getObject(AwsS3FileSerivce.CONFIG_PROP).get();
            AwsS3FileSerivce fileTransfer = (AwsS3FileSerivce)AwsS3FileSerivce.of(aws_s3_config);

            final FileTransfer transfer = fileTransfer.of((Map<String, Object>) factoryConfig.get("props"));
            return new DefaultFileService(ds,transfer);
        } catch (Error e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
    /********************************构建AWS File Service-End**************************************/

    @Configuration
    public static  class InnerSpringBootServletInitializer extends SpringBootServletInitializer {
        /**
         * 部署到外部容器~
         *
         * @param application
         * @return-
         */
        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(InnerSpringBootServletInitializer.class);
        }

        /**
         * 配置文件上传临时路径和上传文件的大小限制
         */
        @Bean
        MultipartConfigElement multipartConfigElement() {
            MultipartConfigFactory config = new MultipartConfigFactory();
            // config.setLocation("D:\\temp");
            config.setMaxFileSize("100MB");
            config.setMaxRequestSize("200MB");
            return config.createMultipartConfig();
        }

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