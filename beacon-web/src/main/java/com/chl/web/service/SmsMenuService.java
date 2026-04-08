package com.chl.web.service;

import java.util.List;
import java.util.Map;

public interface SmsMenuService {

    List<Map<String,Object>> findMenuByUserId(Integer userId);
}
