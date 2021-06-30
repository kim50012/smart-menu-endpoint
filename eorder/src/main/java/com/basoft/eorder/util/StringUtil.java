package com.basoft.eorder.util;


import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * String转base64
     *
     * @param nickName
     * @return
     */
    public static String strToBase64(String nickName) {
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        String base64Sign = null;
        try {
            base64Sign = base64.encodeToString(nickName.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return base64Sign;
    }

    /**
     * @return boolean
     * @Title 判断是否存在特殊字符串
     * @Param content
     * @author Dong Xifu
     * @date 2019/5/10 下午1:59
     */
    public static boolean hasEmoji(String content) {
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * @return java.lang.String
     * @Title 替换字符串中的emoji字符
     * @Param str
     * @author Dong Xifu
     * @date 2019/5/10 下午1:59
     */
    public static String replaceEmoji(String str) {
        return replaceEmojiNew(str);
    }

    public static String replaceEmojiNew(String str) {
        String returnStr = "";
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(ch);
            if (Character.UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock) ||
                    Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock) ||
                    Character.UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock) ||
                    Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(unicodeBlock) ||
                    Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(unicodeBlock) ||
                    Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B.equals(unicodeBlock) ||
                    Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS.equals(unicodeBlock) ||
                    Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT.equals(unicodeBlock) ||
                    Character.UnicodeBlock.HIRAGANA.equals(unicodeBlock) ||
                    Character.UnicodeBlock.KATAKANA.equals(unicodeBlock) ||
                    Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS.equals(unicodeBlock) ||
                    Character.UnicodeBlock.BASIC_LATIN.equals(unicodeBlock))
                returnStr = returnStr + ch;
        }
        return returnStr;
    }


    /**
     * 韩币金额加逗号(无小数)
     * @param data
     * @return
     */
    public static String decimalStr(Long data) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(data);
    }
    /**
     * 韩币金额加逗号(带小数)
     * @param data
     * @return
     */
    public static String decimalObj(Double data) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(data);
    }
}
