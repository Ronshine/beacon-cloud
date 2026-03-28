package com.chl.monitor.tesk;

import com.chl.common.constant.RabbitMQConstants;
import com.chl.monitor.client.CacheClient;
import com.chl.monitor.util.MailUtil;
import com.rabbitmq.client.Channel;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Set;

@Component
@Slf4j
public class MonitorQueueMessageCountTask {
    String text = "<h1>您的队列消息队列堆积了，队名为%s，消息个数为%s</h1>";

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private MailUtil mailUtil;

    // 查询队列名称的固定pattern
    private static final String QUEUE_PATTERN = "channel:*";

    // 得到需要截取channelID的索引地址
    private static final int CHANNEL_ID_INDEX = QUEUE_PATTERN.indexOf("*");

    // 消息堆积最大数量
    private final long MESSAGE_COUNT_LIMIT = 10000;

    @XxlJob("monitorQueueMessageCountTask")
    public void monitor() throws MessagingException {
        //1、获取所有通道的队列名称
        Set<String> keys = cacheClient.keys(QUEUE_PATTERN);

        //2、通过connectionFactory创建channel进行操控MQ
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);

        //对api模块往策略模块进行检测
        listenQueueAndSendEmail(channel,RabbitMQConstants.SMS_PRE_SEND);

        //3、对队列进行遍历依次读取队列数量
        for (String key : keys) {
            String queueName = RabbitMQConstants.SMS_GATEWAY + key.substring(CHANNEL_ID_INDEX);
            listenQueueAndSendEmail(channel, queueName);
        }
    }

    private void listenQueueAndSendEmail(Channel channel, String queueName) throws MessagingException {
        // 队列不存在，直接构建，如果已经存在，直接忽略
        try {
            channel.queueDeclare(queueName,true,false,false,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3、拿到对应队列的消息，确认消息数量，超过限制，及时通知

        long count = 0;
        try {
            //查询该队列有多少条消息，超过最大限制进行邮件通知
            count = channel.messageCount(queueName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (count > MESSAGE_COUNT_LIMIT){
            //4、通知的方式就是发送短信。
            log.info("【监控模块-队列消息数量监听】 邮件发送中-----");
            mailUtil.sendEmail(queueName +"队列消息堆积" , String.format(text,queueName,count));
            log.info("【监控模块-队列消息数量监听】 消息发送成功 ！！！");
        }
    }
}
