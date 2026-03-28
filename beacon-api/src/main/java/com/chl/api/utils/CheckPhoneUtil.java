package com.chl.api.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPhoneUtil {
    /**
     * 验证国内手机号的正则表达式
     */
    public static final Pattern CHINA_PATTERN = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");

    /**
     * 判断是否是中国号码
     * @param number 传入的号码
     */
    public static boolean isChinaPhone(String number){
        Matcher matcher = CHINA_PATTERN.matcher(number);
        return matcher.matches();
    }
}
