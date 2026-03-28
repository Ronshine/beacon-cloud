package com.chl.strategy.util;

import com.chl.common.constant.ApiConstants;
import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.constant.SmsConstant;
import com.chl.common.model.StandardReport;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.client.BeaconCacheClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 自定义异常发送处理工具
 */
@Component
public class ErrorSendMsgUtil {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BeaconCacheClient cacheClient;

    /**
     * 策略模块校验未通过，写入日志操作
     * @param submit 传入的封装参数对象
     */
    public void sendLog(StandardSubmit submit) {
        //2、封装短信状态码
        submit.setReportState(SmsConstant.SMS_STATUS_FAILURE_CODE);
        //3、通过RabbitMQ写日志到Elasticsearch中
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_WRITE_LOG, submit);
    }

    /**
     * 策略模块校验未通过,发送状态报告
     */
    public void sendPushReport(StandardSubmit submit) {
        //4、通过查询判断用户是否需要回调信息
        Integer isCallback = cacheClient.hGetInteger(ApiConstants.CLIENT_BUSINESS + submit.getApikey(), SmsConstant.IS_CALLBACK);
        //判断是否需要回调 1-需要 0-不需要
        if (Objects.equals(isCallback, SmsConstant.IS_CALLBACK_YES)){
            //再获取callbackUrl是否为空判断能否发送
            String callbackUrl = cacheClient.hGet(ApiConstants.CLIENT_BUSINESS + submit.getApikey(), SmsConstant.CALLBACK_URL);
            if (!StringUtils.isEmpty(callbackUrl)){
                //对report对象进行封装
                StandardReport report = new StandardReport();
                BeanUtils.copyProperties(submit,report);
                report.setIsCallback(isCallback);
                report.setCallbackUrl(callbackUrl);
                //创建回调消息的消息队列
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PUSH_REPORT,report);
            }
        }
    }
}
