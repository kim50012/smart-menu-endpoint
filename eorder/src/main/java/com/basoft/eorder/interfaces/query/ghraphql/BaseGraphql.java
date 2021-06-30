package com.basoft.eorder.interfaces.query.ghraphql;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.WxSession;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 10:08 上午 2019/10/3
 **/
public class BaseGraphql {
    private static Logger logger = LoggerFactory.getLogger(DefaultGraphqlQueryInit.class);


    /**
     * UserSession中获取当前登录门店信息
     *
     * @param env
     * @param storeIdProp
     * @return
     */
    public static Long getStoreId(DataFetchingEnvironment env, String storeIdProp) {
        //如果 Context非空时， 优先获取context里头的值
        Map<String, Object> contextMap = env.getContext();
        if (contextMap != null) {
            UserSession us = (UserSession) contextMap.get(AppConfigure.BASOFT_USER_SESSION_PROP);
            if (us != null) {
                logger.debug("Get StoreID from userSeession :" + us.getStoreId());
                return us.getStoreId();
            }

            final WxSession o = (WxSession) contextMap.get(AppConfigure.BASOFT_WX_SESSION_PROP);
            if (o != null) {
                logger.debug("Get StoreId from WxSession :" + o.getStoreId());
                return o.getStoreId();
            }
        }
        return env.getArgument(storeIdProp);
    }

    public static String getStoreIdStr(DataFetchingEnvironment env) {
        //如果 Context非空时， 优先获取context里头的值
        Map<String, Object> contextMap = env.getContext();
        if (contextMap != null) {
            UserSession us = (UserSession) contextMap.get(AppConfigure.BASOFT_USER_SESSION_PROP);
            if (us != null) {
                logger.debug("Get StoreID from userSeession :" + us.getStoreId());
                return String.valueOf(us.getStoreId());
            }

            final WxSession o = (WxSession) contextMap.get(AppConfigure.BASOFT_WX_SESSION_PROP);
            if (o != null) {
                logger.debug("Get StoreId from WxSession :" + o.getStoreId());
                return String.valueOf(o.getStoreId());
            }
        }
        return env.getArgument("storeId");
    }

    /**
     * 查询基础参数
     *
     * @param env
     * @return
     */
    public static Map<String, Object> getBaseQueryParam(DataFetchingEnvironment env) {
        Map<String, Object> param = new HashMap<>();
        param.put("storeId", getStoreIdStr(env));
        Integer page = env.getArgument("page");
        Integer size = env.getArgument("size");

        if (page != null && size != null && page >= 0 && size > 0) {
            param.put("page", page);
            param.put("size", size);
        }

        return param;
    }



}
