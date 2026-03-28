package com.chl.strategy.filter;

import com.chl.common.constant.ApiConstants;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.client.BeaconCacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Component
public class StrategyFilterContext {
    /**
     * 泛型注入
     */
    @Autowired
    private Map<String,StrategyFilter> strategyFilterMap;

    /**
     * 缓存模块client
     */
    @Autowired
    private BeaconCacheClient beaconCacheClient;

    /**
     * 从缓存中取出存放具体客户所需要的校验所需的字段名
     */
    private final String CLIENT_FILTERS = "clientFilters";

    /**
     * 实现校验链
     */
    public void strategy(StandardSubmit submit){
        //1、通过缓存模块查询到校验链
        String filters = beaconCacheClient.hGet(ApiConstants.CLIENT_BUSINESS + submit.getApikey(), CLIENT_FILTERS);
        //2、分割并进行健壮性校验
        String[] filterArray = filters.split(",");
        if (filters != null && filterArray.length > 0){
            for (String strategy : filterArray) {
                //3、遍历分割完的数组进行具体校验
                StrategyFilter strategyFilter = strategyFilterMap.get(strategy);
                if (!ObjectUtils.isEmpty(strategyFilter)){
                    strategyFilter.strategy(submit);
                }
            }
        }
    }
}
