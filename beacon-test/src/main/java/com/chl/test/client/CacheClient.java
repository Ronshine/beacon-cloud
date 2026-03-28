package com.chl.test.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "beacon-cache")
public interface CacheClient {

    @PostMapping("cache/hSet/{key}")
    void hSet(@PathVariable(value = "key")String key, @RequestBody Map<String,Object> map);

    @PostMapping("cache/set/{key}")
    void set(@PathVariable(value = "key")String key, @RequestParam(value = "value") Object value);

    @PostMapping("cache/addSet/{key}")
    void addSet(@PathVariable(value = "key")String key,@RequestBody Map<String,Object> ... maps);

    @PostMapping("/cache/pipelined/string")
    void pipelinedString(@RequestBody Map<String,String> map);

    @PostMapping("/cache/addStr/{key}")
    void addStr(@PathVariable(value = "key")String key,@RequestBody String ... value);
}
