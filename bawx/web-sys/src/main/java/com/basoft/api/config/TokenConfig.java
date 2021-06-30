package com.basoft.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {
	/*basoft.token.prefix=token-
	basoft.browser.token.header=auth
	basoft.browser.token.headPrefix=
	basoft.token.redis.expiredTime=20*/
	
    @Value("${basoft.token.prefix}")
    public String tokenKeyPrefix;
    
    @Value("${basoft.browser.token.header}")
    public String tokenHeader;
    
    @Value("${basoft.browser.token.headPrefix}")
    public String tokenHeadPrefix;
    
    @Value("${basoft.token.expiredTime}")
    public Integer expiredTime;
}