package com.chl.strategy.filter.impl;

import com.chl.common.constant.SmsConstant;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.client.BeaconCacheClient;
import com.chl.strategy.filter.StrategyFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "transfer")
@Slf4j
public class TransferFilter implements StrategyFilter {
    @Autowired
    private BeaconCacheClient cacheClient;

    private final boolean TRANSFER = true;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-携号转网校验】 校验ing ~ ~");
        //获取手机号
        String mobile = submit.getMobile();

        //从redis中获取信息
        String value = cacheClient.getString(SmsConstant.TRANSFER + mobile);

        //判断是否查到信息
        if (!StringUtils.isEmpty(value)){
            //不为空证明是携号转网号码
            log.info("【策略模块-携号转网校验】当前号码为携号转网用户！ ");
            submit.setOperatorId(Integer.parseInt(value));
            submit.setTransfer(TRANSFER);
            return;
        }
        log.info("【策略模块-携号转网校验】 当前号码非携号转网用户！");
    }
}
