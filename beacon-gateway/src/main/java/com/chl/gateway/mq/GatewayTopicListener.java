package com.chl.gateway.mq;

import com.chl.common.model.StandardSubmit;
import com.chl.common.utils.CMPPSubmitRespUtil;
import com.chl.gateway.netty4.NettyClient;
import com.chl.gateway.netty4.entity.CmppSubmit;
import com.chl.gateway.netty4.utils.Command;
import com.chl.gateway.netty4.utils.MsgUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GatewayTopicListener {
    @Autowired
    private NettyClient nettyClient;

    @RabbitListener(queues = "${gateway.sendTopic}")
    public void consume(StandardSubmit submit, Channel channel, Message message) throws IOException {
        log.info("【短信网关模块】 接收到消息 submit = {}",submit);
        // =====================完成运营商交互，发送一次请求，接收两次响应==========================
        //1、获取需要传输给运营商的信息
        String srcNumber = submit.getSrcNumber();
        String mobile = submit.getMobile();
        String text = submit.getText();
        // 这个序列是基于++实现的，当取值达到MAX时，会被重置，这个值是可以重复利用的。
        int sequence = MsgUtils.getSequence();
        //2、创建与运营商交互对象
        CmppSubmit cmppSubmit = new CmppSubmit(Command.CMPP2_VERSION,srcNumber,sequence,mobile,text);
        //创建一个临时存储对象HashMap
        CMPPSubmitRespUtil.put(sequence + "",submit);
        nettyClient.submit(cmppSubmit);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
