package com.yiyayaya.shopmanage.utils;

import java.util.regex.Pattern;

public class ValidationUtil {

    // 正则表达式用于验证手机号
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$"; // 以1开头，第二位为3-9，后面9位数字
    // 正则表达式用于验证邮箱
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    /**
     * 验证手机号格式
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValidPhone(String phone) {
        return Pattern.matches(PHONE_REGEX, phone);
    }

    /**
     * 验证邮箱格式
     * @param email 邮箱
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }
} 