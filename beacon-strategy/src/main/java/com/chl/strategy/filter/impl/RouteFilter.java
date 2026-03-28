package com.chl.strategy.filter.impl;

import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.constant.SmsConstant;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.StrategyException;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.client.BeaconCacheClient;
import com.chl.strategy.filter.StrategyFilter;
import com.chl.strategy.util.ErrorSendMsgUtil;
import com.chl.strategy.util.TransferChannelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "route")
@Slf4j
public class RouteFilter implements StrategyFilter {

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-路由模块】 校验ing ~ ~");
        //1.获取用户的clientId
        Long clientId = submit.getClientId();

        //2.查询通道绑定信息
        Set<Map> clientChannels = cacheClient.sMemberMap(SmsConstant.CLIENT_CHANNEL + clientId);

        //3.对clientChannels基于clientChannelWeight进行权重排序
        TreeSet<Map> clientChannelWeights = new TreeSet<Map>(new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                Integer o1Weight = (Integer) o1.get("clientChannelWeight");
                Integer o2Weight = (Integer) o2.get("clientChannelWeight");
                return o2Weight - o1Weight;
            }
        });
        clientChannelWeights.addAll(clientChannels);

        boolean ok = false;
        Map<String, Object> channel = null;
        Map clientChannel = null;
        //4.对排序好的客户和通道关系进行可用判断
        for (Map clientWeightChannel : clientChannelWeights) {
            //5、如果客户和通道的绑定关系可用，直接去基于Redis查询具体的通道信息
            if ((int)(clientWeightChannel.get("isAvailable")) != 0){
                // 当前关系不可用，直接进行下次循环，选择权重相对更低一点的
                continue;
            }

            //6、根据客户和通道绑定关系获取通道id并获取对象
            channel = cacheClient.getMapByKey(SmsConstant.CHANNEL + clientWeightChannel.get("channelId"));
            if ((int)(channel.get("isAvailable")) != 0){
                //当前通道不可用进入下一次循环
                continue;
            }

            // 获取通道的通讯方式
            Integer channelType = (Integer) channel.get("channelType");
            if (channelType != 0 && !Objects.equals(submit.getOperatorId(), channelType)){
                // 通道不是全网通，并且和当前手机号运营商不匹配
                continue;
            }

            //7、如果后期需要通道转换给程序留一个通道转换的口子
            Map transferChannel = TransferChannelUtil.transfer(submit, channel);

            //找到了可以使用的通道
            ok = true;
            clientChannel = clientWeightChannel;
            break;
        }
        if (!ok){
            log.info("【策略模块-路由策略】   没有选择到可用的通道！！");
            submit.setErrorMsg( ExceptionEnum.NO_CHANNEL.getMsg());
            sendMsgUtil.sendLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnum.NO_CHANNEL);
        }

        //8、将信息封装进submit
        submit.setChannelId(Long.parseLong(channel.get("id") + ""));
        submit.setSrcNumber("" + channel.get("channelNumber") + clientChannel.get("clientChannelNumber"));

        //9、基于通道信息账户接入号和客户的下发扩展号创建消息队列
        try {
            String queueName = RabbitMQConstants.SMS_GATEWAY + channel.get("id");
            amqpAdmin.declareQueue(QueueBuilder.durable(queueName).build());

            //10、发送消息到rabbitMQ
            rabbitTemplate.convertAndSend(queueName,submit);
        } catch (AmqpException e) {
            log.info("【策略模块-路由策略】 声明通道对应队列以及发送消息时出现了问题！！");
            submit.setErrorMsg(e.getMessage());
            sendMsgUtil.sendLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(e.getMessage(),ExceptionEnum.UNKNOWN_ERROR.getCode());
        }

        log.info("【策略模块-路由策略】  路由通道匹配成功！！");
    }
}
