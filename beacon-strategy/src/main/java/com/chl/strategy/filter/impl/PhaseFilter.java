package com.chl.strategy.filter.impl;

import com.chl.common.constant.ApiConstants;
import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.util.MobileOperatorUtil;
import com.chl.strategy.util.OperatorUtil;
import com.chl.strategy.client.BeaconCacheClient;
import com.chl.strategy.filter.StrategyFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "phase")
@Slf4j
public class PhaseFilter implements StrategyFilter {
    @Autowired
    private BeaconCacheClient beaconCacheClient;

    @Autowired
    private MobileOperatorUtil util;

    /**
     * 获取号码前7位起始以及结束
     */
    private final Integer MOBILE_START = 0;
    private final Integer MOBILE_END = 7;

    /**
     * 缓存消息中的分隔符
     */
    private final String SEPARATOR = ",";

    private final String UNKNOWN = "未知 未知,未知";

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-号段补齐】 补齐ing ~ ~");
        //1、根据电话号码前7位
        String mobile = submit.getMobile().substring(MOBILE_START, MOBILE_END);

        //2、获取归属地以及运营商
        String mobileInfo = beaconCacheClient.getString(ApiConstants.MOBILE_PREFIX + mobile);

        //3.如果获取的信息为空需要通过三方接口来进行查询
        getMobileInfo : if (StringUtils.isEmpty(mobileInfo)){
            mobileInfo = util.getMobileInfoBy360(mobile);
            //如果不为空向短信模块发送信息
            if (!StringUtils.isEmpty(mobileInfo)){
                rabbitTemplate.convertAndSend(RabbitMQConstants.MOBILE_AREA_OPERATOR,submit.getMobile());
                log.info("【策略模块-号段补齐】 向mq发送消息进行同步 mobileInfo = {}" , mobileInfo);
                break getMobileInfo; //给if命名如果完成跳出循环继续下面的封装操作
            }
            mobileInfo = UNKNOWN;
            log.info("【策略模块-号段补齐】 未知手机号请通知管理员");
        }

        //4、不论从redis或mysql获取到的信息最后都需要对数据进行封装
        String[] areaAndOperator = mobileInfo.split(SEPARATOR);
        if (areaAndOperator.length == 2){
            //封装归属地
            submit.setArea(areaAndOperator[0]);
            //封装运营商 (1-移动 2-联通 3-电信)
            submit.setOperatorId(OperatorUtil.getOperatorIdByOperatorName(areaAndOperator[1]));
            log.info("【策略模块-号段补齐】 信息封装成功！ ");
        }
    }
}
