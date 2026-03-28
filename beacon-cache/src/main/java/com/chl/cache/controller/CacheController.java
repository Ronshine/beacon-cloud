package com.chl.cache.controller;

import com.msb.framework.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class CacheController {
    @Autowired
    private RedisClient redisClient;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * hash结构map存储数据
     */
    @PostMapping("/cache/hSet/{key}")
    public void hSet(@PathVariable(value = "key")String key, @RequestBody Map<String,Object> map){
        log.info("【缓存模块】hSet方法调用存储key:{},存储value{}",key, map);
        redisClient.hSet(key,map);
    }

    /**
     * 普通set存储数据
     */
    @PostMapping("/cache/set/{key}")
    public void set(@PathVariable(value = "key")String key, @RequestParam(value = "value") Object value){
        log.info("【缓存模块】Set方法调用存储key:{},存储value{}",key,value);
        redisClient.set(key,value);
    }

    /**
     * 采用set结构存储数据多个map参数
     */
    @PostMapping("/cache/addSet/{key}")
    public void addSet(@PathVariable(value = "key")String key,@RequestBody Map<String,Object> ... maps){
        log.info("【缓存模块】addSet方法调用,存储key:{},存储value:{}",key,maps);
        redisClient.sAdd(key,maps);
    }

    /**
     * 采用set结构存储数据多个string参数
     */
    @PostMapping("/cache/addStr/{key}")
    public void addStr(@PathVariable(value = "key")String key,@RequestBody String ... value){
        log.info("【缓存模块】addStr方法调用,存储key:{},存储value:{}",key,value);
        redisClient.sAdd(key,value);
    }

    /**
     * 通过redis交集来对传入的词汇来判断是否有敏感词
     */
    @PostMapping("/cache/sinterstr/{key}/{otherKey}")
    public Set<Object> sInterStr(@PathVariable(value = "key")String key,@PathVariable(value = "otherKey") String otherKey,@RequestBody String ... value){
        log.info("【缓存模块】sinterstr方法调用,查询key:{},存放敏感词key:{},查询数据value:{}",key,otherKey,value);
        //1、先进行需要进行交集数据的存储
        redisClient.sAdd(key,value);
        //2、进行查询数据
        Set<Object> result = redisClient.sIntersect(key, otherKey);
        //3、对刚才查询存储的数据进行删除
        redisClient.delete(key);
        return result;
    }

    @GetMapping("/cache/map/{key}")
    public Map<String,Object> getMapByKey(@PathVariable(value = "key")String key){
        log.info("【缓存模块】hGetAll方法,获取key:{}的数据",key);
        Map<String, Object> value = redisClient.hGetAll(key);
        log.info("【缓存模块】hGetAll方法,获取key:{},数据value:{}",key,value);
        return value;
    }

    @GetMapping("/cache/hGet/{key}/{filed}")
    public Object hGet(@PathVariable(value = "key")String key,@PathVariable(value = "filed")String filed){
        log.info("【缓存模块】hGet方法,获取key:{} filed:{}的数据",key,filed);
        Object value = redisClient.hGet(key, filed);
        log.info("【缓存模块】hGet方法,获取key:{},filed:{}数据value:{}",key,filed,value);
        return value;
    }

    @GetMapping("/cache/sMembers/{key}")
    public Set sMembers(@PathVariable(value = "key")String key){
        log.info("【缓存模块】sMembers方法,获取key:{}的数据",key);
        Set value = redisClient.sMembers(key);
        log.info("【缓存模块】sMembers方法,获取key:{}数据value:{}",key,value);
        return value;
    }

    @GetMapping("/cache/sMemberMap/{key}")
    public Set<Map> sMemberMap(@PathVariable(value = "key")String key){
        log.info("【缓存模块】sMembers方法,获取key:{}的数据",key);
        Set<Map> value = redisClient.sMembers(key);
        log.info("【缓存模块】sMembers方法,获取key:{}数据value:{}",key,value);
        return value;
    }

    @PostMapping("/cache/pipelined/string")
    public void pipelinedString(@RequestBody Map<String,String> map){
        log.info("【缓存模块】pipelined方法, map = {}", map.size());
        redisClient.pipelined(operations -> {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                operations.opsForValue().set(entry.getKey(),entry.getValue());
            }
        });
    }

    /**
     * 普通get获取数据
     */
    @PostMapping("/cache/get/{key}")
    public Object get(@PathVariable(value = "key")String key){
        log.info("【缓存模块】get方法调用存储key:{}",key);
        Object value = redisClient.get(key);
        log.info("【缓存模块】get方法调用 key = {},获取value = {}",key,value);
        return value;
    }

    /**
     * 向zSet结构中添加数据
     */
    @PostMapping("/cache/zAdd/{key}/{score}/{member}")
    public boolean zAdd(@PathVariable(value = "key")String key,
                     @PathVariable(value = "score")Long score,
                     @PathVariable(value = "member")Long member){
        log.info("【缓存模块】zAdd方法调用,存储key:{},存储score:{},存储member:{}",key,score,member);
        boolean result = redisClient.zAdd(key, score, member);
        return result;
    }

    /**
     * 通过zAddRange方式查询区间有多少条数据（范围查询）
     */
    @GetMapping("/cache/zAddRange/{key}/{start}/{end}")
    public int zAddRange(@PathVariable(value = "key")String key,
                        @PathVariable(value = "start")Long start,
                        @PathVariable(value = "end")Long end){
        log.info("【缓存模块】zAddRange方法调用,查询key:{},score:{},member:{}",key,start,end);
        Set values = redisTemplate.opsForZSet().rangeByScoreWithScores(key, Double.parseDouble(start + ""), Double.parseDouble(end + ""));

        if (values != null){
            return values.size();
        }
        return 0;
    }

    /**
     * 对错误发送的消息进行删除
     */
    @DeleteMapping("/cache/zRemove/{key}/{member}")
    public void zRemove(@PathVariable(value = "key")String key,@PathVariable(value = "member")Long member){
        log.info("【缓存模块】zRemove方法调用,删除key:{},member:{}",key,member);
        redisClient.zRemove(key, member);
    }

    /**
     * 对hash结构中进行数据更改
     * @param key 数据id
     * @param field 数据键
     * @param delta 修改数值
     * @return 最终结果
     */
    @PostMapping("/cache/hIncrBy/{key}/{field}/{delta}")
    public Long hIncrBy(@PathVariable(value = "key")String key,
                        @PathVariable(value = "field")String field,
                        @PathVariable(value = "delta")Long delta){
        log.info("【缓存模块】hIncrBy方法调用,增加key:{},field:{},delta:{}",key,field,delta);
        Long result = redisClient.hIncrementBy(key, field, delta);
        log.info("【缓存模块】hIncrBy方法调用结束,增加key:{},field:{},delta:{},返回result:{}",key,field,delta,result);
        return result;
    }

    @PostMapping("/cache/keys/{pattern}")
    public Set<String> keys(@PathVariable String pattern){
        log.info("【缓存模块】 keys方法调用 pattern : {}",pattern);
        Set<String> keys = redisTemplate.keys(pattern);
        log.info("【缓存模块】 keys方法调用结束 pattern:{},keys:{}",pattern,keys);
        return keys;
    }
}
