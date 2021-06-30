package com.basoft.eorder.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TokenGeneratorUtil {
	private static String SECRET = "secret-basoft-180317";

	public static String createToken(String uId) throws IllegalArgumentException, UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alg", "HS256");
		map.put("typ", "JWT");
		String token = JWT.create().withHeader(map).withClaim("id", uId)
				.sign(Algorithm.HMAC256(TokenGeneratorUtil.SECRET));// 加密
		return token;
	}

	/**
	 * 根据联合登录openId创建token
	 * 
	 * @param openId
	 * @return
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public static String createTokenByOpenId(String openId)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alg", "HS256");
		map.put("typ", "JWT");
		String token = JWT.create().withHeader(map).withClaim("id", openId)
				.sign(Algorithm.HMAC256(TokenGeneratorUtil.SECRET));// 加密
		return token;
	}
}
