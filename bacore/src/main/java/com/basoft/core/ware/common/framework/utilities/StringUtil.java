package com.basoft.core.ware.common.framework.utilities;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.basoft.core.ware.wechat.domain.material.MediaUrlReturn;
import com.basoft.core.ware.wechat.util.SQLDateTimeUtils;
import com.basoft.core.ware.wechat.util.WeixinMediaUtils;

public class StringUtil {
    public static String nvl(String source) {
        return nvl(source, "");
    }

    public static String nvl(Object obj) {
        return nvl(obj, "");
    }

    public static String emptyToNull(String source) {
        return (nvl(source).equals("")) ? null : source;
    }

    public static String nvl(Object source, String target) {
        return nvl(String.valueOf(source), target);
    }

    public static String nvl(String source, String target) {
        return (source == null || "".equals(source.trim())) ? target : source.trim();
    }

    public static String trim(String str) {
        return trim(str, "");
    }

    public static String trim(String str, String defValue) {
        if (str == null || "".equals(str)) {
            return defValue;
        } else if (str.trim().length() == 0) {
            return defValue;
        } else {
            return str.trim();
        }
    }

    public static String trim(Object src, String defValue) {
        if (src != null) {
            return src.toString().trim();
        } else {
            return defValue;
        }
    }

    public static Boolean isEmpty(String source) {
        return (source == null || "".equals(source.trim())) ? true : false;
    }

    public static Boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        return isEmpty(obj.toString());
    }

    public static Boolean isNotEmpty(String source) {
        return (source != null && !"".equals(source.trim())) ? true : false;
    }

    public static Boolean isNotEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        return isNotEmpty(obj.toString());
    }

    public static String leftPadding(String src, int length) {
        return leftPadding(src, length, "0");
    }

    public static String leftPadding(String src, int length, String ch) {
        int strLen = src.length();
        if (strLen < length) {
            while (strLen < length) {
                StringBuffer sb = new StringBuffer();
                sb.append(ch).append(src);// 左补0
                src = sb.toString();
                strLen = src.length();
            }
        }
        return src;
    }

    public static String rightPadding(String src, int length) {
        return rightPadding(src, length, "0");
    }

    public static String rightPadding(String src, int length, String ch) {
        int strLen = src.length();
        if (strLen < length) {
            while (strLen < length) {
                StringBuffer sb = new StringBuffer();
                // sb.append(ch).append(src);// 左补0
                sb.append(src).append(ch);// 右补0
                src = sb.toString();
                strLen = src.length();
            }
        }
        return src;
    }

    public static String toUpperCase(String str) {
        return (str == null) ? "" : str.toUpperCase();
    }

    public static String toLowerCase(String str) {
        return (str == null) ? "" : str.toLowerCase();
    }

    public static String replace(String original, String find, String replace) {
        if (original == null || find == null || replace == null || original.length() < 1 || find.length() < 1) {
            return original;
        }
        int index = -1, fromIndex = 0, tempIndex;
        StringBuffer sb = new StringBuffer();
        while ((tempIndex = original.indexOf(find, fromIndex)) >= 0) {
            index = tempIndex;
            sb.append(original.substring(fromIndex, index)).append(replace);
            fromIndex = index + find.length();
        }
        if (sb.length() < 1) {
            return original;
        }
        sb.append(original.substring(index + find.length()));
        return sb.toString();
    }

    public static String getNumber(String string) {
        if (string == null) {
            return "";
        }
        char[] source = string.toCharArray();
        char[] result = new char[source.length];
        int j = 0;
        for (int i = 0, y = result.length; i < y; i++) {
            if (Character.isDigit(source[i])) {
                result[j++] = source[i];
            }
        }
        return new String(result, 0, j);
    }

    public static int getStringLength(String str) {
        char ch[] = str.toCharArray();
        int max = ch.length;
        int count = 0;
        for (int i = 0; i < max; i++) {
            if (ch[i] > 0x80) {
                count++;
            }
            count++;
        }
        return count;
    }

    /**
     * @param str
     * @param format
     * @return #, ###, ###, ###
     */
    public static String getFormat(String str, String format) {
        if (format == null || format.equals("")) {
            format = "###,###,###.###";
        }
        String temp = null;
        if (str == null) {
            temp = "0";
        } else {
            double change = Double.valueOf(str).doubleValue();
            DecimalFormat decimal = new DecimalFormat(format);
            temp = decimal.format(change);
        }
        return temp;
    }

    /**
     * @param istr
     * @param format
     * @return #, ###, ###, ###
     */
    public static String getFormat(int istr, String format) {
        String str = Integer.toString(istr);
        if (format == null || format.equals("")) {
            format = "###,###,###.###";
        }
        String temp = null;
        if (str == null) {
            temp = "0";
        } else {
            double change = Double.valueOf(str).doubleValue();
            DecimalFormat decimal = new DecimalFormat(format);
            temp = decimal.format(change);
        }
        return temp;
    }

    public static boolean isAlphanumeric(String str) {
        return StringUtil.isAlphanumeric(str);
    }

    public static String getRandomStr() {
        Random random = new Random();
        long l = random.nextLong();
        return Long.toString(l);
    }

    /**
     * @param inputValue
     * @param src
     * @param dist
     * @return String
     */
    public static String getReplace(String inputValue, String src, String dist) {
        return inputValue.replaceAll(src, dist);
    }

    public static String[] toArray(Object src) {
        return toArray((String) src);
    }

    public static String[] toArray(Object src, String div) {
        return toArray((String) src, div);
    }

    public static String[] toArray(String src) {
        String div = "|";
        if (src.indexOf(div) != -1) {
            return toArray(src, "[|]");
        } else {
            return toArray(src, "[,]");
        }
    }

    public static String[] toArray(String src, String div) {
        if (div.indexOf('[') == -1) {
            div = "[" + div + "]";
        }
        return src.split(div);
    }

    public static String toUnicode(String s) {
        StringBuffer uni_s = new StringBuffer();
        String temp_s = null;
        final String U = "%u";
        final String OO = "00";
        for (int i = 0; i < s.length(); i++) {
            temp_s = Integer.toHexString(s.charAt(i));
            uni_s.append(U).append((temp_s.length() == 4 ? temp_s : OO + temp_s));
        }
        return uni_s.toString();
    }

    public static BigDecimal toDecimal(String value) {
        BigDecimal _return = BigDecimal.valueOf(0);
        try {
            _return = BigDecimal.valueOf(toDouble(value));
        } catch (Exception e) {
            _return = BigDecimal.valueOf(0);
        }
        return _return;
    }

    public static float toFloat(String value) {
        float _return = 0;
        try {
            _return = Float.parseFloat(value);
        } catch (Exception e) {
            _return = 0;
        }
        return _return;
    }

    public static double toDouble(String value) {
        double _return = 0;
        try {
            _return = Double.parseDouble(value);
        } catch (Exception e) {
            _return = 0;
        }
        return _return;
    }

    public static int toInt(String value) {
        int _return = 0;
        try {
            _return = Integer.parseInt(value);
        } catch (Exception e) {
            _return = 0;
        }
        return _return;
    }

    public static long toLong(String value) {
        long _return = 0;
        try {
            _return = Long.parseLong(value);
        } catch (Exception e) {
            _return = 0;
        }
        return _return;
    }

    public static String getEllipsis(String source, int width) {
        return getEllipsis(source, width, 12);
    }

    public static String getEllipsis(String source, int width, int fontSize) {
        if (isEmpty(source)) {
            return "";
        }
        // Logs.output(source);
        String returns = "";
        boolean isEllipsis = false;
        int length = 0;
        char[] chr;
        chr = source.toCharArray();
        int textWidth = 0;
        try {
            for (char c : chr) {
                if (width > textWidth) {
                    length++;
                    byte[] b = String.valueOf(c).getBytes("UTF-8");
                    if (1 < b.length) {
                        textWidth = textWidth + fontSize;
                    } else {
                        textWidth = textWidth + fontSize / 2;
                    }
                } else {
                    isEllipsis = true;
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        returns = source;
        if (length < source.length()) {
            returns = source.substring(0, length);
        }
        if (isEllipsis) {
            returns = returns + "…";
        }
        return StringEscapeUtils.escapeHtml(returns);
    }

    public static String formartDateTime(Object date, String format) {
        String returns = "";
        try {
            if (date != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                returns = dateFormat.format(date);
            }
        } catch (Exception ex) {
        }
        return returns;
    }

    public static void main(String[] args) {
        String value = rightPadding("name", 20, "=");
        System.out.println(value);
    }

    public static String convertNullToEmpty(String value) {
        return (value == null) ? "" : value;
    }

    static public String convertNullToEmpty(Object object, String defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        String value = String.valueOf(object);
        return IsNullOrEmpty(value) ? defaultValue : value;
    }

    static public String convertNullToEmpty(Object object) {
        return convertNullToEmpty(object, "");
    }

    static public int convertNullToEmpty(Object object, int defaultValue) {
        if (IsNullOrEmpty(object)) {
            return defaultValue;
        } else {
            int value = toInt(convertNullToEmpty(object));
            return value == 0 ? defaultValue : value;
        }
    }

    public static boolean IsNullOrEmpty(Object value) {
        return !(value != null && !"".equals(value));
    }

    public static String getDateTime() {
        return getDateTime(DateType.BySecond, 0);
    }

    public static String getDateTime(String format) {
        return getDateTime(DateType.BySecond, 0, format);
    }

    public static String getDateTime(Date date) {
        return getDateTime(DateType.BySecond, 0, date);
    }

    public static String getDateTime(Date date, String format) {
        return getDateTime(DateType.BySecond, 0, date, format);
    }

    public static String getDateTime(DateType interval, int increment) {
        return getDateTime(interval, increment, "yyyy-MM-dd");
    }

    public static String getDateTime(DateType interval, int increment, String format) {
        return getDateTime(interval, increment, null, format);
    }

    public static String getDateTime(DateType interval, int increment, Date date) {
        return getDateTime(interval, increment, date, "yyyy-MM-dd");
    }

    public static String getDateTime(DateType interval, int increment, Date date, String format) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        if (increment != 0) {
            switch (interval) {
                case ByDate:
                    calendar.add(Calendar.DATE, increment);
                    break;
                case ByHour:
                    calendar.add(Calendar.HOUR, increment);
                    break;
                case ByMinute:
                    calendar.add(Calendar.MINUTE, increment);
                    break;
                case ByMonth:
                    calendar.add(Calendar.MONTH, increment);
                    break;
                case BySecond:
                    calendar.add(Calendar.SECOND, increment);
                    break;
                case ByYear:
                    calendar.add(Calendar.YEAR, increment);
                    break;
                default:
                    break;
            }
        }
        return formartDateTime(calendar.getTime(), format);
    }

    public static String formartDateTime(String date) {
        return formartDateTime(SQLDateTimeUtils.parseToTimestamp(date), "yyyy-MM-dd HH:mm:ss");
    }

    public static String formartDateTime(Date date) {
        return formartDateTime(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formartDateTime(String date, String format) {
        return formartDateTime(SQLDateTimeUtils.parseToTimestamp(date), format);
    }

    // TODO CHECK AND TEST THIS METHOD
    public static String getImgSrc(String con, String token, String uploadBaseDir, String fileUrlKeyWord) {
        Pattern p = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m = p.matcher(con);
        boolean result_img = m.find();// 匹配2223
        if (result_img) {
            while (result_img) {
                String str_img = m.group(2);// 返回
                Pattern p_src = Pattern.compile("( src| SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    if (!m_src.group(3).trim().startsWith("http://mmbiz.")) {// 此处判断图片是否来自微信 ，如果不是就先下载再上传到微信
                        String m_pic = m_src.group(3);
                        System.out.println("预发布图文消息的图片上传处理m_pic-0：：" + m_pic);
                        // TODO 上线变更
                        if (m_src.group(3).trim().startsWith("http://www.xxxxxx.com.cn")) {
                            m_pic = m_pic.replace("http://www.xxxxxx.com.cn", "");
                        }
                        // TODO 上线变更
                        if (m_src.group(3).trim().startsWith("http://www.xxxxxx.com")) {
                            m_pic = m_pic.replace("http://www.xxxxxx.com", "");
                        }
                        /**
                         * 删除 XIUMI编辑器图片结尾参数
                         */
                        if (m_src.group(3).trim().endsWith("?x-oss-process=style/xm")) {
                            m_pic = m_pic.replace("?x-oss-process=style/xm", "");
                        }
                        System.out.println("预发布图文消息的图片上传处理m_pic-1：：" + m_pic);

                        String imgPath = null;
                        if (m_pic.startsWith("http://")) {
                            // imgPath = saveImage(uploadBaseDir, m_pic);
                            imgPath = saveImage1(uploadBaseDir, m_pic, fileUrlKeyWord);
                        } else {
                            // 如果m_pic为/res/images...则走此逻辑，imgPath即为物理绝对路径
                            imgPath = uploadBaseDir + m_pic;
                        }
                        System.out.println("预发布图文消息的图片上传处理imgPath：：" + imgPath);

                        // 上传到微信服务器
                        MediaUrlReturn mediaUrlReturn = WeixinMediaUtils.uploadingMedia(token, imgPath);
                        // MediaUrlReturn mediaUrlReturn = WeixinMediaUtils.uploadingMedia(token, uploadBaseDir+m_pic);
                        // MediaUrlReturn mediaUrlReturn = WeixinMediaUtils.uploadingMedia(token, m_src.group(3));
                        String Conurl = mediaUrlReturn.getUrl();
                        if (m_src.group(3).trim().endsWith("?x-oss-process=style/xm")) {
                            con = con.replaceFirst(m_pic, Conurl);
                        } else {
                            con = con.replaceFirst(m_src.group(3), Conurl);
                        }
                    }
                }
                result_img = m.find();
            }
        }
        return con;
    }

    public static String saveImage(String BaseDir, String imgUrl) {
        URL url = null;
        try {
            url = new URL(imgUrl);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        // 后缀名
        String suffix = imgUrl.substring(imgUrl.trim().lastIndexOf(".") + 1);
        String imagePath = BaseDir + "/uploads/editor/temp_image/" + UUID.randomUUID().toString() + "." + suffix;
        try {
            FileUtils.copyURLToFile(url, new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public static String saveImage1(String BaseDir, String imgUrl, String fileUrlKeyWord) {
        imgUrl = imgUrl.substring(imgUrl.indexOf("/res/") + "/res/".length());
        String imagePath = BaseDir + imgUrl;
        return imagePath;
    }
}