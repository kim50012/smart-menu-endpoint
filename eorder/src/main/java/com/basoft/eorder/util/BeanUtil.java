package com.basoft.eorder.util;


import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.apache.commons.beanutils.BeanUtils.populate;


public class BeanUtil {
    private static final Log logger = LogFactory.getLog( BeanUtil.class );

    public static Object apacheMapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        populate(obj, map);
        return obj;
    }

    public static Map<?, ?> apacheObjectToMap(Object obj) {
        return new BeanMap(obj);
    }

    public static <T> T reflectMapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }

            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }

        return (T) obj;
    }

    /**
     * 将map转换为object，包含赋值给父类属性
     *
     * @param map
     * @param beanClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T reflectMapToObjectWithSuperClass(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        List<Field> fieldList = new ArrayList<Field>();
        Class tempClass = obj.getClass();
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            // 得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }

        for (Field field : fieldList) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return (T) obj;
    }

    public static Map<String, Object> reflectObjectToMap(Object obj)  {
        if(obj == null){
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (Exception e) {
                return null;
            }
        }

        return map;
    }

    public static Object propertyMapToObject(Map<String, Object> map, Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (map.containsKey(key)) {
                Object value = map.get(key);
                // 得到property对应的setter方法
                Method setter = property.getWriteMethod();
                setter.invoke(obj, value);
            }
        }

        return obj;
    }

    public static Map<String, Object> propertyObjectToMap(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();

            // 过滤class属性
            if (!key.equals("class")) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);

                map.put(key, value);
            }

        }
        return map;
    }

    public static <T> Map<String, ?> reflectObjectToMap(Object obj, Class<T> clazz) {
        if(obj == null){
            return null;
        }
        try {
            Map<String, Object> map;

            if (clazz.newInstance() instanceof TreeMap) {
                map = new TreeMap<>();
            } else {
                return reflectObjectToMap(obj);
            }

            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                try {
                    map.put(field.getName(), field.get(obj));
                } catch (Exception e) {
                    return null;
                }
            }

            return map;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
