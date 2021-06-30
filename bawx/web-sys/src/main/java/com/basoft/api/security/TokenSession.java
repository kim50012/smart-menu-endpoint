package com.basoft.api.security;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.basoft.core.util.CacheMap;

/**
 * @author basoft
 */
@Repository
public class TokenSession {
	private Logger logger = LoggerFactory.getLogger(TokenAuthenticationTokenFilter.class);
	
	// private static Map<String,TokenSession> tokenCache = new Hashtable<String,TokenSession>();
	private static Map<Object,Object> tokenCache = CacheMap.getDefault();
	
	public TokenSession(){
		logger.info("start to check authentication cahe......");
		logger.info("cache status is::"+tokenCache.size());
		logger.info("authentication cahe[tokenCache] is OK");
	}
	
	@PostConstruct
	public void initTokenCache() {
		// 用于前期开发接口测试，不用了注释掉
		/*String token ="token-eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjIzNTgxMjM5MzYzMTA4ODY0NiJ9.e5nRsCvV7DbjbRmAPWGGj2AjqVStA4p4i6miRH0zglM";
		TokenSession tokenSession = new TokenSession();
		tokenSession.setUserId("root");
		tokenSession.setUserName("basoft");
		tokenCache.put(token, tokenSession);*/
		logger.info("authentication cahe[tokenCache] initialize OK");
	}
	
	/**
	 * 获取登录缓存池
	 * 
	 * @return
	 */
	public static Map<Object,Object> getTokenCache(){
		return TokenSession.tokenCache;
	}
	
	/**
	 * 根据token从緩存中获取登录信息
	 * 
	 * @param token
	 * @return
	 */
	public static TokenSession getTokenSessionByToken(String token) {
		if(!StringUtils.isEmpty(token)) {
			TokenSession ts = (TokenSession) tokenCache.get(token);
			return ts;
		}
		return null;
	}
	
	/**
	 * 将登录信息和token放入缓存中
	 * 
	 * @param token
	 * @param ts
	 * @return
	 */
	public static void setTokenSessionByToken(String token,TokenSession ts) {
		if(!StringUtils.isEmpty(token) && ts != null) {
			tokenCache.put(token, ts);
		}
	}
	
	/**
	 * 根据token清理登录信息
	 * 
	 * @param token
	 * @return
	 */
	public static void removeTokenSessionByToken(String token) {
		if(!StringUtils.isEmpty(token)) {
			tokenCache.remove(token);
		}
	}
    
    // 用户ID
    private String userId;
    
    private String userName;
    
    private Integer groupId;
    
    /**
     * 账号
     */
    private String account;
    /**
     * 手机号
     */
    private String mobilephone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 注册时间
     */
    private Long regDate;
    
    /**
     * 用户角色
     */
    private String[] roles;
    
    private String userAuth;
    
    // 当前正在操作的shopId
    private String shopId;
    
    private String openId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getRegDate() {
		return regDate;
	}

	public void setRegDate(Long regDate) {
		this.regDate = regDate;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(String userAuth) {
		this.userAuth = userAuth;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public static TokenSession getTokenSession(HttpServletRequest request) {
		TokenSession tokenSession = null;
		if (request.getAttribute("tokenSession") != null) {
			tokenSession = (TokenSession) (request.getAttribute("tokenSession"));
		}
		return tokenSession;
	}

	@Override
	public String toString() {
		return "TokenSession [logger=" + logger + ", userId=" + userId + ", userName=" + userName + ", groupId="
				+ groupId + ", account=" + account + ", mobilephone=" + mobilephone + ", email=" + email + ", regDate="
				+ regDate + ", roles=" + Arrays.toString(roles) + ", userAuth=" + userAuth + ", shopId=" + shopId
				+ ", openId=" + openId + "]";
	}
}