package com.basoft.eorder.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

  /*  public static void addCookie(HttpServletResponse response, String name, String value, int max) {
        if (StringUtils.isNotBlank(value)) {
            String expires = DateUtil.getGMTDateStr(max * 1000);
            response.addHeader("Set-Cookie",name + "=" + value+";expires="+expires+";Max-Age="+max+";Domain="+ AppConfigure.BASOFT_DOMAIN +";Path=/;HTTPOnly;");
        } else {
            response.addHeader("Set-Cookie", name + "=;expires=0;Max-Age=" + max + ";Domain="+AppConfigure.BASOFT_DOMAIN+";Path=/;HTTPOnly");
        }
    }

    public static void addCookie(HttpServletResponse response, String name, String value) {
        if (StringUtils.isNotBlank(value)) {
            response.addHeader("Set-Cookie", name + "=" + value +";Domain=" + AppConfigure.BASOFT_DOMAIN+";Path=/;HTTPOnly");
        } else {
            response.addHeader("Set-Cookie", name + "=;Domain="+AppConfigure.BASOFT_DOMAIN+"Path=/;HTTPOnly");
        }
    }*/

    public static String getCookieName(HttpServletRequest request, String name) {
        Cookie cookie = getCookieByName(request, name);
        if (cookie != null) {
            return cookie.getName();
        } else {
            return "";
        }
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookieByName(request, name);

        if (cookie != null) {
            return cookie.getValue();
        } else {
            return "";
        }
    }

    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

/*    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, String name) {
        Cookie cookie = getCookieByName(request, name);
        if (cookie != null) {
            addCookie(response, cookie.getName(), null, 0);
        }
    }

    public static void deleteAllCookie(HttpServletRequest request,
                                       HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                addCookie(response, cookie.getName(), null, 0);
            }
        }
    }*/

/*    public static String getCookieValue(HttpServletRequest request,	HttpServletResponse response, String name) {
        Cookie cookie = getCookieByName(request, name);
        Cookie cookieTime = getCookieByName(request, AppConfigure.BASOFT_COOKIE_TIME);

        if (cookie != null && cookieTime != null) {
            return cookie.getValue();
        } else {
            deleteCookie(request, response, name);
            deleteCookie(request, response, AppConfigure.BASOFT_COOKIE_TIME);
            deleteCookie(request, response, AppConfigure.BASOFT_COOKIE_TOKEN);
            return null;
        }
    }*/
}
