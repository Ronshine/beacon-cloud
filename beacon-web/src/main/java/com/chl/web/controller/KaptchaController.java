package com.chl.web.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码配置类
 */
@Controller
public class KaptchaController {
    //常量
    private final String JPG = "jpg";

    @Autowired
    private DefaultKaptcha kaptcha;

    @GetMapping("/kaptcha.jpg")
    public void kaptcha(HttpServletResponse resp){
        //1、验证码图片不需要做存储和缓存
        resp.setHeader("Cache-Control","no-store, no-cache");

        //2、设置响应头信息
        resp.setContentType("image/jpg");

        //3、生成验证码文字
        String text = kaptcha.createText();

        //4、基于文字生成对应的图片
        BufferedImage image = kaptcha.createImage(text);

        //5、写回验证码图片信息
        try {
            ServletOutputStream outputStream = resp.getOutputStream();
            ImageIO.write(image,JPG,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
