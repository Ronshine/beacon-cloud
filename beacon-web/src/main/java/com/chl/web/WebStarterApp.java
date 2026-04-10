package com.chl.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan(basePackages = "com.chl.web.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class WebStarterApp {
    public static void main(String[] args) {
        SpringApplication.run(WebStarterApp.class,args);
    }
}
