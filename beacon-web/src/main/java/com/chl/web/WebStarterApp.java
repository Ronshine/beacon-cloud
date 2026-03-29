package com.chl.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.chl.web.mapper")
public class WebStarterApp {
    public static void main(String[] args) {
        SpringApplication.run(WebStarterApp.class,args);
    }
}
