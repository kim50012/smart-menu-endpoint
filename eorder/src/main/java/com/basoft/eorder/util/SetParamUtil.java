package com.basoft.eorder.util;

import ch.qos.logback.core.CoreConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:38 下午 2019/11/1
 **/
@Slf4j
public class SetParamUtil {

    /**
     * 设置时间参数 isReduce:1 上个月
     *
     * @Param
     * @return Map<String,Object>
     * @author Dong Xifu
     * @date 2019/11/1 3:35 下午
     */
    public static Map<String,Object> setTimeParam(String startTime, String endTime, Boolean isReduce,Map<String,Object> param) {
        log.info("startTimeLong="+startTime);
        log.info("endTimeLong="+endTime);
        startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN);
        endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DAILY_DATE_PATTERN);

        if (isReduce) {
            param.put("startTime",DateUtil.dateFormat(startTime));
            param.put("endTime",DateUtil.getLastDayOfMonth(DateUtil.dateFormat(endTime)));
            param.put("month",DateUtil.dateFormat(startTime).substring(0,7));
        }else {
            param.put("startTime",startTime);
            param.put("endTime",endTime);
            param.put("month",startTime.substring(0,7));
        }
        return param;
    }

    public static Map<String, Object> setBaseParam(Long storeId,int page,int size,Map<String,Object> param){
       return setBaseParam(storeId,"","","",page,size,param);
    }

    public static Map<String, Object> setBaseParam(Long storeId,String storeName,String storeType
        ,String city,int page,int size,Map<String,Object> param) {
        if (storeId > 0) {
            param.put("storeId", storeId);
        }
        param.put("storeName", storeName);
        param.put("storeNm", storeName);
        param.put("storeType", storeType);
        param.put("city", city);
        param.put("page", page);
        param.put("size", size);
        return  param;
    }
}
