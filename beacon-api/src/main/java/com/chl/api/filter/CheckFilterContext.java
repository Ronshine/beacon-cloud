package com.chl.api.filter;

import com.chl.common.model.StandardSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RefreshScope
public class CheckFilterContext {
    @Autowired
    private Map<String, CheckFilter> checkFilterMap;

    @Value("${filters:apikey,ip,sign}")
    private String filters;

    /**
     * 此check方法校验执行顺序
     */
    public void check(StandardSubmit submit){
        //将filters根据逗号分隔
        String[] filterArray = this.filters.split(",");

        //对数组进行遍历
        for (String filter : filterArray) {
            CheckFilter checkFilter = checkFilterMap.get(filter);
            checkFilter.check(submit);
        }
    }
}
