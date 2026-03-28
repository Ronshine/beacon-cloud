package com.chl.search.mq;

import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.model.StandardSubmit;
import com.chl.common.utils.JsonUtil;
import com.chl.search.service.EsSearchService;
import com.chl.search.util.SearchUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class SmsWriteLogListener {
    /**
     * 索引前缀
     */
    private final String INDEX = "sms_submit_log_";

    @Autowired
    private EsSearchService esSearchService;

    @RabbitListener(queues = RabbitMQConstants.SMS_WRITE_LOG)
    public void consume(StandardSubmit submit, Channel channel, Message message) throws IOException {

        //1、向elasticsearch中插入消息
        log.info("接收到存储日志的信息   submit = {}",submit.toString());
        esSearchService.index(SearchUtil.INDEX + SearchUtil.getYear(),submit.getSequenceId().toString(), JsonUtil.obj2json(submit));

        //2、手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


}
