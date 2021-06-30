package com.basoft.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.basoft.service.definition.base.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.basoft.api.config.TokenConfig;

/**
 * TokenAuthenticationTokenFilter
 * 
 * @author basoft
 */
public class TokenAuthenticationTokenFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(TokenAuthenticationTokenFilter.class);

    @Autowired
    private TokenConfig tokenConfig;
    
    @Autowired
    private TokenUserDetailsServiceImpl userDetailsService;

    @Autowired
    private TokenUtil jwtTokenUtil;

    @Autowired
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(tokenConfig.tokenHeader);

        if (authHeader != null && authHeader.startsWith(tokenConfig.tokenHeadPrefix)) {
        	
            final String authToken = authHeader.substring(tokenConfig.tokenHeadPrefix.length());
            logger.info("checking authentication token:" + authToken);
            
            if (authToken != null && !authToken.isEmpty()) {
                String tokenKey = tokenConfig.tokenKeyPrefix + authToken;
                logger.info("tokenKey:" + tokenKey);

                TokenSession tokenSession = (TokenSession) redisService.getTokenSession(tokenKey);
                
                //TokenSession tokenSession = TokenSession.getTokenSessionByToken(tokenKey);
                request.setAttribute("tokenSession", tokenSession);
                request.setAttribute("tokenKey", tokenKey);
                // 更新缓存k-v的时间戳
                if(tokenSession != null) {
                	TokenSession.getTokenCache().put(tokenKey, tokenSession);
                }
                
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUser(tokenSession);
                    if (null != userDetails && jwtTokenUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        logger.info("authenticated user 【" + userDetails.getUsername() + "】, setting security context");
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}