package com.basoft.eorder.util;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class TnuUtil {

    public static String createSign(Map<String, String> info) {
        StringBuilder builder = new StringBuilder();

        Iterator<Map.Entry<String, String>> iterator = info.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            String key = entry.getKey();
            String val = entry.getValue();

            if (StringUtils.isNotBlank(val)
                    && !"sign".equals(key) && !"key".equals(key)
                    && !"return_code".equals(key) && !"return_msg".equals(key)) {
                builder.append(key + "=" + val + "&");
            }
        }
        builder.append("key=" + info.get("key"));

        String sign = DigestUtils.sha256Hex(Objects.toString(builder)).toUpperCase();

        return sign;
    }
}
