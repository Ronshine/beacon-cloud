package com.chl.web.service.impl;

import com.chl.web.entity.ClientBusiness;
import com.chl.web.entity.ClientBusinessExample;
import com.chl.web.mapper.ClientBusinessMapper;
import com.chl.web.service.ClientBusinessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClientBusinessServiceImpl implements ClientBusinessService {
    @Resource
    private ClientBusinessMapper clientBusinessMapper;

    /**
     * 查询所有客户信息
     */
    @Override
    public List<ClientBusiness> findAll() {
        return clientBusinessMapper.selectByExample(null);
    }

    /**
     * 根据userId查询客户信息
     * @param userId 用户id
     */
    @Override
    public List<ClientBusiness> findByUserId(Integer userId) {
        ClientBusinessExample example = new ClientBusinessExample();
        example.createCriteria().andExtend1EqualTo(userId + "");
        return clientBusinessMapper.selectByExample(example);
    }
}
