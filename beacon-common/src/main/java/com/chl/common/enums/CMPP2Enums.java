package com.chl.common.enums;

public enum CMPP2Enums {
    OK(0,"正确"),
    MSG_STRUCT_ERR(1,"消息结构错误"),
    CMD_ERR(2,"命令字错"),
    SEQ_DUP(3,"消息序号重复"),
    LEN_ERR(4,"消息长度错"),
    TARIFF_ERR(5,"资费代码错"),
    EXCEED_MAX_LEN(6,"超过最大信息长"),
    SERVICE_ERR(7,"业务代码错"),
    FLOW_CTRL_ERR(8,"流量控制错"),
    OTHER_ERR(9,"其他错误"),
    ;
    private Integer result;
    private String errorMessage;

    CMPP2Enums(Integer result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public Integer getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
