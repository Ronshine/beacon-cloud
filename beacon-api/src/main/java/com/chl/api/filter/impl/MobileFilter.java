package com.chl.api.filter.impl;

import com.chl.api.filter.CheckFilter;
import com.chl.api.utils.CheckPhoneUtil;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.ApiException;
import com.chl.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service(value = "mobile")
@Slf4j
public class MobileFilter implements CheckFilter {
    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验手机号】手机号合法性校验中！！！！");

        //获取传入的手机号
        String mobile = submit.getMobile();

        //判断是否为空以及是否为中国区手机号
        if (!StringUtils.isEmpty(mobile) && CheckPhoneUtil.isChinaPhone(mobile)){
            log.info("【接口模块-校验手机号】 手机号格式合法 mobile = {}",mobile);
            return;
        }

        //没通过证明手机号格式不合法
        log.info("【接口模块-校验手机号】手机号格式不正确 mobile = {}",mobile);
        throw new ApiException(ExceptionEnum.ERROR_MOBILE);
    }
}
