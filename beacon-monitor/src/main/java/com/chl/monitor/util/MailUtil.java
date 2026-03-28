package com.chl.monitor.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Component
@RefreshScope
public class MailUtil {
    @Value("${spring.mail.username}")
    private String form;

    @Value("${spring.mail.tos}")
    private String tos;

    @Resource
    JavaMailSender javaMailSender;

    /**
     * 定时任务监听
     * @param subject 邮件标题
     * @param text 邮件文本内容
     */
    public void sendEmail(String subject,String text) throws MessagingException {
        //创建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //通过helper类进行赋值
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        //发件人
        helper.setFrom(form);
        //收件人
        helper.setTo(tos.split(","));
        //标题
        helper.setSubject(subject);
        //文本内容
        helper.setText(text,true);

        //发送短信
        javaMailSender.send(mimeMessage);
    }

    /**
     * 重载定时任务监听
     * @param email 客户邮箱
     * @param subject 邮件标题
     * @param text 邮件文本内容
     * @throws MessagingException
     */
    public void sendEmail(String email,String subject,String text) throws MessagingException {
        //创建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //通过helper类进行赋值
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        //发件人
        helper.setFrom(form);
        //收件人
        helper.setTo(email);
        //标题
        helper.setSubject(subject);
        //文本内容
        helper.setText(text,true);

        //发送短信
        javaMailSender.send(mimeMessage);
    }

}
