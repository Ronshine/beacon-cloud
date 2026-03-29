package com.chl.web.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
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

    /**
     * 认证
     * @param token 传入的token
     * @return 返回认证对象
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1、获得用户名
        String username = (String) token.getPrincipal();

        //判断是否用户名正确如果不正确就直接返回null
        if (username == null || !username.equals("admin")){
            return null;
        }

        //2、获取密码和盐 （模拟数据库操作）
        String salt = "09a8424ed5bf4373af6530fec2b29c0f";
        String password = "b39dc5da02d002e6ac581e5bb929d2e5";
        //3、创建返回的信息对象
        AuthenticationInfo info = new SimpleAuthenticationInfo(username,password, ByteSource.Util.bytes(salt),"shiroRealm");

        return info;
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
