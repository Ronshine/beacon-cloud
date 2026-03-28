package com.chl.monitor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "beacon-cache")
public interface CacheClient {

    @PostMapping("/cache/keys/{pattern}")
    Set<String> keys(@PathVariable String pattern);

    @GetMapping("/cache/map/{key}")
    Map<String,Object> getMapByKey(@PathVariable(value = "key")String key);

}
