package com.basoft.core.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

public class MyMapUtils {

    /**
     * 获取Map的第一个值
     * 
     * @param map
     * @return
     */
    public static Object getFirstValue(Map<String, Object> map) {
        Iterator<String> localIterator = map.keySet().iterator();
        if (localIterator.hasNext()) {
            Object k = localIterator.next();
            return map.get(k);
        }
        return StringUtils.EMPTY;
    }

    public static Map<String, Object> fill(List<?> ks, FillFunction fillFunction) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (Object k : ks) {
            map.put((String) k, fillFunction.fill(k));
        }
        return map;
    }

    /**
     * calAll:(统计集合中满足条件的数量). <br/>
     * 
     * @author zhiyong.li
     * @param collection
     * @param p
     * @return
     */
    public static int calAll(Collection<?> collection, Predicate<Object> p) {
        int total = 0;
        for (Object obj : collection) {
            // 使用Predicate的test()方法判断该对象是否满足Predicate指定的条件
            if (p.test(obj)) {
                total++;
            }
        }
        return total;
    }


    /**
     * getCount:(统计集合中包含str字符串的数量). <br/>
     * 
     * @author zhiyong.li
     * @param collection 集合
     * @param str
     * @return
     */
    public static int getCount(Collection<?> collection, String str) {
        return calAll(collection, ele -> ((String) ele).contains(str));
    }

    /**
     * getCount:(统计集合中字符串长度大于num的数量). <br/>
     * 
     * @author zhiyong.li
     * @param collection 集合
     * @param num
     * @return
     */
    public static int getCount(Collection<?> collection, int num) {
        return calAll(collection, ele -> ((String) ele).length() > num);
    }



    /**
     * transformListToMap:(把list转换成Map). <br/>
     * 
     * @author zhiyong.li
     * @param list 存储V的List对象
     * @param fieldName4Key V的属性名
     * @param c 对象
     * @return
     */
    public static <K, V> Map<K, V> transformListToMap(List<V> list, String keyMethodName, Class<V> c) {
        Map<K, V> map = new HashMap<K, V>();
        if (list != null) {
            try {
                Method methodGetKey = c.getMethod(keyMethodName);
                for (int i = 0; i < list.size(); i++) {
                    V value = list.get(i);
                    @SuppressWarnings("unchecked")
                    K key = (K) methodGetKey.invoke(list.get(i));
                    map.put(key, value);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("field can't match the key!");
            }
        }

        return map;
    }

    /**
     * sortMapByValue:(使用 Map按value进行排序). <br/>
     * 
     * @author zhiyong.li
     * @param oriMap
     * @return
     */
    public static Map<String, Object> sortMapByValue(Map<String, Object> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Object> sortedMap = new LinkedHashMap<String, Object>();
        List<Map.Entry<String, Object>> entryList = new ArrayList<Map.Entry<String, Object>>(oriMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Entry<String, Object> me1, Entry<String, Object> me2) {
                return ((String) me1.getValue()).compareTo((String) me2.getValue());
            }
        });
        Iterator<Map.Entry<String, Object>> iter = entryList.iterator();
        Entry<String, Object> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    /**
     * sortMapByKey:(使用 Map按key进行排序). <br/>
     * 
     * @author zhiyong.li
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new Comparator<String>() {
            public int compare(String str1, String str2) {
                return str1.compareTo(str2);
            }
        });

        sortMap.putAll(map);

        return sortMap;
    }

    public static class MyMap {
        public static <K, V> TMap<K, V> set(K k, V v) {
            return new TMap<K, V>().set(k, v);
        }

        public static <K> String joinEntry(Map<K, String> map, String sep) {
            return MyListUtils.join(map.values(), sep);
        }

        public static class TMap<K, V> extends HashMap<K, V> {
            private static final long serialVersionUID = 1L;

            public TMap<K, V> set(K k, V v) {
                super.put(k, v);
                return this;
            }
        }
    }

    public static abstract interface FillFunction {
        public abstract Object fill(Object paramK);
    }

    /**
     * convertBeanToMap:(将一个 JavaBean 对象转化为一个 Map). <br/>
     * 
     * @author zhiyong.li
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> convertBeanToMap(Object bean) {
        Class type = bean.getClass();
        Map<String, String> returnMap = Maps.newHashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    try {
                        Object object = readMethod.invoke(bean, new Object[0]);
                        if (object != null) {
                            returnMap.put(propertyName, (String) object);
                        } else {
                            returnMap.put(propertyName, "");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return returnMap;
    }

}
