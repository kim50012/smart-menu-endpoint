package com.basoft.service.config;

import static com.basoft.service.config.DataSourceConfig.BASE_PACKAGES;
import static com.basoft.service.config.DataSourceConfig.BASE_SQL_SESSION_FACTORY;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 产品切换部署数据库步骤如下：
 * 1、public static final String BASE_SQL_SESSION_FACTORY = "primarySqlSessionFactory";
 * 修改该变量的值，可选值有primarySqlSessionFactory（mysql）,secondarySqlSessionFactory(oracle)和sqlserverSqlSessionFactory（ms sqlserver）
 * 2、指定primary DataSourceTransactionManager
 */

/**
 * DataSourceConfig
 *
 * @Description:
 * @Create in 201803
 * @Version: v1.0
 * @Author: yan
 */
@Configuration
@PropertySource("classpath:datasource.properties")
@MapperScan(basePackages = BASE_PACKAGES, sqlSessionFactoryRef = BASE_SQL_SESSION_FACTORY)
public class DataSourceConfig {
    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    // 扫描加载BASE_PACKAGES指定包下面的MAPPER
    public static final String BASE_PACKAGES = "com.basoft.service.dao";

    // 扫描加载到BASE_SQL_SESSION_FACTORY指定的session factory
    public static final String BASE_SQL_SESSION_FACTORY = "primarySqlSessionFactory";
//	public static final String BASE_SQL_SESSION_FACTORY = "secondarySqlSessionFactory";
    //public static final String BASE_SQL_SESSION_FACTORY = "sqlserverSqlSessionFactory";


    private static final String CONFIGURATION_LOCATION = "classpath:sqlmap/mybatis-config.xml";

    private static final String MYSQL_LOCATIONS = "classpath:sqlmap/mysql/**/**/*.xml";
    private static final String ORACLE_LOCATIONS = "classpath:sqlmap/oracle/**/**/*.xml";
    private static final String SQLSERVER_LOCATIONS = "classpath:sqlmap/mssqlserver/**/**/*.xml";

    /**
     * mysql ds
     *
     * @return DataSource
     */
    @Bean(name = "primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>start config and init datasource<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return DataSourceBuilder.create().build();
    }

    /**
     * mysql会话工厂
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        //解决myBatis下不能嵌套jar文件的问题
        //VFS.addImplClass(SpringBootVFS.class);
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.basoft.service.entity");
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(CONFIGURATION_LOCATION));
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MYSQL_LOCATIONS));
        return bean.getObject();
    }

    /**
     * mysql事务
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "primaryTransactionManager")
    @Primary
    public DataSourceTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        DataSourceTransactionManager myDataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        myDataSourceTransactionManager.setGlobalRollbackOnParticipationFailure(true);
        myDataSourceTransactionManager.setNestedTransactionAllowed(true);
        return myDataSourceTransactionManager;
    }

    /**
     * mysql SqlSessionTemplate
     *
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }



    /**
     * Oracle ds
     *
     * @return DataSource
     */
    /**@Bean(name = "secondaryDataSource")
     @ConfigurationProperties(prefix = "spring.datasource.secondary")
     public DataSource secondaryDataSource() {
     return DataSourceBuilder.create().build();
     }*/ //20200427

    /**
     * oracle SqlSessionFactory
     * @param dataSource
     * @return
     * @throws Exception
     */
    /**@Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(dataSource);
    bean.setTypeAliasesPackage("com.basoft.service.entity");
    bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(CONFIGURATION_LOCATION));
    bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(ORACLE_LOCATIONS));
    return bean.getObject();
    }*/ //20200427

    /**
     * oracle DataSourceTransactionManager
     *
     * @param dataSource
     * @return
     */
    /**@Bean(name = "secondaryTransactionManager")
    //@Primary
    public DataSourceTransactionManager secondaryTransactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
    DataSourceTransactionManager myDataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
    myDataSourceTransactionManager.setGlobalRollbackOnParticipationFailure(true);
    myDataSourceTransactionManager.setNestedTransactionAllowed(true);
    return myDataSourceTransactionManager;
    }*///20200427

    /**
     * oracle SqlSessionTemplate
     *
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    /**@Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate secondarySqlSessionTemplate(@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory);
    }*///20200427



    /**
     * sqlServer dataSource
     *
     * @return DataSource
     */
    @Bean(name = "sqlserverDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    public DataSource sqlserverDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * MS SQLServer SqlSessionFactory
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlserverSqlSessionFactory")
    public SqlSessionFactory sqlserverSqlSessionFactory(@Qualifier("sqlserverDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.basoft.service.entity");
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(CONFIGURATION_LOCATION));
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(SQLSERVER_LOCATIONS));
        return bean.getObject();
    }

    /**
     * sqlServer DataSourceTransactionManager
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "sqlserverTransactionManager")
    public DataSourceTransactionManager sqlserverTransactionManager(@Qualifier("sqlserverDataSource") DataSource dataSource) {
        DataSourceTransactionManager myDataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        myDataSourceTransactionManager.setGlobalRollbackOnParticipationFailure(true);
        myDataSourceTransactionManager.setNestedTransactionAllowed(true);
        return myDataSourceTransactionManager;
    }

    /**
     * sqlServer SqlSessionTemplate
     *
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlserverSqlSessionTemplate")
    public SqlSessionTemplate sqlserverSqlSessionTemplate(@Qualifier("sqlserverSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>config and init datasource end<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}