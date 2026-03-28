package com.chl.api.enums;

public enum SmsEnum {
    PARAMETER_ERROR(-10,"参数不合法");

    private Integer code;
    private String msg;

    SmsEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
