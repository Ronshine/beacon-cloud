package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.ClientChannelMapper;
import com.chl.test.pojo.Channel;
import com.chl.test.pojo.ClientChannel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ClientChannelMapperTest {
    @Autowired
    private ClientChannelMapper clientChannelMapper;
    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() throws JsonProcessingException {
        List<ClientChannel> list = clientChannelMapper.findAll();
        for (ClientChannel clientChannel : list){
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(objectMapper.writeValueAsString(clientChannel), Map.class);
            cacheClient.addSet("client_channel:" + clientChannel.getClientId(),map);
        }
    }
}