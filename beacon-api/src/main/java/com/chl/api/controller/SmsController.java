package com.chl.api.controller;

import com.chl.api.enums.SmsEnum;
import com.chl.api.filter.CheckFilterContext;
import com.chl.api.form.SingleSendForm;
import com.chl.api.utils.R;
import com.chl.api.vo.ResultVO;
import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.model.StandardSubmit;
import com.chl.common.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/sms")
@Slf4j
public class SmsController {
    @Autowired
    private SnowFlakeUtil snowFlakeUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CheckFilterContext checkFilterContext;
    /**
     * 不同代理对象发送的请求头名称集合需要用","隔开
     */
    @Value("${headers}")
    private String headers;

    /**
     * 基于请求头获取信息时，可能获取到的未知信息
     */
    private static final String UNKNOW = "unknow";

    /**
     * nginx代理配置的多层代理获取的ip集合
     */
    private static final String X_FORWARDED_FOR = "x-forwarded-for";

    @PostMapping(value = "/single_send",produces = "application/json;charset=utf-8")
    public ResultVO singleSend(@RequestBody @Validated SingleSendForm singleSendForm, BindingResult bindingResult,
                               HttpServletRequest req){
        if (bindingResult.hasErrors()){
            String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
                log.info("【接口模块-单条短信Controller】 参数不合法 msg = {}",msg);
                return R.error(SmsEnum.PARAMETER_ERROR.getCode(),msg);
        }
        //===============================获取真实ip====================================
        String realIp = this.getRealIp(req);
        //===============================封装缓存StandardSubmit对象==================================
        StandardSubmit submit = new StandardSubmit();
        submit.setRealIp(realIp);
        submit.setApikey(singleSendForm.getApikey());
        submit.setMobile(singleSendForm.getMobile());
        submit.setText(singleSendForm.getText());
        submit.setUid(singleSendForm.getUid());
        submit.setState(singleSendForm.getState());

        //对apikey进行校验
        checkFilterContext.check(submit);

        //===============对standardSubmit对象进行雪花算法id封装===============
        submit.setSequenceId(snowFlakeUtil.nextId());

        //======封装发送时间=======
        submit.setSendTime(LocalDateTime.now());

        //=======================向策略模块发送消息===================================
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PRE_SEND,submit,new CorrelationData(submit.getSequenceId().toString()));

        //返回响应结果
        return R.ok();
    }

    /**
     * 获取真实ip
     * @param req 发送过来的请求
     */
    private String getRealIp(HttpServletRequest req) {
        //返回的ip
        String ip;
        //对请求头集合进行分隔遍历
        for (String header : headers.split(",")) {
            //判断请求头是否为空
            if (!StringUtils.isEmpty(header)){
                ip = req.getHeader(header);
                //判断ip是否为空或是否是未知信息
                if (!StringUtils.isEmpty(ip) && !UNKNOW.equalsIgnoreCase(ip)){
                    //如果请求头是x_forwarded_for就对其进行,分割取第一个ip作为真实ip
                    if (X_FORWARDED_FOR.equalsIgnoreCase(header) && ip.indexOf(",") > 0){
                        ip = ip.substring(0,ip.indexOf(","));
                    }
                    //返回ip地址
                    return ip;
                }
            }
        }
        //如果上述方法都获取不到ip就使用最原始的方式获取ip
        return req.getRemoteAddr();
    }
}
