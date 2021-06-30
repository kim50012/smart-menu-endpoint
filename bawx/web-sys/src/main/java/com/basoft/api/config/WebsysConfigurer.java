package com.basoft.api.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * ClassName: FrontAppConfigurer
 * 解决跨域
 *
 * @version 1.0
 */
@Slf4j
@Configuration
public class WebsysConfigurer extends WebMvcConfigurerAdapter {
    Logger logger = LoggerFactory.getLogger(WebsysConfigurer.class);

    @Value("${basoft.client.language.header}")
    public String languageHeader;

    @Value("${basoft.web.file-path}")
    private String mImagesPath;

    @Value("${static.resources.union.path}")
    private String staticUnionPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
        log.info("WebsysConfigurer::addResourceHandlers::mImagesPath::::" + mImagesPath);
        if (mImagesPath.equals("") || mImagesPath.equals("${basoft.web.file-path}")) {
            String imagesPath = WebsysConfigurer.class.getClassLoader().getResource("").getPath();
            if (imagesPath.indexOf(".jar") > 0) {
                imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
            } else if (imagesPath.indexOf("classes") > 0) {
                imagesPath = "file:" + imagesPath.substring(0, imagesPath.indexOf("classes"));
            }
            imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/")) + "/images/";
            mImagesPath = imagesPath;
        }
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Static resources mapping path::" + mImagesPath + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // registry.addResourceHandler("/images/**").addResourceLocations(mImagesPath);//因为images和上传路径设置的ROOT冲突，所以改名为res
        // registry.addResourceHandler("/res/**").addResourceLocations(mImagesPath);
        registry.addResourceHandler(staticUnionPath).addResourceLocations(mImagesPath);

        super.addResourceHandlers(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // addAllowedOrigin是追加访问源地址,allowedMethods设置访问源请求方法,allowedHeaders设置访问源请求头
        registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(false).maxAge(3600);
    }
}
