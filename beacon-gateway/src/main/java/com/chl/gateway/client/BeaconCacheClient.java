package com.chl.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "beacon-cache")
public interface BeaconCacheClient {
    @GetMapping("/cache/hGet/{key}/{filed}")
    String hGet(@PathVariable(value = "key")String key, @PathVariable(value = "filed")String filed);

    @GetMapping("/cache/hGet/{key}/{filed}")
    Integer hGetInteger(@PathVariable(value = "key")String key, @PathVariable(value = "filed")String filed);
}
