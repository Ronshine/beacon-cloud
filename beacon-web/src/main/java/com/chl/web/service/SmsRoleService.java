package com.chl.web.service;

import java.util.Set;

public interface SmsRoleService {

    /**
     * 通过用户id查询用户所拥有的角色
     * @param userId 用户id
     * @return 返回用户所有的角色信息
     */
    Set<String> findRoleByUserId(Integer userId);
}
