package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.ClientBalanceMapper;
import com.chl.test.pojo.ClientBalance;
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
class ClientBalanceMapperTest {
    @Autowired
    private ClientBalanceMapper clientBalanceMapper;
    @Autowired
    private CacheClient cacheClient;

    @Test
    void findByClientId() throws JsonProcessingException {
        ClientBalance balance = clientBalanceMapper.findByClientId(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(balance), Map.class);

        cacheClient.hSet("client_balance:1",map);
    }
}