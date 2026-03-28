package com.chl.common.exception;

import com.chl.common.enums.ExceptionEnum;

/**
 * 校验模块运行异常处理
 */
public class ApiException extends RuntimeException{
    private final Integer code;

    public ApiException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public ApiException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
    }
}
