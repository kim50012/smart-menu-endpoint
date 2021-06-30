package com.basoft.eorder.interfaces.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class ServletUtils {

    private static final Logger logger = LoggerFactory.getLogger(ServletUtils.class);

    public static void printHeader(HttpServletRequest request) {
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String hname = headerNames.nextElement();
//            logger.info("H:"+hname+" val:"+request.getHeader(hname));
        }
    }
}
