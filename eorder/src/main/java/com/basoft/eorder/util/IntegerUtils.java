package com.basoft.eorder.util;

import java.math.BigDecimal;

public class IntegerUtils {
    public static Integer valueOf(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer valueOf(String s, Integer defalut) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return defalut;
        }
    }

    public static Integer valueOf(Object s) {
        try {
            if (s == null) {
                return null;
            }
            return Integer.valueOf(s.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer valueOf(Object s, Integer defalut) {
        try {
            if (s == null) {
                return defalut;
            }
            return Integer.valueOf(s.toString());
        } catch (Exception e) {
            return defalut;
        }
    }

    public static Integer convert2fen(BigDecimal decimal) {
        Integer returns = -1;
        if (decimal != null) {
            decimal = decimal.multiply(new BigDecimal(100));
            returns = decimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        }
        return returns;
    }
}
