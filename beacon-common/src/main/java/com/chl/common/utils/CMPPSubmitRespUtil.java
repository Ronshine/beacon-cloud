package com.chl.common.utils;

import com.chl.common.model.StandardSubmit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 临时存储与运营商对话对象的工具类
 */
public class CMPPSubmitRespUtil {
    private static ConcurrentHashMap<String, StandardSubmit> map = new ConcurrentHashMap<>();

    /**
     * 存储
     * @param sequence 唯一标识
     * @param submit 具体内容
     */
    public static void put(String sequence,StandardSubmit submit){
        map.put(sequence,submit);
    }

    /**
     * 获取
     */
    public static StandardSubmit get(String sequence){
        return map.get(sequence);
    }

    /**
     * 删除
     */
    public static StandardSubmit remove(String sequence){
        return map.remove(sequence);
    }
}
