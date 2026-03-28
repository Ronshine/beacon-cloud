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

import java.util.Map;
import java.util.Set;


@Service(value = "sign")
@Slf4j
public class SignFilter implements CheckFilter {
    @Autowired
    private BeanCacheClient beanCacheClient;

    /**
     * 签名前缀索引
     */
    private static final Integer SING_START_INDEX = 1;

    /**
     * 签名map集合消息
     */
    private static final String CLIENT_SIGN_INFO = "signInfo";

    private static final String SIGN_ID = "id";

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验签名】签名校验中！！！！");
        //获取sign签名
        String text = submit.getText();

        //1.判断签名是否携带【】
        if (!text.startsWith(ApiConstants.SIGN_PREFIX) || !text.contains(ApiConstants.SIGN_SUFFIX)){
            log.info("【接口模块-校验签名】无可用签名 text = {}",text);
            throw new ApiException(ExceptionEnum.ERROR_SIGN);
        }

        //2.截取【】中内容判断
        String sign = text.substring(SING_START_INDEX, text.indexOf(ApiConstants.SIGN_SUFFIX));
        if (StringUtils.isEmpty(sign)){
            log.info("【接口模块-校验签名】无可用签名 text = {}",text);
            throw new ApiException(ExceptionEnum.ERROR_SIGN);
        }

        //3. 从缓存中查询出客户绑定的签名
        Set<Map> set = beanCacheClient.sMembers(ApiConstants.CLIENT_SIGN + submit.getClientId());
        if (set != null && set.size() == 0){
            log.info("【接口模块-校验签名】 无可用签名 text = {}",text);
            throw new ApiException(ExceptionEnum.ERROR_SIGN);
        }

        //4.判断
        for (Map map : set) {
            if (sign.equals(map.get(CLIENT_SIGN_INFO))){
                //签名校验成功对模板校验所需信息进行封装
                submit.setSign(sign);
                submit.setSignId(Long.parseLong(map.get(SIGN_ID)+""));
                log.info("【接口模块-校验签名】   找到匹配的签名 sign = {}",sign);
                return;
            }
        }

        //5. 到这，说明没有匹配的签名
        log.info("【接口模块-校验签名】 无可用签名 text = {}",text);
        throw new ApiException(ExceptionEnum.ERROR_SIGN);
    }
}
