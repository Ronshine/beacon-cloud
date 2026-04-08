package com.chl.common.utils;

import com.chl.common.enums.ExceptionEnum;
import com.chl.common.vo.ResultVO;

import java.util.Map;

/**
 * 后端返回前端响应信息工具
 */
public class R {
    /**
     * 正确响应,无数据
     * @return ResultVO响应对象
     */
    public static ResultVO ok(){
        return new ResultVO(0,"");
    }

    /**
     * 正确响应,有数据
     * @return ResultVO响应对象
     */
    public static ResultVO ok(Map<String,Object> map){
        return new ResultVO(0,"", map);
    }

    /**
     * 错误响应根据枚举返回对应错误码和信息
     * @param enums 对应错误枚举
     * @return ResultVO响应对象
     */
    public static ResultVO error(ExceptionEnum enums){
        return new ResultVO(enums.getCode(),enums.getMsg());
    }
}
