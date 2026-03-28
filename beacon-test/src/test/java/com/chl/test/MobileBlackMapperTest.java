package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.MobileBlackMapper;
import com.chl.test.pojo.MobileArea;
import com.chl.test.pojo.MobileBlack;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class MobileBlackMapperTest {
    @Autowired
    private MobileBlackMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() {
        List<MobileBlack> mobileBlacks = mapper.findAll();
        for (MobileBlack mobileBlack : mobileBlacks) {
            //如果获取clientId说明是平台黑名单
            if (mobileBlack.getClientId().equals(0)){
                cacheClient.set("black:"+mobileBlack.getBlackNumber(),"1");
            }else {
                cacheClient.set("black:"+mobileBlack.getClientId() + ":" +mobileBlack.getBlackNumber(),"1");
            }
        }
    }
}