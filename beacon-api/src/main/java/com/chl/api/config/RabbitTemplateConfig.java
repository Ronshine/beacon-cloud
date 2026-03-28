package com.chl.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitTemplateConfig {
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        //创建template对象
        RabbitTemplate rabbitTemplate = new RabbitTemplate();

        //配置连接工厂
        rabbitTemplate.setConnectionFactory(connectionFactory);

        //配置confirm机制
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack){
                    log.error("【接口模块-发送信息】消息没有发送到交换机 correlationData = {}，cause = {}",correlationData,cause);
                }
            }
        });

        //配置return机制
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {

            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.error("【接口模块-发送信息】消息没有发送到队列中 message = {},exchange = {},routingKey = {}",new String(message.getBody()),exchange,routingKey);
            }
        });

        //返回RabbitTemplate
        return rabbitTemplate;
    }
}
