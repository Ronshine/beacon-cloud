package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.ChannelMapper;
import com.chl.test.pojo.Channel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class ChannelMapperTest {
    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() throws JsonProcessingException {
        List<Channel> list = channelMapper.findAll();
        for(Channel channel :list){
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(objectMapper.writeValueAsString(channel), Map.class);
            cacheClient.hSet("channel:"+channel.getId(),map);
        }
    }
}