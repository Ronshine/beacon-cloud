package com.chl.api.filter.impl;

import com.chl.api.clients.BeanCacheClient;
import com.chl.api.filter.CheckFilter;
import com.chl.common.constant.ApiConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.ApiException;
import com.chl.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

@Service(value = "ip")
@Slf4j
public class IPFilter implements CheckFilter {
    @Autowired
    private BeanCacheClient beanCacheClient;

    //ip字段名
    private static final String IP_ADDRESS = "ipAddress";

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验ip】IP校验中！！！！");
        //获取ip白名单
        List<String> ip = (List<String>) beanCacheClient.hGet(ApiConstants.CLIENT_BUSINESS + submit.getApikey(), IP_ADDRESS);
        submit.setIp(ip);//将白名单封装进submit对象

        //判断ip白名单是否为空或者真实ip是否再ip白名单内
        if (CollectionUtils.isEmpty(ip) || ip.contains(submit.getRealIp())){
            log.info("【接口模块-校验ip】客户端请求IP合法 ! ");
            //进行放行
            return;
        }

        //ip白名单不为空，真实ip不在ip白名单内
        log.info("【接口模块-校验ip】 请求的ip不在白名单内");
        throw new ApiException(ExceptionEnum.IP_NOT_WHITE);
    }
}
