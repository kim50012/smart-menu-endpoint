package com.basoft.api.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.basoft.core.web.vo.Echo;

/**
 * This is invoked when user tries to access a secured REST resource without
 * supplying any credentials
 * 
 * @author basoft
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		// This is invoked when user tries to access a secured REST resource without supplying any credentials
		// We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
		// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		// response.setHeader("Access-Control-Max-Age", "3600"); //设置过期时间
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		Echo<?> result = new Echo<>();
		result.setMessage("没有登录或者登录已过期，无权访问！");
		result.setStatus(-2);
		response.getWriter().println(result.toString());
	}
}