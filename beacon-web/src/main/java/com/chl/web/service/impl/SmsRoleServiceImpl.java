package com.chl.web.service.impl;

import com.chl.web.mapper.SmsRoleMapper;
import com.chl.web.service.SmsRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class SmsRoleServiceImpl implements SmsRoleService {
    @Resource
    private SmsRoleMapper smsRoleMapper;

    @Override
    public Set<String> findRoleByUserId(Integer userId) {
        return smsRoleMapper.findRoleByUserId(userId);
    }
}
