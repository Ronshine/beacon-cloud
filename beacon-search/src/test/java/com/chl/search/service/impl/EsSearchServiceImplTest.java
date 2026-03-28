package com.chl.search.service.impl;

import com.chl.search.service.EsSearchService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class EsSearchServiceImplTest {
    @Autowired
    private EsSearchService esSearchService;

    @Test
    void index() throws IOException {
        esSearchService.index("sms_submit_log_2026","2","{\"clientId\":2}");
    }
}