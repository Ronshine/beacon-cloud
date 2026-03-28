package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.ClientTemplateMapper;
import com.chl.test.pojo.ClientTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
class ClientTemplateMapperTest {
    @Autowired
    private ClientTemplateMapper clientTemplateMapper;
    @Autowired
    private CacheClient cacheClient;

    @Test
    void findBySignId() {
        List<ClientTemplate> list24 = clientTemplateMapper.findBySignId(24L);
        List<ClientTemplate> list15 = clientTemplateMapper.findBySignId(15L);

        //24签名没有模板
        for (ClientTemplate clientTemplate : list24) {
            System.out.println("24签名具有的模板:"+clientTemplate);
        }

        for (ClientTemplate clientTemplate : list15) {
            System.out.println("15签名具有的模板" + clientTemplate);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        List<Map> list = list15.stream().map(ct -> {
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(ct), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        cacheClient.addSet("client_template:15",list.toArray(new Map[]{}));
    }
}