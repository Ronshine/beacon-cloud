package com.chl.web.config;

import com.chl.web.realm.ShiroRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //1、过滤器链
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
        //1、创建需要认证和放行资源
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put("/public/**","anon");
        filterChainDefinitionMap.put("/sys/user/login","anon");
        filterChainDefinitionMap.put("/index.html","anon");
        filterChainDefinitionMap.put("/login.html","anon");
        filterChainDefinitionMap.put("/logout","logout");
        filterChainDefinitionMap.put("/**","authc");

        //2、创建DefaultShiroFilterChainDefinition返回
        DefaultShiroFilterChainDefinition shiroFilter = new DefaultShiroFilterChainDefinition();
        shiroFilter.addPathDefinitions(filterChainDefinitionMap);

        return shiroFilter;
    }

    //2、安全管理器
    @Bean
    public DefaultWebSecurityManager securityManager(ShiroRealm realm){
        //1、创建securityManager 对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //2、设置自定义realm
        securityManager.setRealm(realm);

        //3、返回
        return securityManager;
    }

}
