package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.ClientBusinessMapper;
import com.chl.test.pojo.ClientBusiness;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class ClientBusinessMapperTest {
    @Autowired
    private ClientBusinessMapper clientBusinessMapper;

    @Autowired
    private CacheClient client;

    @Test
    void findById() throws JsonProcessingException {
        ClientBusiness cb = clientBusinessMapper.findById(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(cb), Map.class);
        client.hSet("client_business:"+cb.getApikey(),map);
    }
}