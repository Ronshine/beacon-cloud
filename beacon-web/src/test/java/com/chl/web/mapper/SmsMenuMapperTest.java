package com.chl.web.mapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SmsMenuMapperTest {

    @Resource
    private SmsMenuMapper smsMenuMapper;

    @Test
    void findMenuByUserId() {
        List<Map<String, Object>> menuList = smsMenuMapper.findMenuByUserId(1);
        System.out.println(menuList);
    }
}