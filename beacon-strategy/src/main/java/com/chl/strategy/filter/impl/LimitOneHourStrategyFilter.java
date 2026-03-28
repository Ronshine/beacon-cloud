package com.chl.strategy.filter.impl;

import com.chl.common.constant.SmsConstant;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.StrategyException;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.client.BeaconCacheClient;
import com.chl.strategy.filter.StrategyFilter;
import com.chl.strategy.util.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 一小时限制发送3条短信限流机制
 */
@Service("limitOneHour")
@Slf4j
public class LimitOneHourStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    private final int RETRY = 2;

    //当前地区时区
    private final String UTC = "+8";

    //1分钟毫秒值
    private final int ONE_HOUR = 60 * 60 * 1000 - 1;

    //一小时可以发送的验证码数量
    private final int LIMIT_COUNT = 3;

    //获取用户对象
    private StandardSubmit submit;

    @Override
    public void strategy(StandardSubmit submit) {
        //判断是否是验证码类型短信
        if (submit.getState() != SmsConstant.MESSAGE_TYPE){
            return;
        }
        log.info("【策略模块-一分钟限流】 一小时限流进行中。。。。");

        //1.获取用户信息发送时间
        LocalDateTime sendTime = submit.getSendTime();

        //2.基于submit获得时间毫秒值
        Long sendTimeMilli = sendTime.toInstant(ZoneOffset.of(UTC)).toEpochMilli();
        //防止和一分钟限流冲突需要将发送时间毫秒值转移到submit中另一个变量
        submit.setOneHourLimit(sendTimeMilli);

        //3.获取用户手机号 clientId信息
        Long clientId = submit.getClientId();
        String mobile = submit.getMobile();

        //4.将短信发送信息发送到redis中保存
        String key = SmsConstant.LIMIT_HOUR + clientId + SmsConstant.SEPARATOR + mobile;
        int retry = 0;
        while (!cacheClient.zAdd(key, submit.getOneHourLimit(), submit.getOneHourLimit())){
            //重试将原来的系统时间设置为当先系统时间
            if (retry == RETRY) {
                break;//重试成功跳出循环
            }
                retry++;
                submit.setOneHourLimit(System.currentTimeMillis());
        }
        //如果重试次数达到2还没有成功进行报错
        if (retry == RETRY){
            log.info("【策略模块-一小时限流】重试次数达到2次发送消息！");
            submit.setErrorMsg( ExceptionEnum.ONE_HOUR_LIMIT + ",mobile = " + mobile);
            sendMsgUtil.sendLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnum.ONE_HOUR_LIMIT);
        }

        //在没有重试达到两次的时候发送成功对当前时间内的信息条数进行范围查询
        Long start = submit.getOneHourLimit() - ONE_HOUR;
        int count = cacheClient.zAddRange(key, start, submit.getOneHourLimit());

        //如果数量大于3条触发限流机制
        if (count > LIMIT_COUNT){
            log.info("【策略模块-一小时限流】一小时内发送的短信超过三条触发一小时限流机制！");
            //将多的这条短信进行删除
            cacheClient.zRemove(key,submit.getOneHourLimit());
            submit.setErrorMsg( ExceptionEnum.ONE_HOUR_LIMIT + ",mobile = " + mobile);
            sendMsgUtil.sendLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnum.ONE_HOUR_LIMIT);
        }

        log.info("【策略模块-一小时限流】一小时限流通过，发送消息！！");
    }
}



