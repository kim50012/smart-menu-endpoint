package com.basoft.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class MyStringUtils {


    /**
     * getBytes:(字符串转换成byte数组). <br/>
     * 
     * @author zhiyong.li
     * @param str
     * @param charSet 编码格式
     * @return
     */
    public static byte[] getBytes(String str, String charSet) {
        try {
            return str.getBytes(charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.getBytes();
    }

    /**
     * encodeBase64:(字符串进行base64密码加密). <br/>
     * 
     * @author zhiyong.li
     * @param str
     * @return
     */
    public static String encodeBase64(String str) {
        return Base64.encodeBase64String(getBytes(str, "UTF-8"));
    }

    /**
     * decodeBase64:(字符串进行base64密码解密). <br/>
     * 
     * @author zhiyong.li
     * @param str
     * @param charset
     * @return
     */
    public static String decodeBase64(String str, String charset) {
        try {
            return new String(Base64.decodeBase64(str), charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * getStringArra:(以flag为标记，把str分割为String数组。 ). <br/>
     * 
     * @author zhiyong.li
     * @param str
     * @param flag
     * @return
     */
    public static String[] getStringArra(String str, String flag) {
        String[] result = null;
        StringTokenizer tem = new StringTokenizer(str, flag);
        int n = tem.countTokens();
        result = new String[n];
        for (int i = 0; tem.hasMoreTokens(); i++)
            result[i] = tem.nextToken();
        return result;
    }

    /**
     * trim:(String数组的值去空格). <br/>
     * 
     * @author zhiyong.li
     * @param paramArray
     * @return
     */
    public static String[] trim(String[] paramArray) {
        if (ArrayUtils.isEmpty(paramArray)) {
            return paramArray;
        }
        String[] resultArray = new String[paramArray.length];
        for (int i = 0; i < paramArray.length; i++) {
            String param = paramArray[i];
            resultArray[i] = StringUtils.trim(param);
        }
        return resultArray;
    }

    /**
     * splitString:(将一个字符串从某位置开始以某字符作为分隔符进行分隔(得到每段作为字符串的字符串数组). <br/>
     * <blockquote>
     * 
     * <pre> 
     *      String list[] = Utilities.splitString("AAAA,BBBB,CCCC,DDDDD",0,',')
     *      list 为  { "AAAA","BBBB","CCCC","DDDD" } 
     * </pre>
     * 
     * </blockquote>
     * 
     * @author zhiyong.li
     * @param str 被分隔的字符串
     * @param istart 开始位置
     * @param delimiter 分隔符
     * @return
     */
    public final static String[] splitString(String str, int istart, char delimiter) {
        if (str == null) {
            return null;
        }
        int sl = str.length();
        int n = 0;
        for (int i = istart; i < sl; i++) {
            if (str.charAt(i) == delimiter) {
                n++;
            }
        }
        String[] sa = new String[n + 1];
        int i = istart, j = 0;
        for (; i < sl;) {
            int iend = str.indexOf(delimiter, i);
            if (iend < 0) {
                break;
            }
            sa[j++] = str.substring(i, iend);
            i = iend + 1;
        }
        sa[j++] = str.substring(i);
        return sa;
    }

    /**
     * splitReplaceParam:(分割字符串({\d})并替换). <br/>
     * 
     * @author zhiyong.li
     * @param value
     * @param obj
     * @return
     */
    public static String splitReplaceParam(String value, Object[] obj) {
        List<String> list = new ArrayList<String>();
        String[] s = value.split("\\{");
        for (int i = 1; i < s.length; i++) {
            if (s[i].indexOf("}") < 0) {
                continue;
            }
            String key = s[i].substring(0, s[i].indexOf("}"));
            if (list.isEmpty()) {
                list.add(key);
            } else if (!list.contains(key)) {
                list.add(key);
            }
        }
        Collections.sort(list);
        Object[] obj1 = list.toArray();
        for (int i = 0; i < obj.length; i++) {
            value = value.replaceAll("\\{" + obj1[i] + "}", String.valueOf(obj[i]));
        }
        return value;
    }


    /**
     * full2HalfChange:(全角符号替换成半角符号 ). <br/>
     * 
     * @author zhiyong.li
     * @param QJstr
     * @return
     */
    public static String full2HalfChange(String QJstr) {
        StringBuffer outStrBuf = new StringBuffer("");
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            Tstr = QJstr.substring(i, i + 1);
            // 全角空格转换成半角空格
            if (Tstr.equals("　")) {
                outStrBuf.append(" ");
                continue;
            }
            try {
                b = Tstr.getBytes("unicode");
                // 得到unicode字节数据
                if (b[2] == -1) {
                    // 表示全角
                    b[3] = (byte) (b[3] + 32);
                    b[2] = 0;
                    outStrBuf.append(new String(b, "unicode"));
                } else {
                    outStrBuf.append(Tstr);
                }
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                return ex.getMessage();
            }
        }
        return outStrBuf.toString();
    }

    /**
     * isNumeric:(判断字符串是否为数字). <br/>
     * 
     * @author zhiyong.li
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * strIsEnglish:(判断字符串是否为英文字母). <br/>
     * 
     * @author zhiyong.li
     * @param str
     * @return
     */
    public static boolean strIsEnglish(String str) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]*");
        return pattern.matcher(str).matches();
    }

    public static boolean contain(String strs, String dest, String sep) {
        if ((StringUtils.isEmpty(strs)) || (StringUtils.isEmpty(dest)) || (StringUtils.isEmpty(sep))) {
            return false;
        }
        if (strs.contains(sep)) {
            for (String s : strs.split(sep)) {
                if (s.equals(dest)) return true;
            }
        } else {
            return strs.equals(dest);
        }
        return false;
    }

    public static boolean starWith(String strs, String dest, String sep) {
        if ((StringUtils.isEmpty(strs)) || (StringUtils.isEmpty(dest)) || (StringUtils.isEmpty(sep))) {
            return false;
        }
        if (strs.contains(sep)) {
            for (String s : strs.split(sep)) {
                if (dest.startsWith(s)) return true;
            }
        } else {
            return dest.startsWith(strs);
        }
        return false;
    }

    public static List<String> split(String str) {
        return split(str, ",");
    }

    @SuppressWarnings("unchecked")
    public static List<String> split(String str, String sep) {
        if ((StringUtils.isEmpty(str)) || (StringUtils.isEmpty(sep))) {
            return new ArrayList<String>();
        }
        Object[] stra = str.split(sep);
        return MyListUtils.toList(stra);
    }

    public static String fromInputStream(InputStream is) {
        try {
            return IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String replaceBetween(String str, String begin, String end, String newStr) {
        if ((newStr == null) || (begin == null) || (end == null)) {
            return str;
        }

        if (begin.equals(end)) {
            return str;
        }

        if ((str.indexOf(begin) == -1) || (str.indexOf(end) == -1)) {
            return str;
        }

        String replacedStr = StringUtils.substringBetween(str, begin, end);

        return StringUtils.replaceOnce(str, replacedStr, newStr);
    }

    public static boolean isNoneEmpty(String[] strs) {
        if ((strs == null) || (strs.length == 0)) {
            return false;
        }
        for (String str : strs) {
            if (StringUtils.isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * getUUID: 生成UUID <br/>
     * 
     * @author zhiyong.li
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * randomChinese: 生成随机个数的中文 <br/>
     * 
     * @author zhiyong.li
     * @param minLen
     * @param maxLen
     * @return
     */
    public static String randomChinese(int minLen, int maxLen) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int max = maxLen > minLen ? random.nextInt(maxLen - minLen) + minLen : minLen - 1;
        for (int i = 0; i <= max; i++) {
            Integer hightPos = Integer.valueOf(176 + Math.abs(random.nextInt(39)));

            Integer lowPos = Integer.valueOf(161 + Math.abs(random.nextInt(93)));
            try {
                sb.append(new String(new byte[] {hightPos.byteValue(), lowPos.byteValue()}, "GBk"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * getStringRandom: 随机生成包含大小写字母+数字的字符串 <br/>
     * 
     * @author zhiyong.li
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        // 参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * getRandNum: 随机的N位数字 <br/>
     * 
     * @author zhiyong.li
     * @return
     */
    public static int getRandNum() {
        int randNum = (int) ((Math.random() * 9 + 1) * 100000);
        return randNum;
    }

    /**
     * randString: 随机生成包含大小写字母+特殊字符+汉字的字符串 <br/>
     * 
     * @author zhiyong.li
     * @param minLen
     * @param maxLen
     * @return
     */
    public static String randString(int minLen, int maxLen) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int max = maxLen > minLen ? random.nextInt(maxLen - minLen) + minLen : minLen - 1;
        for (int i = 0; i <= max; i++) {
            sb.append(random.nextInt() % 2 != 0 ? RandomStringUtils.randomAscii(1) : randomChinese(1, 1));
        }
        return sb.toString();
    }

    public static String remove(String str, List<String> removeStrs) {
        String newStr = str;
        for (String remove : removeStrs) {
            newStr = StringUtils.remove(newStr, remove);
        }
        return newStr;
    }

    public static String remove(String str, String[] removeStrs) {
        return (removeStrs == null) || (removeStrs.length == 0) ? str : remove(str, Arrays.asList(removeStrs));
    }

    public static String toHexStr(String str) {
        return Hex.encodeHexString(getBytes(str, "UTF-8"));
    }

    public static String fromHex(String hexStr) {
        try {
            return new String(Hex.decodeHex(hexStr.toCharArray()), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexStr;
    }

    public static String encrypt(String plainStr) {
        return encrypt(plainStr + RandomStringUtils.randomAlphanumeric(10), "中文@英文la!234&23-!");
    }

    public static String decrypt(String encryptStr) {
        String strWithSalt = decrypt(encryptStr, "中文@英文la!234&23-!");
        return StringUtils.left(strWithSalt, strWithSalt.length() - 10);
    }

    private static String encrypt(String plainStr, String key) {
        String str = "";

        if (key.length() == 0) {
            return plainStr;
        }

        if (!plainStr.equals(null)) {
            int i = 0;
            for (int j = 0; i < plainStr.length(); j++) {
                if (j > key.length() - 1) {
                    j %= key.length();
                }
                int ch = plainStr.codePointAt(i) + key.codePointAt(j);
                if (ch > 65535) {
                    ch %= 65535;
                }
                str = str + (char) ch;

                i++;
            }

        }

        return toHexStr(str);
    }

    private static String decrypt(String encryptStr, String key) {
        String str = "";

        if (key.length() == 0) {
            return encryptStr;
        }

        String clearedStr = fromHex(encryptStr);
        if (!clearedStr.equals(key)) {
            int i = 0;
            for (int j = 0; i < clearedStr.length(); j++) {
                if (j > key.length() - 1) {
                    j %= key.length();
                }
                int ch = clearedStr.codePointAt(i) + 65535 - key.codePointAt(j);
                if (ch > 65535) {
                    ch %= 65535;
                }
                str = str + (char) ch;

                i++;
            }

        }

        return str;
    }


    /**
     * matcher:(正则匹配字符串). <br/>
     * 
     * @author zhiyong.li
     * @param str
     * @return
     */
    public static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }
}
