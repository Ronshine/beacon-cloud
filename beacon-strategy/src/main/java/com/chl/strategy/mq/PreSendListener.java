package com.chl.strategy.mq;

import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.StrategyException;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.filter.StrategyFilterContext;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class PreSendListener {
    @Autowired
    private StrategyFilterContext strategyFilterContext;

    @RabbitListener(queues = {RabbitMQConstants.SMS_PRE_SEND})
    public void listener(StandardSubmit submit, Message message, Channel channel) throws IOException {
        log.info("【策略模块-接收信息】 接收信息成功 ！submit = {}",submit);

        try {
            strategyFilterContext.strategy(submit);
            log.info("【策略模块-消费信息】 手动ack");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (StrategyException ex) {
            log.info("【策略模块-消费失败】不知道被哪个良子吃了awa 错误信息ex = {} ",ex.getMessage());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }
    }

}
