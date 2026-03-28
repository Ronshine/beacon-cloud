package com.chl.push.mq;

import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.model.StandardReport;
import com.chl.common.utils.JsonUtil;
import com.chl.push.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@Slf4j
public class PushReportListener {
    /**
     * 延迟推送时间规则数组
     */
    private final int[] delay = {0,15000,30000,60000,300000};//0秒 15秒 30秒 1分钟 5分钟

    //判断推送是否成功标识
    private final String SUCCESS = "SUCCESS";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConstants.SMS_PUSH_REPORT)
    public void consume(StandardReport report, Channel channel, Message message) throws IOException {
        //1、获取发送状态报告的url
        String callbackUrl = report.getCallbackUrl();
        if (callbackUrl.isEmpty()){
            log.info("【推送模块-推送状态报告】 推送客户没有设置通知地址！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        //2、封装restTemplate发送请求推送报告
        boolean flag = pushReport(report);

        //3、根据报告推送是否成功进行重试
        isResend(report, flag);

        //4、手动ack(失败或者成功都需要进行手动ack)
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    @RabbitListener(queues = RabbitMQConfig.PUSH_DELAYED_QUEUE)
    public void delayedConsume(StandardReport report, Channel channel, Message message) throws IOException {
        //1、封装restTemplate发送请求推送报告
        boolean flag = pushReport(report);

        isResend(report, flag);


        //2、手动ack(失败或者成功都需要进行手动ack)
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    private boolean pushReport(StandardReport report) {
        //声明返回结果(默认为false)
        boolean flag = false;

        //1、通过报告对象获取当前信息并转成json,并设置请求头
        String body = JsonUtil.obj2json(report);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //2、通过restTemplate进行发送请求
        String result = null;
        try {
            log.info("【推送模块-推送状态报告】第{}次推送状态报告开始！！report = {}",report.getResendCount() + 1,report);
            result = restTemplate.postForObject("http://" + report.getCallbackUrl(), new HttpEntity<>(body, httpHeaders), String.class);
            flag = SUCCESS.equals(result);
        } catch (RestClientException ignored) {
        }
        return flag;
    }

    private void isResend(StandardReport report, boolean flag) {
        //2、根据报告推送是否成功进行重试
        if (!flag){
            log.info("【推送模块-推送状态报告】 第{}次推送报告失败！！report = {}", report.getResendCount() + 1, report);
            //达到最大重试错误，并防止数组越界
            if (report.getResendCount() >= delay.length - 1){
                log.info("【推送模块-推送状态报告】已经重试{}次,未成功发送放弃该信息！！！",report.getResendCount());
                return;
            }
            //将重置次数+1进行下一时间延迟
            report.setResendCount(report.getResendCount() + 1);
            rabbitTemplate.convertAndSend(RabbitMQConfig.PUSH_DELAYED_EXCHANGE, "", report, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //对数据进行重试操作
                    message.getMessageProperties().setDelay(delay[report.getResendCount()]);
                    return message;
                }
            });
        }else {
            log.info("【推送模块-推送状态报告】 第{}次推送状态报告开始！！report = {}", report.getResendCount() + 1, report);
        }
    }
}
