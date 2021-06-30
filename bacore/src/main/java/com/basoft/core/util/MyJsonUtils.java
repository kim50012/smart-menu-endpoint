package com.basoft.core.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * ClassName: MyJsonUtils JSON处理工具类<br/>
 * date: 2017年11月2日 上午10:24:23 <br/>
 * 
 * @author zhiyong.li
 * @version
 */
public class MyJsonUtils {

    /**
     * transformObjToMap: 把对象转成Map <br/>
     * 
     * @author zhiyong.li
     * @param object
     * @return
     */
    public static Map<String, Object> transformObjToMap(Object object) {
        return transformStrToMap(transformObjToStr(object));
    }


    /**
     * transformObjToStr: 把对象转成json字符串 <br/>
     * 
     * @author zhiyong.li
     * @param obj
     * @return
     */
    public static String transformObjToStr(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * transformStrToMap: 把json字符串转成Map <br/>
     * 
     * @author zhiyong.li
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> transformStrToMap(String json) {
        try {
            return getObjectMapper().readValue(json, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
        // Include.Include.ALWAYS 默认
        // Include.NON_DEFAULT 属性为默认值不序列化
        // Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
        // Include.NON_NULL 属性为NULL 不序列化
        mapper.setSerializationInclusion(Include.NON_NULL);
        // 过滤map中的null值
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        return mapper;
    }
}
