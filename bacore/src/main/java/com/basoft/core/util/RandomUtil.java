package com.basoft.core.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {
    public static int generateNumber(int max) {
        return new SecureRandom().nextInt(max);
    }

    public static String generateNumberString(int length) {
        int random = new SecureRandom().nextInt(10 ^ length - 1);
        return String.format("%0"+ length +"d", random);
    }

    public static String generateAZaz09String(int length) {
        return generateString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray(), length);
    }

    public static String generateAZazString(int length) {
        return generateString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray(), length);
    }

    public static String generateAZ09String(int length) {
        return generateString("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray(), length);
    }

    public static String generate09String(int length) {
        return generateString("1234567890".toCharArray(), length);
    }

    private static String generateString(char[] chars, int length) {
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = chars[random.nextInt(chars.length)];
        }
        return new String(result);
    }
}
