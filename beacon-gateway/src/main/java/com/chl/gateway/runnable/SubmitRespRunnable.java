package com.chl.gateway.runnable;

import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.model.StandardReport;
import com.chl.common.model.StandardSubmit;
import com.chl.common.utils.CMPP2ResultUtil;
import com.chl.common.utils.CMPPDeliverMapUtil;
import com.chl.common.utils.CMPPSubmitRespUtil;
import com.chl.gateway.netty4.entity.CmppSubmitResp;
import com.chl.gateway.netty4.utils.SpringUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;

public class SubmitRespRunnable implements Runnable{
    private static final int OK = 0;

    private RabbitTemplate rabbitTemplate = SpringUtil.getBeanByClass(RabbitTemplate.class);

    private CmppSubmitResp submitResp;

    public SubmitRespRunnable(CmppSubmitResp submitResp) {
        this.submitResp = submitResp;
    }

    @Override
    public void run() {
        StandardReport report = null;
        //1、拿到自增ID，并且从ConcurrentHashMap中获取到存储的submit
        StandardSubmit submit = CMPPSubmitRespUtil.remove(submitResp.getSequenceId() + "");

        //2、根据运营商返回的result，确认短信状态并且封装submit
        int result = submitResp.getResult();
        if (result != OK){
            String resultMessage = CMPP2ResultUtil.getErrorMessageByResult(result);
            submit.setReportState(result);
            submit.setErrorMsg(resultMessage);
        }else {
            // 如果没进到if中，说明运营商已经正常的接收了发送短信的任务，这边完成3操作
            //3、将submit封装为Report，临时存储，以便运营商返回状态码时，可以再次获取到信息
            // 这里没有对其他信息做封装
            report = new StandardReport();
            BeanUtils.copyProperties(submit,report);
            CMPPDeliverMapUtil.put(submitResp.getMsgId() + "",report);
        }
        //4、将前三步操作全部扔到线程池中执行，并且在线程池中完成消息的发送
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_WRITE_LOG,submit);
    }
}
