package com.basoft.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * ClassName: MyGsonUtils Gson解析JSON<br/>
 * date: 2017年11月14日 下午6:29:16 <br/>
 * 
 * @author zhiyong.li
 * @version
 */
public class MyGsonUtils {


    private static Gson gson = null;
    static {
        if (gson == null) {
            gson = new Gson();
        }
    }


    private MyGsonUtils() {}


    /**
     * gsonString: 将object对象转成json字符串 <br/>
     * 
     * @author zhiyong.li
     * @param object 对象
     * @return
     */
    public static String gsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }


    /**
     * gsonToBean: 将gsonString转成泛型bean <br/>
     * 
     * @author zhiyong.li
     * @param gsonString json字符串
     * @param cls 对象
     * @return
     */
    public static <T> T gsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }


    /**
     * gsonToList: 把json字符串转成list <br/>
     * 
     * @author zhiyong.li
     * @param gsonString json字符串
     * @param cls 对象
     * @return
     */
    public static <T> List<T> gsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {}.getType());
        }
        return list;
    }


    /**
     * jsonToList: 把json字符串转成list 解决泛型问题 <br/>
     * 
     * @author zhiyong.li
     * @param json
     * @param cls
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }



    /**
     * gsonToListMaps: 把json字符串转成list中有map <br/>
     * 
     * @author zhiyong.li
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {}.getType());
        }
        return list;
    }


    /**
     * sonToMaps: 把json字符串转成map <br/>
     * 
     * @author zhiyong.li
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> sonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {}.getType());
        }
        return map;
    }
}
