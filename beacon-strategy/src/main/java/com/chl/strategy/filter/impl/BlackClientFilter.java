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

/**
 * 客户级黑名单校验
 */
@Service(value = "blackClient")
@Slf4j
public class BlackClientFilter implements StrategyFilter {
    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    // 黑名单的默认value
    private final String TRUE = "1";

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-客户级黑名单校验】 校验ing ~ ~");

        //1、获取用户电话号码
        String mobile = submit.getMobile();
        Long clientId = submit.getClientId();

        //2、查询平台级用户黑名单
        String value = cacheClient.getString(SmsConstant.BLACK + clientId + SmsConstant.SEPARATOR + mobile);

        //3、判断该号码是否是平台级黑名单
        if (TRUE.equals(value)){
            //发送消息和推送报告和抛出异常
            log.info("【策略模块-客户级黑名单校验】该号码为客户级黑名单无法发送消息 mobile = {}",mobile);
            submit.setErrorMsg("该用户电话号码为客户级黑名单无法发送消息，请到对应平台联系管理员解除黑名单 mobile = " + mobile);
            sendMsgUtil.sendLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnum.BLACK_CLIENT);
        }

        //4、通过检验日志
        log.info("【策略模块-客户级黑名单校验】客户级黑名单校验审核通过！");
    }
}
