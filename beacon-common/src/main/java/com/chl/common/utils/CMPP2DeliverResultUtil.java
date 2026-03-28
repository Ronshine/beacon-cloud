package com.chl.common.utils;

import com.chl.common.enums.CMPP2Enums;
import com.chl.common.enums.CMPPDeliverEnums;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据运营商枚举获取对应数字id的错误信息二次回调
 */
public class CMPP2DeliverResultUtil {

    private static final Map<String,String> description = new HashMap<>();

    static {
        CMPPDeliverEnums[] cmppDeliverEnums = CMPPDeliverEnums.values();
        for (CMPPDeliverEnums cmppDeliverEnum : cmppDeliverEnums) {
            description.put(cmppDeliverEnum.getStat(),cmppDeliverEnum.getDescription());
        }
    }

    /**
     * 根据运营商发送消息获取错误信息二次回调
     * @param stat 运营商发送消息标识
     * @return 返回对应错误信息
     */
    public static String getErrorMessageByResult(String stat){
        return description.get(stat);
    }
}
