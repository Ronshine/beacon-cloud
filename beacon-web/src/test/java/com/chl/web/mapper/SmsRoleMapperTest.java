package com.chl.web.mapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SmsRoleMapperTest {
    @Resource
    private SmsRoleMapper smsRoleMapper;

    @Test
    void findRoleByUserId() {
        Set<String> set = smsRoleMapper.findRoleByUserId(1);
        System.out.println(set);
    }
}