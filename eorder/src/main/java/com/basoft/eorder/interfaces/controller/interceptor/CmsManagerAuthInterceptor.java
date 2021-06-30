package com.basoft.eorder.interfaces.controller.interceptor;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.LoginService;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.interfaces.controller.ServletUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author woonill
 * <p>
 * 门店店主权限检测
 */
public class CmsManagerAuthInterceptor extends HandlerInterceptorAdapter {
    private LoginService service;
    private ObjectMapper om = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    CmsManagerAuthInterceptor() {
    }

    @Autowired
    public CmsManagerAuthInterceptor(LoginService loginService) {
        this.service = loginService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        if (!"/eorder/manager/api/v1/query".equalsIgnoreCase(request.getRequestURI())) {
            logger.info("RequestURI:" + request.getRequestURI());
        }

        if ("/eorder/manager/api/v1/login".equalsIgnoreCase(request.getRequestURI())) {
            return true;
        }

        if ("/eorder/manager/api/v1/exportExcel".equalsIgnoreCase(request.getRequestURI())) {
            return true;
        }
        if ("/eorder/manager/api/v1/storeSettlesExcel".equalsIgnoreCase(request.getRequestURI())) {
            return true;
        }
        if ("/eorder/manager/api/v1/storeDtlSettlesExcel".equalsIgnoreCase(request.getRequestURI())) {
            return true;
        }

        // admin cms内登录manager cms
        if ("/eorder/manager/api/v1/singleLogin".equalsIgnoreCase(request.getRequestURI())) {
            return true;
        }

        ServletUtils.printHeader(request);

        String tokenVal = request.getHeader("token");
        if (request.getCookies() != null && request.getCookies().length > 0) {

            logger.debug("\t\t AuthInterceptor Log: start get tooken from cookie");
            tokenVal = Arrays.asList(request.getCookies())
                    .stream()
                    .filter((cook) -> cook.getName().equalsIgnoreCase("token"))
                    .map((cook) -> cook.getValue())
                    .findFirst()
                    .orElseGet(() -> null);

        }
        //tokenVal = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImNhbnRpbmcwMDAxIn0.QFGGaJ7SBHJboCSQc3BtyemrPpYlPk5jVyvqgQ7HoUM";
        if (StringUtils.isEmpty(tokenVal)) {
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }

        logger.debug("\t\t AuthInterceptor log Token:" + tokenVal);

        // String cmsToken = request.getHeader(CacheLoginService.REDIS_LOGIN_KEY);
        UserSession user = service.getCurrentUser(tokenVal);

        if (user == null) {
            logger.warn("token error:" + tokenVal);
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }

        request.setAttribute(AppConfigure.BASOFT_USER_SESSION_PROP, user);
        return true;
    }
}
