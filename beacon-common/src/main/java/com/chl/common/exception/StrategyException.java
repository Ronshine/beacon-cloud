package com.chl.common.exception;

import com.chl.common.enums.ExceptionEnum;

/**
 * 策略模块运行自定义异常
 */
public class StrategyException extends RuntimeException{
    private static final long serialVersionUID = 6943693089383362267L;

    //状态码
    private final Integer code;

    public StrategyException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public StrategyException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
    }
}
