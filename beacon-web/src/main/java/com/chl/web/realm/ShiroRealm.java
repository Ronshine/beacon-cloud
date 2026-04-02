package com.chl.web.realm;

import com.chl.web.entity.SmsUser;
import com.chl.web.service.SmsUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShiroRealm extends AuthorizingRealm {
    {
        //设置加密方式MD5以及加密次数
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("MD5");
        matcher.setHashIterations(1024);
        this.setCredentialsMatcher(matcher);
    }

    @Autowired
    private SmsUserService service;

    /**
     * 认证
     * @param token 传入的token
     * @return 返回认证对象
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1、获得用户名
        String username = (String) token.getPrincipal();

        //2、获取数据库查询对象
        SmsUser smsUser = service.findUserByUsername(username);

        //判断是否用户名正确如果不正确就直接返回null
        if (username == null){
            return null;
        }

        //3、创建返回的信息对象
        return new SimpleAuthenticationInfo(smsUser,smsUser.getPassword(), ByteSource.Util.bytes(smsUser.getSalt()),"shiroRealm");
    }

    /**
     * 授权
     * @param principalCollection 路径集合
     * @return 返回授权对象
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


}
