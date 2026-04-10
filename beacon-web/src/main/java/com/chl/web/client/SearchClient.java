package com.chl.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "beacon-search")
public interface SearchClient {
    @PostMapping("/search/findSmsByParameters")
    Map<String,Object> findSmsByParameters(@RequestBody Map<String,Object> map);
}
