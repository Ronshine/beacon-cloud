package com.chl.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CacheStartApp {
    public static void main(String[] args) {
        SpringApplication.run(CacheStartApp.class,args);
    }
}
