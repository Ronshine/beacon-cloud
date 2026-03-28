package com.chl.strategy.util;

import com.chl.common.enums.MobileTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据运营商枚举获取对应数字id的工具类
 */
public class OperatorUtil {

    private static Map<String,Integer> operators = new HashMap<>();

    static {
        MobileTypeEnum[] mobileTypeEnums = MobileTypeEnum.values();
        for (MobileTypeEnum mobileTypeEnum : mobileTypeEnums) {
            operators.put(mobileTypeEnum.getOperatorName(),mobileTypeEnum.getOperatorId());
        }
    }

    /**
     * 根据运营商名称获取对应运营商id
     * @param operatorName 运营商名称
     * @return 返回运营商id
     */
    public static Integer getOperatorIdByOperatorName(String operatorName){
        return operators.get(operatorName);
    }
}
