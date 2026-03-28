package com.chl.api.filter;

import com.chl.common.model.StandardSubmit;

import java.util.Objects;

public interface CheckFilter {

    /**
     * 接口信息校验
     */
    void check(StandardSubmit submit);

}
