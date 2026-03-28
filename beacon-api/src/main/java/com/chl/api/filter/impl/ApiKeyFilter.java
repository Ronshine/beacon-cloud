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
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Service(value = "apikey")
@Slf4j
public class ApiKeyFilter implements CheckFilter {
    @Autowired
    private BeanCacheClient beanCacheClient;

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验apikey】apikey校验中！！！！");
        //根据apikey查询
        Map clientBusiness = beanCacheClient.getMapByKey(ApiConstants.CLIENT_BUSINESS + submit.getApikey());

        //进行校验
        if (ObjectUtils.isEmpty(clientBusiness) && clientBusiness.size() == 0){
            log.info("【接口模块-校验apikey】非法apikey:{}",submit.getApikey());
            throw new ApiException(ExceptionEnum.ERROR_APIKEY);
        }

        //封装信息
        submit.setClientId(Long.parseLong(clientBusiness.get("id")+""));
        log.info("【接口模块-校验apikey】 查询到客户信息 clientBusiness = {}",clientBusiness);
    }
}
