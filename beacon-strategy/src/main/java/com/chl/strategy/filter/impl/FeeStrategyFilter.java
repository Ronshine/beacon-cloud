package com.chl.strategy.filter.impl;

import com.chl.common.constant.ApiConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.StrategyException;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.client.BeaconCacheClient;
import com.chl.strategy.filter.StrategyFilter;
import com.chl.strategy.util.ClientBalanceUtil;
import com.chl.strategy.util.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fee")
@Slf4j
public class FeeStrategyFilter implements StrategyFilter {

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    private final String BALANCE = "balance";

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-扣费模块】 扣费校验进行中。。。。");

        //1.获取用户信息
        Long fee = submit.getFee();
        Long clientId = submit.getClientId();

        //2.获取最低欠费金额限制（先写死）
        Long amountLimit = ClientBalanceUtil.getClientAmountLimit();

        //3.进行扣费操作，并获取当前余额
        Long amount = cacheClient.hIncrBy(ApiConstants.CLIENT_BALANCE + clientId, BALANCE, -fee);


        //4.达到最大金额将扣除的金额添加回来并告诉客户发送失败
        if (amount < amountLimit){
            //5.超过了最大金额扣除限制将扣除金额加上并返回错误
            cacheClient.hIncrBy(ApiConstants.CLIENT_BALANCE + clientId, BALANCE, fee);
            log.info("【策略模块-扣费模块】客户余额不足！！");
            submit.setErrorMsg( ExceptionEnum.BALANCE_NOT_ENOUGH.getMsg());
            sendMsgUtil.sendLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnum.BALANCE_NOT_ENOUGH);
        }

        log.info("【策略模块-扣费模块】扣费成功！！");
    }
}
