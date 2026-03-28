package com.chl.cache.controller;

import com.msb.framework.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TestController {
    @Autowired
    private RedisClient redisClient;

    @PostMapping("/test/set/{key}")
    public String set(@PathVariable String key, @RequestBody Map map){
        redisClient.hSet(key,map);
        return "ok";
    }

    @GetMapping("/test/get/{key}")
    public Map get(@PathVariable String key){
        return redisClient.hGetAll(key);
    }
}
