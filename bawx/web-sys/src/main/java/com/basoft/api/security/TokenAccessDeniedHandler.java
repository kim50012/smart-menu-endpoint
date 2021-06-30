package com.basoft.api.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.basoft.core.web.vo.Echo;

/**
 * 拒绝访问处理
 * 
 * @author basoft
 */
public class TokenAccessDeniedHandler implements AccessDeniedHandler {
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		// response.setHeader("Access-Control-Max-Age", "3600"); //设置过期时间
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		Echo<?> result = new Echo<>();
		result.setMessage("没有权限访问！");
		result.setStatus(-1);
		response.getWriter().println(result.toString());
	}
}
