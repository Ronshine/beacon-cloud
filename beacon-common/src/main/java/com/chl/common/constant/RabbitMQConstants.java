package com.chl.common.constant;

public interface RabbitMQConstants {

    /**
     * api模块发送消息至策略模块队列名称
     */
    String SMS_PRE_SEND = "sms_pre_send";

    /**
     * 策略模块发送消息至短信后台模块
     */
    String MOBILE_AREA_OPERATOR = "mobile_area_operator";

    /**
     * 写日志到Elasticsearch
     */
    String SMS_WRITE_LOG = "sms_write_log";

    /**
     * 推送回调消息到客户的队列
     */
    String SMS_PUSH_REPORT = "sms_push_report";

    /**
     * 推送短信路由模块发送信息队列
     */
    String SMS_GATEWAY = "sms_gateway_topic_";

    /**
     * 短信网关模块死信队列以及交换器和正常
     */
    String SMS_NORMAL_EXCHANGE = "sms_normal_exchange";
    String SMS_NORMAL_QUEUE = "sms_normal_queue";
    String SMS_DEAD_EXCHANGE = "sms_dead_exchange";
    String SMS_DEAD_QUEUE = "sms_dead_queue";


}
