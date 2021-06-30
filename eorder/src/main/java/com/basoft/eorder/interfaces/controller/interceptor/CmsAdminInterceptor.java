package com.basoft.eorder.interfaces.controller.interceptor;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author woonill
 */
@Slf4j
public class CmsAdminInterceptor extends HandlerInterceptorAdapter {
    private RedisUtil redisUtil;

    CmsAdminInterceptor() {
    }

    @Autowired
    public CmsAdminInterceptor(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        String headerToken = "token-" + getHeader(request);
        log.info("Admin auth token:" + headerToken);

        if (StringUtils.isEmpty(headerToken)) {
            log.error("Header token is null");
            throw new BizException(ErrorCode.LOGIN_INVALID);
        }

        final Object bawx_cms_token2 = redisUtil.hget("BAWX_CMS_TOKEN", headerToken);

        if (bawx_cms_token2 == null) {
            log.error("not found value of token:" + headerToken);
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }

        log.info("Redis Value[" + bawx_cms_token2 + "] of token:" + headerToken);

        Map<String, Object> jsonMap = Collections.EMPTY_MAP;
        try {
            jsonMap = new ObjectMapper().readValue((String) bawx_cms_token2, HashMap.class);
        } catch (IOException e) {
            log.error("IOException Info" + e.getMessage());
            log.error("IOException Info" + e.getMessage(), e);
        }
        request.setAttribute("BAWX_CMS_USER", jsonMap);


        return super.preHandle(request, response, handler);
    }

    private String getHeader(HttpServletRequest request) {
        String tokenVal = request.getHeader("auth");
        if (StringUtils.isEmpty(tokenVal)) {
            if (request.getCookies() != null && request.getCookies().length > 0) {
                log.debug("\t\t AuthInterceptor Log: start get tooken from cookie");
                tokenVal = Arrays.asList(request.getCookies())
                        .stream()
                        .filter((cook) -> cook.getName().equalsIgnoreCase("auth"))
                        .map((cook) -> cook.getValue())
                        .findFirst()
                        .orElseGet(() -> null);
            }
        }
        return tokenVal;
    }
}