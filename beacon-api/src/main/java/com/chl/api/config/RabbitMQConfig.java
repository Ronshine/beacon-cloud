package com.chl.api.config;

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
    @Bean
    public Queue preSendQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_PRE_SEND).build();
    }
}
