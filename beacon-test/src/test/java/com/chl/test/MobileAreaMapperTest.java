package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.MobileAreaMapper;
import com.chl.test.pojo.MobileArea;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class MobileAreaMapperTest {
    @Autowired
    private MobileAreaMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() {
        List<MobileArea> areaList = mapper.findAll();
        Map<String,String> map = new HashMap<>();
        for (MobileArea mobileArea : areaList) {
            map.put("phase:"+mobileArea.getMobileNumber(),mobileArea.getMobileArea()+","+mobileArea.getMobileType());
        }
        cacheClient.pipelinedString(map);
    }
}