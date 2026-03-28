package com.chl.api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "beacon-cache")
public interface BeanCacheClient {

    @GetMapping("/cache/map/{key}")
    Map getMapByKey(@PathVariable(value = "key")String key);

    @PostMapping("cache/hSet/{key}")
    void hSet(@PathVariable(value = "key")String key, @RequestBody Map<String,Object> map);

    @PostMapping("cache/set/{key}")
    void set(@PathVariable(value = "key")String key, @RequestParam(value = "value") Object value);

    @PostMapping("cache/addSet/{key}")
    void addSet(@PathVariable(value = "key")String key,@RequestBody Map<String,Object> ... maps);

    @GetMapping("/cache/hGet/{key}/{filed}")
    Object hGet(@PathVariable(value = "key")String key,@PathVariable(value = "filed")String filed);

    @GetMapping("/cache/hGet/{key}/{filed}")
    String hGetString(@PathVariable(value = "key")String key,@PathVariable(value = "filed")String filed);

    @GetMapping("/cache/sMembers/{key}")
    Set<Map> sMembers(@PathVariable(value = "key")String key);

}
