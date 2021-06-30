package com.basoft.eorder.interfaces.controller.interceptor;

import com.basoft.eorder.application.LoginService;
import com.basoft.eorder.interfaces.controller.Echo;
import com.basoft.eorder.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class AuthInterceptorRegistry extends WebMvcConfigurerAdapter implements InitializingBean {
    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("\n\n\n start addInterceptors");
        // Register guest interceptor with single path pattern
        registry.addInterceptor(new WechatInterceptor(redisUtil)).addPathPatterns(new String[]{"/wechat", "/wechat/**"});
        registry.addInterceptor(new CmsManagerAuthInterceptor(loginService)).addPathPatterns(new String[]{"/manager", "/manager/**"});
        registry.addInterceptor(new CmsAdminInterceptor(redisUtil)).addPathPatterns(new String[]{"/admin", "/admin/**"});
        log.info("End Regist Interceptor");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Return Handler \n\n\n");
        final List<HandlerMethodReturnValueHandler> unmodifiableList = this.handlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> list = new ArrayList<>(unmodifiableList.size());
        for (HandlerMethodReturnValueHandler returnValueHandler : unmodifiableList) {
            if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                list.add(new ResultWarpReturnValueHandler(returnValueHandler));
            } else {
                list.add(returnValueHandler);
            }
        }
        this.handlerAdapter.setReturnValueHandlers(list);
        log.info("End Return handler");
    }

    /*@Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }*/

    static final class ResultWarpReturnValueHandler implements HandlerMethodReturnValueHandler {
        private final HandlerMethodReturnValueHandler delegate;

        public ResultWarpReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean supportsReturnType(MethodParameter returnType) {
            return delegate.supportsReturnType(returnType);
        }

        @Override
        public void handleReturnValue(
                Object returnValue,
                MethodParameter returnType,
                ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest) throws Exception {
            String methodParameterName = returnType.getMethod().getName();

            if ((returnValue instanceof RedirectView) || (methodParameterName.equals("order_pay"))) {
                //过滤像 redirect 的传统 View
                delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            } else {
                Echo<Object> result = new Echo<>(returnValue);
                delegate.handleReturnValue(result, returnType, mavContainer, webRequest);
                // delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            }
        }
    }
}
