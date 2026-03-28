package com.chl.gateway.config;

import com.chl.common.constant.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Exchange normalExchange(){
        return ExchangeBuilder.fanoutExchange(RabbitMQConstants.SMS_NORMAL_EXCHANGE).build();
    }

    @Bean
    public Queue normalQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_NORMAL_QUEUE)
                .ttl(10000)
                .deadLetterExchange(RabbitMQConstants.SMS_DEAD_EXCHANGE)
                .deadLetterRoutingKey("")
                .build();
    }

    @Bean
    public Binding normalBinding(Exchange normalExchange,Queue normalQueue){
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("").noargs();
    }

    @Bean
    public Exchange deadExchange(){
        return ExchangeBuilder.fanoutExchange(RabbitMQConstants.SMS_DEAD_EXCHANGE).build();
    }

    @Bean
    public Queue deadQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_DEAD_QUEUE).build();
    }

    @Bean
    public Binding deadBinding(Exchange deadExchange,Queue deadQueue){
        return BindingBuilder.bind(deadQueue).to(deadExchange).with("").noargs();
    }


    /**
     * 配置rabbitMQ处理信息现场数量
     */
//    @Bean
//    public SimpleRabbitListenerContainerFactory gatewayContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory){
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        //设置多少线程工作
//        factory.setConcurrentConsumers(10);
//        //设置一个线程最大能够处理多少消息
//        factory.setPrefetchCount(50);
//        //手动ack
//        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        //构建配置
//        configurer.configure(factory,connectionFactory);
//        return factory;
//    }

}
