package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.ClientSignMapper;
import com.chl.test.pojo.ClientSign;
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
class ClientSignMapperTest {
    @Autowired
    private ClientSignMapper clientSignMapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findByClientId() {
        List<ClientSign> signList = clientSignMapper.findByClientId(1L);
        for (ClientSign clientSign : signList) {
            System.out.println(clientSign);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> list = signList.stream().map(cs -> {
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(cs), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        cacheClient.addSet("client_sign:1",list.toArray(new Map[]{}));
    }
}