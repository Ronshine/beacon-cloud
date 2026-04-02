package com.chl.web.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha kaptcha(){
        //1、默认验证码生成
        DefaultKaptcha kaptcha = new DefaultKaptcha();

        //2、properties信息
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,"4");

        //3、config对象配置
        Config config = new Config(properties);

        //4、将config对象设置进kaptcha
        kaptcha.setConfig(config);

        //5、返回对象
        return kaptcha;
    }
}
