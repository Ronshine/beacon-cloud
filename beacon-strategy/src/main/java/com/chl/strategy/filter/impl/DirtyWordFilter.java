package com.chl.strategy.filter.impl;

import com.chl.common.constant.ApiConstants;
import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.constant.SmsConstant;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.StrategyException;
import com.chl.common.model.StandardReport;
import com.chl.common.model.StandardSubmit;
import com.chl.strategy.client.BeaconCacheClient;
import com.chl.strategy.filter.StrategyFilter;
import com.chl.strategy.util.ErrorSendMsgUtil;
import com.chl.strategy.util.HutoolDFAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "dirtyword")
@Slf4j
public class DirtyWordFilter implements StrategyFilter {
    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-敏感词校验】校验ing ~ ~ ");

        //1、获取短信中文本消息
        String text = submit.getText();

        //2、对短信内容进行分词
        List<String> dirtyWords = HutoolDFAUtil.matchAll(text);

        //3、判断返回的set集合是否为空
        if (dirtyWords != null && dirtyWords.size() > 0){
            //5、如果为空存在敏感词进行处理
            log.info("【策略模块-敏感词校验】信息内存存在敏感词校验失败！");

            //===============发送写日志==================  全部将其封装为方法
            //1、封装错误信息
            submit.setErrorMsg(ExceptionEnum.HAVE_DIRTY_WORD.getMsg() + "敏感词为：" + dirtyWords);
            sendMsgUtil.sendLog(submit);

            // ================发送状态报告的消息前，需要将report对象数据封装====================
            sendMsgUtil.sendPushReport(submit);

            //抛出异常
            throw new StrategyException(ExceptionEnum.HAVE_DIRTY_WORD);
        }

        log.info("【策略模块-敏感词校验】 敏感词校验通过！！！");
    }
}
