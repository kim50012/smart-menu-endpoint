package com.basoft.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 全局的公共的属性配置
 * 
 * @author basoft
 */
@Configuration
public class GlobalCommonConfig {
    @Value("${basoft.dbtype}")
    public String databaseType;
}