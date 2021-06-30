/*
package com.basoft.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
//@EnableScheduling
@ComponentScan(basePackages = { "com.basoft" })
public class BASoftWechatApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(BASoftWechatApplication.class, args);
	}
	
	*/
/**
	 * 部署到外部容器~
	 * 
	 * @param application
	 * @return
	 *//*

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BASoftWechatApplication.class);
    }
	
	*/
/**
	 * 配置文件上传临时路径和上传文件的大小限制
	 *//*

	 */
/*@Bean
	 MultipartConfigElement multipartConfigElement() {
	    MultipartConfigFactory config = new MultipartConfigFactory();
	    // config.setLocation("D:\\temp");
	    config.setMaxFileSize("100MB");
        config.setMaxRequestSize("200MB");
	    return config.createMultipartConfig();
	}*//*

}*/
