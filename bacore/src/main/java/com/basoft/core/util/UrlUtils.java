package com.basoft.core.util;

import com.basoft.core.constants.CoreConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 此类用于处理URL
 */
public class UrlUtils {
    private static final Logger LOG = LoggerFactory.getLogger(UrlUtils.class);

    public static final String URL_REALM = "(http://|ftp://|https://|www)?[^\\s]*?\\.(com\\.cn|com|net|cn|me|tw|fr)";


    /**
     * 对url 采取编码(UTF-8).
     * @param url
     * @return
     */
    public static String encodeUrl(String url) {
        if(StringUtils.isNotBlank(url)) {
            try {
                return URLEncoder.encode(url, CoreConstants.URL_ENCODE_NAME_UTF_8);
            } catch (Exception e) {
                LOG.error("对URL编码出错：" + url.toString() + e.getMessage(), e);
            }
        }

        return url;
    }

    /**
     * 对url 采取解码(UTF-8).
     * @param url
     * @return
     */
    public static String decodeUrl(String url) {
        if(StringUtils.isNotBlank(url)) {
            try {
                return URLDecoder.decode(url, CoreConstants.URL_ENCODE_NAME_UTF_8);
            } catch (Exception e) {
                LOG.error("对URL编码出错：" + url.toString() + e.getMessage(), e);
            }
        }

        return url;
    }

    public static boolean isUrl(String imageSegment) {
        // the regex was from http://daringfireball.net/2010/07/improved_regex_for_matching_urls
        String regex = "(?i)\\b((?:[a-z][\\w-]+:(?:/{1,3}|[a-z0-9%])|www\\d{0,3}[.]|[a-z0-9.\\-]" +
                "+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(imageSegment);

        return matcher.matches();
    }

    public static boolean match(String str, String regex) {
        if (StringUtil.isNotBlank(str)) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);

            return m.find();
        }

        return false;
    }

    public static String appendParamsToUrl(String url, String pv) {
        if(StringUtil.isNotBlank(url) && StringUtil.isNotBlank(pv)) {
            url += (url.indexOf("?") > 0 ? "&" : "?") + pv;
        }

        return url;
    }
}
