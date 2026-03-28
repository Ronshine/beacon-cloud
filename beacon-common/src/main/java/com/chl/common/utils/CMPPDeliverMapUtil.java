package com.chl.common.utils;

import com.chl.common.model.StandardReport;
import com.chl.common.model.StandardSubmit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 临时存储与运营商对话对象的工具类
 */
public class CMPPDeliverMapUtil {
    private static final ConcurrentHashMap<String, StandardReport> map = new ConcurrentHashMap<>();

    /**
     * 存储
     * @param msgId 唯一标识
     * @param report 具体内容
     */
    public static void put(String msgId,StandardReport report){
        map.put(msgId,report);
    }

    /**
     * 获取
     */
    public static StandardReport get(String msgId){
        return map.get(msgId);
    }

    /**
     * 删除
     */
    public static StandardReport remove(String msgId){
        return map.remove(msgId);
    }
}
