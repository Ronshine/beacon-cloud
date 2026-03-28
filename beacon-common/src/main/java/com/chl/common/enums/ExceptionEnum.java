package com.chl.common.enums;

public enum ExceptionEnum {
    ERROR_APIKEY(-1,"非法的apikey"),
    IP_NOT_WHITE(-2,"请求的ip不在白名单内"),
    ERROR_SIGN(-3,"无可用签名"),
    ERROR_TEMPLATE(-4,"无可用模板"),
    ERROR_MOBILE(-5,"手机号格式不正确"),
    BALANCE_NOT_ENOUGH(-6,"客户余额不足"),
    SNOW_OUT_OF_RANGE(-11,"机器ID或服务ID超过最大范围值！！"),
    SNOWFLAKE_TIME_BACK(-12,"当前服务出现时间回拨！！！"),
    HAVE_DIRTY_WORD(-13,"短信内容包含敏感词，审核不通过！"),
    BLACK_GLOBAL(-14,"该号码为平台级黑名单无法发送短信"),
    BLACK_CLIENT(-15,"该号码为客户级黑名单无法发送短信"),
    ONE_MINUTE_LIMIT(-16,"客户触发一分钟限流机制"),
    ONE_HOUR_LIMIT(-17,"客户触发一小时限流机制"),
    NO_CHANNEL(-18,"没有符合通道可以选择"),
    SEARCH_NOT_SEND(-19,"写入数据失败！！"),
    SEARCH_UPDATE_ERROR(-20,"更新数据失败！！"),
    UNKNOWN_ERROR(-999,"未知错误!!!"),
    ;

    private final Integer code;
    private final String msg;

    ExceptionEnum(Integer code, String msg) {
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
