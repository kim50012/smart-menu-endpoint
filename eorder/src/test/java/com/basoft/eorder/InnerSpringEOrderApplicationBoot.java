package com.basoft.eorder;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.basoft.eorder")
@EnableTransactionManagement
@Configuration
class InnerSpringEOrderApplicationBoot extends SpringEOrderApplicationBoot {
    /*@Bean("dataSource")
    @Override
    public DataSource getDataSource(AppConfigure ac) {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setDatabaseName("eorder");
        mds.setPortNumber(3307);
        mds.setUser("bawechat");
        mds.setServerName("192.168.0.241");
        mds.setPassword("1q2w3e4r!");
        mds.setUseSSL(true);
        return mds;
    }*/

    @Override
    protected String getConfigFileName(Environment env) {
        System.out.println("\n\n");
        System.out.println("Get active:" + env.getProperty("spring.profiles.active"));

        if ("test".equalsIgnoreCase(env.getProperty("spring.profiles.active"))) {
            return "classpath:app-config-test.json";
        }

        if ("local".equalsIgnoreCase(env.getProperty("spring.profiles.active"))) {
            return "classpath:app-config-local.json";
        }

        return "classpath:app-config-devp.json";
    }
}