package com.chl.api.filter.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignFilterTest {
    @Test
    void check() {
        String text = "【123456】";
        String sign = text.substring(1, text.indexOf("】"));
        System.out.println(sign);
    }
}