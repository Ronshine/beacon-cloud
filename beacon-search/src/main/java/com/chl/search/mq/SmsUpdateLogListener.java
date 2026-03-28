package com.chl.search.mq;

import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.model.StandardReport;
import com.chl.search.service.EsSearchService;
import com.chl.search.util.SearchUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SmsUpdateLogListener {
    @Autowired
    private EsSearchService service;

    @RabbitListener(queues = {RabbitMQConstants.SMS_NORMAL_QUEUE})
    public void consume(StandardReport report, Channel channel, Message message) throws IOException {
        log.info("【搜素模块-修改日志】 接收到修改日志的消息 report = {}", report);
        //1、将report对象设置进当前线程
        SearchUtil.set(report);

        //2、进行需要更新数据的封装
        Map<String,Object> doc = new HashMap<>();
        doc.put("reportState",report.getReportState());
        service.update(SearchUtil.INDEX + SearchUtil.getYear(), report.getSequenceId().toString() , doc);

        //3、对消息进行ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

}
