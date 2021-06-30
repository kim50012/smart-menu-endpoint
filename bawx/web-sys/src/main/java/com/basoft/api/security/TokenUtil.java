package com.basoft.api.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenUtil {
	public boolean validateToken(String token, UserDetails userDetails) {
		// TODO 完善验证规则
		return true;
	}
}