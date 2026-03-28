package com.chl.test;

import com.chl.test.client.CacheClient;
import com.chl.test.mapper.MobileTransferMapper;
import com.chl.test.pojo.MobileBlack;
import com.chl.test.pojo.MobileTransfer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class MobileTransferMapperTest {
    @Autowired
    private MobileTransferMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    void findAll() {
        List<MobileTransfer> transferList = mapper.findAllMobileTransfer();
        for (MobileTransfer mobileTransfer : transferList) {
            cacheClient.set("transfer:" + mobileTransfer.getTransferNumber(),mobileTransfer.getNowIsp());
        }
    }
}