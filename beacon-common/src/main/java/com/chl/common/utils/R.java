package com.chl.common.utils;

import com.chl.common.enums.ExceptionEnum;
import com.chl.common.vo.ResultVO;

/**
 * 后端返回前端响应信息工具
 */
public class R {
    /**
     * 正确响应
     * @return ResultVO响应对象
     */
    public static ResultVO ok(){
        return new ResultVO(0,"");
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
