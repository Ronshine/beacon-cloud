package com.chl.web.service.impl;

import com.chl.web.entity.SmsUser;
import com.chl.web.service.SmsUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SmsUserServiceImplTest {

    @Autowired
    private SmsUserService service;

    @Test
    void findUserByUsername() {
        SmsUser admin = service.findUserByUsername("admin");
        System.out.println(admin);
    }
}