package com.chl.strategy.filter.impl;

import com.chl.common.constant.SmsConstant;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.StrategyException;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.client.BeaconCacheClient;
import com.chl.strategy.filter.StrategyFilter;
import com.chl.strategy.util.ErrorSendMsgUtil;
import jdk.nashorn.internal.ir.IfNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 1分钟限流校验
 */
@Service("limitOneMinute")
@Slf4j
public class LimitOneMinuteStrategyFilter implements StrategyFilter {
    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    //当前地区时区
    private final String UTC = "+8";

    //1分钟毫秒值
    private final int ONE_MINUTE = 60*1000 - 1;

    //获取用户对象
    private StandardSubmit submit;

    @Override
    public void strategy(StandardSubmit submit) {
        //判断是否是验证码类型短信
        if (submit.getState() != SmsConstant.MESSAGE_TYPE){
            return;
        }
        log.info("【策略模块-一分钟限流】 一分钟限流进行中。。。。");
        //1.获取用户信息发送时间
        LocalDateTime sendTime = submit.getSendTime();

        //2.基于submit获得时间毫秒值
        Long sendTimeMilli = sendTime.toInstant(ZoneOffset.of(UTC)).toEpochMilli();

        //3.获取用户手机号 clientId信息
        Long clientId = submit.getClientId();
        String mobile = submit.getMobile();

        //4.将短信发送信息发送到redis中保存
        String key = SmsConstant.LIMIT_MINUTE + clientId + SmsConstant.SEPARATOR + mobile;
        boolean addOK = cacheClient.zAdd(key, sendTimeMilli, sendTimeMilli);

        //5.对一分钟内发送条数进行判断(考虑并发同一时间)
        if (!addOK){
            log.info("【策略模块-一分钟限流】出现并发情况同一时间内发送消息！");
            submit.setErrorMsg( ExceptionEnum.ONE_MINUTE_LIMIT + ",mobile = " + mobile);
            sendMsgUtil.sendLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnum.ONE_MINUTE_LIMIT);
        }

        //6.对消息发送时间进行一分钟内范围查询
        Long start = sendTimeMilli - ONE_MINUTE;
        int count = cacheClient.zAddRange(key, start, sendTimeMilli);

        //7.如果消息数量大于两条满足一分钟限 流机制，直接返回无法发送
        if (count > 1){
            log.info("【策略模块-一分钟限流】一分钟内发送信息超过数量限制！");
            //对失败的短信进行删除
            cacheClient.zRemove(key,sendTimeMilli);
            submit.setErrorMsg( ExceptionEnum.ONE_MINUTE_LIMIT + ",mobile = " + mobile);
            sendMsgUtil.sendLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnum.ONE_MINUTE_LIMIT);
        }
        log.info("【策略模块-一分钟限流】 一分钟限流通过，发送消息");
    }
}



