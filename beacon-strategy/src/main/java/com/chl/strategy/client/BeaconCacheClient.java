package com.chl.strategy.client;

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

    @PostMapping("cache/get/{key}")
    String getString(@PathVariable(value = "key")String key);

    @PostMapping("/cache/sinterstr/{key}/{otherKey}")
    Set<Object> sInterStr(@PathVariable(value = "key")String key, @PathVariable(value = "otherKey") String otherKey, @RequestBody String ... value);

    @GetMapping("/cache/sMembers/{key}")
    Set<String> sMembers(@PathVariable(value = "key")String key);

    @PostMapping("cache/get/{key}")
    Integer getInteger(@PathVariable(value = "key")String key);

    /**
     * 向zSet结构中添加数据
     */
    @PostMapping("/cache/zAdd/{key}/{score}/{member}")
    boolean zAdd(@PathVariable(value = "key")String key,
                @PathVariable(value = "score")Long score,
                @PathVariable(value = "member")Long member);

    /**
     * 通过zAddRange方式查询区间有多少条数据（范围查询）
     */
    @GetMapping("/cache/zAddRange/{key}/{start}/{end}")
    int zAddRange(@PathVariable(value = "key")String key,
                 @PathVariable(value = "start")Long start,
                 @PathVariable(value = "end")Long end);

    /**
     * 对错误发送的消息进行删除
     */
    @DeleteMapping("/cache/zRemove/{key}/{member}")
    void zRemove(@PathVariable(value = "key")String key,@PathVariable(value = "member")Long member);

    /**
     * 对hash结构中进行数据更改
     * @param key 数据id
     * @param field 数据键
     * @param delta 修改数值
     * @return 最终结果
     */
    @PostMapping("/cache/hIncrBy/{key}/{field}/{delta}")
    Long hIncrBy(@PathVariable(value = "key")String key,
                        @PathVariable(value = "field")String field,
                        @PathVariable(value = "delta")Long delta);

    @GetMapping("/cache/sMemberMap/{key}")
    Set<Map> sMemberMap(@PathVariable(value = "key")String key);

    @GetMapping("/cache/map/{key}")
    Map<String,Object> getMapByKey(@PathVariable(value = "key")String key);

}
