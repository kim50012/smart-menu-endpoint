package com.basoft.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.zip.CRC32;

/**
 * Created by presleyli on 2017/11/2.
 */
public class StringUtil extends StringUtils {
    public static String shortenLong(String prefix, Long id) {
        CRC32 crc = new CRC32();
        String time = Long.toString(id);
        for (int c : time.toCharArray()) {
            crc.update(c);
        }
        StringBuilder str = new StringBuilder(prefix);
        long result = crc.getValue();
        while (result != 0) {
            str.append(StringUtil.convert62((int) (result % 62)));
            result /= 62;
        }
        return str.toString();
    }

    private static char convert62(int num) {
        return (char) (num > 35 ? (97 + num - 36) : (num > 9 ? (65 + num - 10) : (48 + num)));
    }

    public static boolean isValid(String str) {
        return str != null && str.trim().length() != 0;
    }

    public static String getFileUrl(String filePath, String fileName) {
        return StringUtil.trimToEmpty(filePath) + StringUtil.trimToEmpty(fileName);
    }

    /**
     * 截取字符串str中指定字符strStart、strEnd之间的字符串
     *
     * @param str
     * @param strStart
     * @param strEnd
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return null;
        }
        if (strEndIndex < 0) {
            return null;
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        System.out.println(StringUtil.subString("1层20号桌","层","桌").length()==2);
        System.out.println(StringUtil.subString("0层0号桌","层","桌").length()==2);
    }
}
