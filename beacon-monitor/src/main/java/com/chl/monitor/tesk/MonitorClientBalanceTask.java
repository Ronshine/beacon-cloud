package com.chl.monitor.tesk;

import com.chl.monitor.client.CacheClient;
import com.chl.monitor.util.MailUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class MonitorClientBalanceTask {
    // 客户余额限制，小于500大洋，直接发送信息
    private final long balanceLimit = 500000;

    private final String CLIENT_BALANCE_PATTERN = "client_balance:*";

    private final String BALANCE = "balance";

    private final String EMAIL = "extend1";

    private final String text = "客户您好，你在【烽火短信平台】内的余额仅剩余%s元，请您及时补充金额，避免影响您的短信发送！！";

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private MailUtil mailUtil;

    @XxlJob("monitorClientBalanceTask")
    public void monitor() throws MessagingException {
        Set<String> keys = cacheClient.keys(CLIENT_BALANCE_PATTERN);

        for (String key : keys) {
            Map<String, Object> map = cacheClient.getMapByKey(key);
            Long balance = Long.parseLong(map.get(BALANCE) + "");
            String email = map.get(EMAIL) + "";

            if (balance < balanceLimit){
                log.info("【监控模块-客户余额】 邮件发送中-----");
                mailUtil.sendEmail(email,"【烽火短信平台】提醒您余额不足。",String.format(text,balance/1000));
                log.info("【监控模块-客户余额】 消息发送成功 ！！！");
            }
        }
    }
}
