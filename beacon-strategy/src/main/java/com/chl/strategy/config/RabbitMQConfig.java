package com.chl.strategy.config;

import com.chl.common.constant.RabbitMQConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建RabbitMQ队列交换机以及绑定配置类
 */
@Configuration
public class RabbitMQConfig {
    /**
     * 策略模块发送消息到数据库进行数据同步队列
     */
    @Bean
    public Queue preSendQueue(){
        return QueueBuilder.durable(RabbitMQConstants.MOBILE_AREA_OPERATOR).build();
    }

    /**
     * 写日志到Elasticsearch中队列
     */
    @Bean
    public Queue smsWriteLogQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_WRITE_LOG).build();
    }

    /**
     * 创建推送回调消息到客户的队列对象
     */
    @Bean
    public Queue pushReportQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_PUSH_REPORT).build();
    }
}
