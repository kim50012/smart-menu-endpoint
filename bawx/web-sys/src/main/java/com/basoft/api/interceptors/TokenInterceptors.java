package com.basoft.api.interceptors;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.basoft.api.config.TokenConfig;
import com.basoft.api.security.TokenSession;

public class TokenInterceptors implements WebRequestInterceptor {
	private TokenConfig tokenConfig;

	public void setTokenConfig(TokenConfig tokenConfig) {
		this.tokenConfig = tokenConfig;
	}

	@Override
	public void preHandle(WebRequest webRequest) throws Exception {
		String token = webRequest.getHeader(tokenConfig.tokenHeader);
		if (null != token && !token.isEmpty()) {
			String redisTokenKey = tokenConfig.tokenKeyPrefix + token;
			TokenSession tokenSession = TokenSession.getTokenSessionByToken(redisTokenKey);
			webRequest.setAttribute("tokenSession", tokenSession, WebRequest.SCOPE_REQUEST);
		}
	}

	@Override
	public void postHandle(WebRequest webRequest, ModelMap modelMap) throws Exception {
	}

	@Override
	public void afterCompletion(WebRequest webRequest, Exception e) throws Exception {
	}
}
