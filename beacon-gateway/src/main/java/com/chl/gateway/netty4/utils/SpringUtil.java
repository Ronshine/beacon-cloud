package com.chl.gateway.netty4.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {
    @Autowired
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static Object getBeanByName(String beanName){
        return SpringUtil.applicationContext.getBean(beanName);
    }

    public static <T> T getBeanByClass(Class<T> clazz){
        return SpringUtil.applicationContext.getBean(clazz);
    }
}
