package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.DirtyWordMapper;
import com.chl.test.pojo.MobileArea;
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
class DirtyWordMapperTest {
    @Autowired
    private DirtyWordMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() {
        List<String> dirtyWords = mapper.findDirtyWord();
        cacheClient.addStr("dirty_word",dirtyWords.toArray(new String[]{}));
    }
}