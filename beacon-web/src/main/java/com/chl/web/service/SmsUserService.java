package com.chl.web.service;

import com.chl.web.entity.SmsUser;

public interface SmsUserService {

    SmsUser findUserByUsername(String username);
}
