package com.chl.strategy.filter;

import com.chl.common.model.StandardSubmit;

public interface StrategyFilter {

    /**
     * 实现策略模式校验链
     */
    void strategy(StandardSubmit submit);

}
