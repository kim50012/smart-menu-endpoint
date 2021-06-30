package com.basoft.eorder.interfaces.controller.interceptor;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.WxSession;
import com.basoft.eorder.interfaces.controller.ServletUtils;
import com.basoft.eorder.util.CookieUtil;
import com.basoft.eorder.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author woonill
 * <p>
 * Wechat客户端权限拦截器
 */
public class WechatInterceptor extends HandlerInterceptorAdapter {
    private RedisUtil redisUtil;
    private static final Logger logger = LoggerFactory.getLogger(WechatInterceptor.class);

    WechatInterceptor() {
    }

    public WechatInterceptor(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        logger.debug("<<<<<<<<<<<< WechatInterceptor >>>>>>>>>> url=[" + requestUri + "];");

        // if (matched(request.getRequestURI())) {
        if (matched(requestUri)) {
            logger.info("Start matched handler URI::[" + requestUri + "]");
            ServletUtils.printHeader(request);

            String token = request.getHeader(AppConfigure.BASOFT_H5_SESSION);
            if (StringUtils.isBlank(token)) {
                token = CookieUtil.getCookieValue(request, AppConfigure.BASOFT_H5_SESSION);
                if (StringUtils.isBlank(token)) {
                    logger.error("{{{{{{{{{ token is null !!!! }}}}}}}}}:" + token);
                    throw new BizException(ErrorCode.SYS_TIMEOUT);
                }
            }

            WxSession ws = (WxSession) redisUtil.hget(AppConfigure.BASOFT_H5_SESSION, token);
            if (ws == null) {
                logger.error("WxSession null token:" + token);
                throw new BizException(ErrorCode.SYS_TIMEOUT);
            }

            request.setAttribute(AppConfigure.BASOFT_WX_SESSION_PROP, ws);
            return true;
        }

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {
        if (ex != null) {
            /*long expireTimeMillis = System.currentTimeMillis();
            CookieUtil.addCookie(response, AppConfigure.BASOFT_H5_SESSION, token, AppConfigure.TIME_ONE_HOUR);
            CookieUtil.addCookie(response, AppConfigure.BASOFT_H5_SESSION, String.valueOf(expireTimeMillis), AppConfigure.TIME_ONE_HOUR);*/
        }
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 需要传token的URL
     */
    private static final List<String> excludeURLS =
            Arrays.asList(
                    "/eorder/wechat/api/v1/main",
                    "/eorder/wechat/api/v1/main*",
                    "/eorder/wechat/api/v1/query*",
                    "/eorder/wechat/api/v1/query/*",
                    "/eorder/wechat/api/v1/command/*",
                    "/eorder/wechat/api/v1/myorder"

                    // 酒店类查询-20191219
                    ,
                    "/eorder/wechat/api/v2/h/*",
                    "/eorder/wechat/api/v2/r/*",
                    // 零售类-20200416
                    "/eorder/wechat/api/v2/retail/*"
            );

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    public static boolean matched(String uri) {
        return excludeURLS.stream().filter((s) -> pathMatcher.match(s, uri)).count() > 0;
    }
}