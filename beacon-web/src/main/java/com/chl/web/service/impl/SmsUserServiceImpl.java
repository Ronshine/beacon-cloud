package com.chl.web.service.impl;

import com.chl.web.entity.SmsUser;
import com.chl.web.entity.SmsUserExample;
import com.chl.web.mapper.SmsUserMapper;
import com.chl.web.service.SmsUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SmsUserServiceImpl implements SmsUserService {

    @Resource
    private SmsUserMapper smsUserMapper;


    @Override
    public SmsUser findUserByUsername(String username) {
        //1、封装查询数据
        SmsUserExample smsUserExample = new SmsUserExample();
        smsUserExample.createCriteria().andUsernameEqualTo(username);

        //2、通过mapper查询
        List<SmsUser> smsUsers = smsUserMapper.selectByExample(smsUserExample);

        //返回查询结果
        return smsUsers != null ? smsUsers.get(0):null;
    }
}
