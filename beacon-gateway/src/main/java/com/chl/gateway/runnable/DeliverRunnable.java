package com.chl.gateway.runnable;

import com.chl.common.constant.ApiConstants;
import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.constant.SmsConstant;
import com.chl.common.model.StandardReport;
import com.chl.common.utils.CMPP2DeliverResultUtil;
import com.chl.common.utils.CMPPDeliverMapUtil;
import com.chl.gateway.client.BeaconCacheClient;
import com.chl.gateway.netty4.utils.SpringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

public class DeliverRunnable implements Runnable{
    private final BeaconCacheClient cacheClient = SpringUtil.getBeanByClass(BeaconCacheClient.class);

    private final RabbitTemplate rabbitTemplate = SpringUtil.getBeanByClass(RabbitTemplate.class);

    private final long msgId;

    private final String stat;

    public DeliverRunnable(long msgId, String stat) {
        this.msgId = msgId;
        this.stat = stat;
    }

    @Override
    public void run() {
        //1、通过临时存储获取到repo
        StandardReport report = CMPPDeliverMapUtil.get(msgId + "");

        //2、判断当前短信的最终状态
        String DELIVRD = "DELIVRD";
        if (!stat.isEmpty()&&stat.equals(DELIVRD)){
            //发送成功
            report.setReportState(SmsConstant.REPORT_SUCCESS);
        }else {
            //发送失败
            report.setReportState(SmsConstant.REPORT_FAIL);
            report.setErrorMsg(CMPP2DeliverResultUtil.getErrorMessageByResult(stat));
        }

        //3、客户状态报告推送，让网关模块查询缓存，当前客户是否需要状态报告推送
        // 查询当前客户的isCallback
        Integer isCallback = cacheClient.hGetInteger(ApiConstants.CLIENT_BUSINESS + report.getApikey(), SmsConstant.IS_CALLBACK);
        //判断是否需要回调 1-需要 0-不需要
        if (Objects.equals(isCallback, SmsConstant.IS_CALLBACK_YES)){
            //再获取callbackUrl是否为空判断能否发送
            String callbackUrl = cacheClient.hGet(ApiConstants.CLIENT_BUSINESS + report.getApikey(), SmsConstant.CALLBACK_URL);
            if (!StringUtils.isEmpty(callbackUrl)){
                //对report对象进行封装
                report.setIsCallback(isCallback);
                report.setCallbackUrl(callbackUrl);
                //创建回调消息的消息队列
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PUSH_REPORT,report);
            }
        }

        //4、发送消息，让搜索模块对之前写入的信息做修改，这里需要做一个死信队列，延迟10s发送修改es信息的消息
        // 声明好具体的交换机和队列后，直接发送report到死信队列即可
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_NORMAL_EXCHANGE,"",report);
    }
}
