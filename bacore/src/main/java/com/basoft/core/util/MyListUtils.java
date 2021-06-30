package com.basoft.core.util;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * ClassName: MyListUtils List集合处理工具类<br/>
 * date: 2017年7月31日 下午3:54:22 <br/>
 * 
 * @author zhiyong.li
 * @version
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MyListUtils {

    /**
     * uniqueList:( List集合去重). <br/>
     * 
     * @author zhiyong.li
     * @param list
     * @return
     */
    public static List unique(List list) {
        Set set = new HashSet(list);
        return new ArrayList(set);
    }

    public static List randomSubList(List list, int num) {
        // 使用默认随机源对列表进行置换，所有置换发生的可能性都是大致相等的
        Collections.shuffle(list);
        return num >= list.size() ? list : list.subList(0, num);
    }

    public static List<File> subBySuffix(List<File> files, String[] suffixes) {
        List list = new ArrayList();
        for (File file : files) {
            String extName = FilenameUtils.getExtension(file.getPath());
            for (String suffix : suffixes) {
                if (suffix.equalsIgnoreCase(extName)) {
                    list.add(file);
                }
            }
        }

        return list;
    }

    public static List toList(Collection cs) {
        List list = new ArrayList(cs.size());
        for (Iterator localIterator = cs.iterator(); localIterator.hasNext();) {
            Object t = localIterator.next();
            list.add(t);
        }
        return list;
    }

    public static List toList(Object ts[]) {
        List list = new ArrayList();
        for (Object t : ts) {
            list.add(t);
        }
        return list;
    }

    public static Object randomOne(List list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static List removeNull(List list) {
        List ts = new ArrayList();
        for (Iterator localIterator = list.iterator(); localIterator.hasNext();) {
            Object t = localIterator.next();
            if (t != null) {
                ts.add(t);
            }
        }
        return ts;
    }

    public static String join(Collection<String> strs) {
        return join(strs, "\r\n");
    }

    public static String join(Collection<String> strs, String sep) {
        if ((strs == null) || (sep == null) || (strs.size() == 0)) {
            return null;
        }
        return StringUtils.join((String[]) strs.toArray(new String[strs.size()]), sep);
    }

    public static Object find(List list, TrueExpression trueExpression) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            Object t = iterator.next();
            if (trueExpression.target(t)) return t;
        }

        return null;
    }

    public static List findAll(List list, TrueExpression trueExpression) {
        List ts = new ArrayList();
        Iterator iterator = list.iterator();
        do {
            if (!iterator.hasNext()) break;
            Object t = iterator.next();
            if (trueExpression.target(t)) ts.add(t);
        } while (true);
        return ts;
    }

    public static abstract interface TrueExpression<T> {
        public abstract boolean target(T paramT);
    }


    /**
     * sortByKeyToList:(List根据属性值进行降序和升序排序). <br/>
     * 
     * @author zhiyong.li
     * @param list
     * @param fieldName 属性名
     * @param isDesc 是否降序
     */
    public static void sortByKeyToList(List<Object> list, String fieldName, boolean isDesc) {
        Collections.sort(list, new Comparator<Object>() {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);

            @Override
            public int compare(Object o1, Object o2) {
                try {
                    Method method1 = o1.getClass().getMethod(getter, new Class[] {});
                    Object value1 = method1.invoke(o1, new Object[] {});
                    Method method2 = o2.getClass().getMethod(getter, new Class[] {});
                    Object value2 = method2.invoke(o2, new Object[] {});
                    // 倒序
                    if (isDesc) {
                        if (value2 instanceof String) {
                            return ((String) value2).compareTo((String) value1);
                        } else if (value2 instanceof Integer) {
                            return ((Integer) value2).compareTo((Integer) value1);
                        } else if (value2 instanceof Double) {
                            return ((Double) value2).compareTo((Double) value1);
                        } else if (value2 instanceof Float) {
                            return ((Float) value2).compareTo((Float) value1);
                        } else if (value2 instanceof BigDecimal) {
                            return ((BigDecimal) value2).compareTo((BigDecimal) value1);
                        } else {
                            return ((String) value2).compareTo((String) value1);
                        }
                    }
                    // 升序
                    if (value1 instanceof String) {
                        return ((String) value1).compareTo((String) value2);
                    } else if (value1 instanceof Integer) {
                        return ((Integer) value1).compareTo((Integer) value2);
                    } else if (value1 instanceof Double) {
                        return ((Double) value1).compareTo((Double) value2);
                    } else if (value1 instanceof Float) {
                        return ((Float) value1).compareTo((Float) value2);
                    } else if (value1 instanceof BigDecimal) {
                        return ((BigDecimal) value1).compareTo((BigDecimal) value2);
                    } else {
                        return ((String) value1).compareTo((String) value2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    /**
     * transformArrayToList:(把数组转成List). <br/>
     * 
     * @author zhiyong.li
     * @param arr 数组
     * @return
     */
    public List<?> transformArrayToList(Object[] arr) {
        return Arrays.asList(arr);
    }

    /**
     * transformMapToListByKey:(把map key转换成List). <br/>
     * 
     * @author zhiyong.li
     * @param map
     * @return
     */
    public List<?> transformMapToListByKey(Map<String, Object> map) {
        return Lists.newArrayList(map.keySet());
    }

    /**
     * transformMapToListByValue:(把map key转换成List). <br/>
     * 
     * @author zhiyong.li
     * @param map
     * @return
     */
    public List<?> transformMapToListByValue(Map<String, Object> map) {
        return Lists.newArrayList(map.values());
    }
}
