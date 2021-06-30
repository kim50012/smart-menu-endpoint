package com.basoft.api.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


/**
 * TokenUserFactory
 * 
 * @author basoft
 */
public class TokenUserFactory {
	private TokenUserFactory() {
	}

	/**
	 * create TokenUser with userId and userName
	 * 
	 * @param userId
	 * @param userName
	 * @return
	 */
	public static TokenUser create(String userId, String userName) {
		return new TokenUser(userId.toString(), userName, null, null, mapToGrantedAuthorities(new ArrayList<>()), null);
	}

	public static TokenUser createByOpenId(String openId, String userName) {
		return new TokenUser(openId, userName, null, null, mapToGrantedAuthorities(new ArrayList<>()), null);
	}

	/**
	 * create TokenUser with userId and userName,and roles
	 * 
	 * @param userId
	 * @param userName
	 * @param roles
	 * @return
	 */
	public static TokenUser create(String userId, String userName, List<String> roles) {
		if (null == roles) {
			roles = new ArrayList<>();
		}
		return new TokenUser(userId.toString(), userName, null, null, mapToGrantedAuthorities(roles), null);
	}


    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
