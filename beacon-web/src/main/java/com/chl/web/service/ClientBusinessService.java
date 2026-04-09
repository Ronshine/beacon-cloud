package com.chl.web.service;

import com.chl.web.entity.ClientBusiness;

import java.util.List;

public interface ClientBusinessService {
    /**
     * 查询所有客户信息
     */
    List<ClientBusiness> findAll();

    /**
     * 根据userId查询客户信息
     */
    List<ClientBusiness> findByUserId(Integer userId);
}
