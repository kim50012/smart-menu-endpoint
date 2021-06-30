package com.basoft.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class MyHttpUtils {

    /**
     * getIpAddr: 获取客户端IP <br/>
     * 
     * @author zhiyong.li
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "本地";
        }
        return ip;
    }

    /**
     * getClientOS: 获取客户端的操作系统 <br/>
     * 
     * @author zhiyong.li
     * @param request
     * @return
     */
    public static String getClientOS(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        String cos = "unknow os";

        Pattern p = Pattern.compile(".*(Windows NT 6\\.1).*");
        Matcher m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Windows7";
            return cos;
        }

        p = Pattern.compile(".*(Windows NT 10.0).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Win10";
            return cos;
        }
        
        p = Pattern.compile(".*(Windows NT 5\\.1|Windows XP).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "WinXP";
            return cos;
        }

        p = Pattern.compile(".*(Windows NT 5\\.2).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Win2003";
            return cos;
        }

        p = Pattern.compile(".*(Win2000|Windows 2000|Windows NT 5\\.0).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Win2000";
            return cos;
        }

        p = Pattern.compile(".*(Mac|apple|MacOS8).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "MAC";
            return cos;
        }

        p = Pattern.compile(".*(WinNT|WindowsNT).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "WinNT";
            return cos;
        }

        p = Pattern.compile(".*Linux.*");
        if (m.find()) {
            cos = "Linux";
            return cos;
        }

        p = Pattern.compile(".*68k|68000.*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Mac68k";
            return cos;
        }

        p = Pattern.compile(".*(9x 4.90|Win9(5|8)|Windows 9(5|8)|95/NT|Win32|32bit).*");
        m = p.matcher(userAgent);
        if (m.find()) {
            cos = "Win9x";
            return cos;
        }

        return cos;
    }

    /**
     * getClientBrowser: 获取浏览器信息 <br/>
     * 
     * @author zhiyong.li
     * @param request
     * @return
     */
    public static String getClientBrowser(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    // 得到客户端计算机名
    public static String getComputerName(HttpServletRequest request) {
        return request.getRemoteHost();
    }
}
