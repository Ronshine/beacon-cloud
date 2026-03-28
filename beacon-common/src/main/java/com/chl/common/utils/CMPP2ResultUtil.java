package com.chl.common.utils;

import com.chl.common.enums.CMPP2Enums;
import com.chl.common.enums.MobileTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据运营商枚举获取对应数字id的错误信息
 */
public class CMPP2ResultUtil {

    private static Map<Integer,String> operators = new HashMap<>();

    static {
        CMPP2Enums[] mobileTypeEnums = CMPP2Enums.values();
        for (CMPP2Enums cmpp2Enum : mobileTypeEnums) {
            operators.put(cmpp2Enum.getResult(),cmpp2Enum.getErrorMessage());
        }
    }

    /**
     * 根据运营商发送消息获取错误信息
     * @param result 运营商发送消息标识
     * @return 返回对应错误信息
     */
    public static String getErrorMessageByResult(Integer result){
        return operators.get(result);
    }
}
