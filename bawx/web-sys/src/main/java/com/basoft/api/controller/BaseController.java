package com.basoft.api.controller;

import com.basoft.api.config.GlobalCommonConfig;
import com.basoft.api.config.WebsysConfigurer;
import com.basoft.api.security.TokenSession;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.service.definition.base.RedisService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    protected HttpServletRequest request;
    
    @Autowired
    protected HttpServletResponse response;
    
    @Autowired
    private WebsysConfigurer websysConfigurer;
    
    @Autowired
    private GlobalCommonConfig globalCommonConfig;
    @Autowired
	private RedisService redisService;

    //缓存map
    //public static Map<String, Object> session=new HashMap<>();

    /**
     * 获取前端的语言环境。中划线转为下划线，小写字母全部变为大写。
     * 
     * @return String 类似于ZH_CN
     */
    protected String getLanguage() {
        String lang = request.getHeader(websysConfigurer.languageHeader);
        if (StringUtils.isBlank(lang)) {
        	return "ZH_CN";
        } else {
            return lang.toUpperCase().replace("-", "_");
        }
    }
    
	/**
	 * 获取缓存的登录等数据信息
	 */
	protected TokenSession getTokenSession() {
		logger.info("get token session");
		return TokenSession.getTokenSession(request);
	}
	
	/**
	 * 获取缓存的登录等数据信息
	 */
	protected String getToken() {
		logger.info("get the token of this request");
		return (String) request.getAttribute("tokenKey");
	}
    
	/**
	 * 获取缓存的登录等数据信息
	 */
	protected Long getShopId() {
		TokenSession tokenSession = (TokenSession) redisService.getTokenSession(getToken());
		if(tokenSession==null)
			throw new BizException(ErrorCode.SYS_TIMEOUT);
		String shopId = tokenSession.getShopId();
		//String shopId = TokenSession.getTokenSession(request).getShopId();
		// 如果获取不到shopId，抛出系统登陆过期
		if (StringUtils.isEmpty(shopId)) {
			throw new BizException(ErrorCode.SYS_TIMEOUT);
		}
		return Long.parseLong(shopId);
	}

	/**
	 * @author Dong Xifu
	 * @Date 2018/4/26 下午3:08
	 * @describe 获取登录人Id
	 * @param
	 * @return
	 **/
	protected String getUserId() {
		String userId = TokenSession.getTokenSession(request).getUserId();
		if (StringUtils.isEmpty(userId)) {
			// return "root";
			throw new BizException(ErrorCode.SYS_TIMEOUT);
		}
		return userId;
	}
	
	/**
	 * 获取全局的配置属性
	 * @param configKey
	 * @return
	 */
	protected String getGlobalCommonConfig(String configKey) {
		String configValue = "";
		// 数据库类型
		if("dbType".equals(configKey)) {
			return globalCommonConfig.databaseType;
		}
		return configValue;
	}
}
