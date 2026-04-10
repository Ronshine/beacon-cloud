package com.chl.search.controller;

import com.chl.search.service.EsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class SmsSearchController {
    @Autowired
    private EsSearchService esSearchService;

    @PostMapping("/search/findSmsByParameters")
    public Map<String,Object> findSmsByParameters(@RequestBody Map<String,Object> map) throws IOException {
        return esSearchService.findSmsByParameters(map);
    }

}
