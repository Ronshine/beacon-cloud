package com.chl.common.constant;

public class SmsConstant {
    /**
     * 发送中短信状态码
     */
    public static final Integer SMS_STATUS_SENDING_CODE = 0;

    /**
     * 发送成功短信状态码
     */
    public static final Integer SMS_STATUS_SUCCESS_CODE = 1;

    /**
     * 发送失败短信状态码
     */
    public static final Integer SMS_STATUS_FAILURE_CODE = 2;

    /**
     * 状态报告回调参数isCallback
     */
    public static final String IS_CALLBACK = "isCallback";

    /**
     * 状态报告回调参数callbackUrl
     */
    public static final String CALLBACK_URL = "callbackUrl";

    /**
     * isCallback是否进行发送状态报告 需要 1
     */
    public static final Integer IS_CALLBACK_YES = 1;

    /**
     * isCallback是否进行发送状态报告 不需要 0
     */
    public static final Integer IS_CALLBACK_NO = 0;

    /**
     * 策略模块校验黑名单前缀
     */
    public static final String BLACK = "black:";

    /**
     * 分割符
     */
    public static final String SEPARATOR = ":";

    /**
     * 携号转网封装数据前缀
     */
    public static final String TRANSFER = "transfer:";

    /**
     * 策略模块一分钟限流去前缀
     */
    public static final String LIMIT_MINUTE = "limit:minute:";

    /**
     * 策略模块一小时限流机制前缀
     */
    public static final String LIMIT_HOUR = "limit:hour:";


    /**
     * 验证码类短信
     */
    public static final int MESSAGE_TYPE = 0;

    /**
     * 通知类短信
     */
    public static final int NOTIFY_TYPE = 1;

    /**
     * 营销类短信
     */
    public static final int MARKETING_TYPE = 2;

    /**
     * 用户与通道绑定前缀
     */
    public static final String CLIENT_CHANNEL = "client_channel:";

    /**
     * 通道缓存前缀
     */
    public static final String CHANNEL = "channel:";

    /**
     * 短信网关模块推送报告成功
     */
    public static final int REPORT_SUCCESS = 1;

    /**
     * 短信网关模块推送报告失败
     */
    public static final int REPORT_FAIL = 2;

    /**
     * 短信网关模块推送报告发送中
     */
    public static final int REPORT_SENDING = 0;
}
