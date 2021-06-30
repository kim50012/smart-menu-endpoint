package com.basoft.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.basoft.api.config.TokenConfig;

@Service
public class TokenUserDetailsServiceImpl implements UserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(TokenUserDetailsServiceImpl.class);

	@Autowired
	private TokenConfig tokenConfig;

	@Override
	public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
		String redisTokenKey = tokenConfig.tokenKeyPrefix + token;
		TokenSession tokenSession = TokenSession.getTokenSessionByToken(redisTokenKey);
		if (tokenSession == null) {
			return null;
		} else {
			// TODO：创建TokenUser,添加roles
			return TokenUserFactory.create(tokenSession.getUserId(), tokenSession.getUserName());
		}
	}

	public UserDetails loadUser(TokenSession tokenSession) {
		if (null == tokenSession) {
			logger.info("tokenSession is null");
			return null;
		}
		if (null != tokenSession.getUserId()) {
			logger.info("create userDetail by user id");
			return TokenUserFactory.create(tokenSession.getUserId(), tokenSession.getUserName());
		} else if (null != tokenSession.getOpenId()) {
			logger.info("create userDetail by openId");
			return TokenUserFactory.createByOpenId(tokenSession.getOpenId(), tokenSession.getOpenId());
		}
		logger.info("tokenSession has no userId|openId,create userDetail failed");
		return null;
	}
}