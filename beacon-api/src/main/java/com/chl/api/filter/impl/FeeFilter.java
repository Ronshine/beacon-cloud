package com.chl.api.filter.impl;

import com.chl.api.clients.BeanCacheClient;
import com.chl.api.filter.CheckFilter;
import com.chl.common.constant.ApiConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.ApiException;
import com.chl.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "fee")
@Slf4j
public class FeeFilter implements CheckFilter {
    @Autowired
    private BeanCacheClient beanCacheClient;

    /**
     * 短信内容<=70为一条费用
     */
    private static final Integer MAX_LENGTH = 70;

    /**
     * 大于70后每67字计为一条
     */
    private static final Integer EXCEED_LENGTH = 67;

    /**
     * 在存储对象中余额字段名
     */
    private static final String BALANCE = "balance";

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验金额】金额是否充足校验中！！！！");

        //1.获取当前短信长度
        int length = submit.getText().length();

        //2. 判断长度是否小于等于70 大于70 超出的按67字一条收费不足67按一条
        if (length <= MAX_LENGTH){
            //计数为一条
            submit.setFee(ApiConstants.SINGLE_FEE);
        }else {
            Integer pcs = length % EXCEED_LENGTH == 0 ? length / EXCEED_LENGTH : length / EXCEED_LENGTH + 1;
            submit.setFee(ApiConstants.SINGLE_FEE * pcs);
        }

        //3.获取客户余额
        Long balance = ((Integer)beanCacheClient.hGet(ApiConstants.CLIENT_BALANCE + submit.getClientId(), BALANCE)).longValue();

        //4.判断余额是否充足
        if (balance >= submit.getFee()){
            log.info("【接口模块-校验金额】 客户余额充足! ");
            return;
        }

        log.info("【】 客户余额不足 balance = {} 费用为 fee = {}" , balance , submit.getFee());
        throw new ApiException(ExceptionEnum.BALANCE_NOT_ENOUGH);
    }
}
