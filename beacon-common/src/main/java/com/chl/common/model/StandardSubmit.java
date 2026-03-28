package com.chl.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 在接口模块-策略模块-短信网关模块需要做校验和封装的POJO类对象
 */
public class StandardSubmit implements Serializable {
    private static final long serialVersionUID = -4450920467474725782L;

    /**
     * 针对当前短信的唯一标识
     */
    private Long sequenceId;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 客户端的ip白名单
     */
    private List<String> ip;

    /**
     * 客户业务内的uid
     */
    private String uid;

    /**
     * 目标手机号
     */
    private String mobile;

    /**
     * 短信内容的签名
     */
    private String sign;

    /**
     * 短信内容
     */
    private String text;

    /**
     * 短信的发送时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;

    /**
     * 当前短信的费用
     */
    private Long fee;

    /**
     * 目标手机号的运营商
     */
    private Integer operatorId;

    /**
     * 目标手机号的归属地区号  0451  0455
     */
    private Integer areaCode;

    /**
     * 目标手机号的归属地  哈尔滨，  绥化~
     */
    private String area;

    /**
     * 通道下发的源号码  106934985673485645
     */
    private String srcNumber;

    /**
     * 通道的id信息
     */
    private Long channelId;

    /**
     * 短信的发送状态， 0-等待ing，1-成功，2-失败
     */
    private int reportState;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 短信来源的真实ip
     */
    private String realIp;

    /**
     * 短信类型
     * 0-验证码短信 1-通知类短信 2-营销类短信
     */
    private Integer state;

    /**
     * 服务方提供的用户唯一标识
     */
    private String apikey;

    /**
     * 判断是否进行携号转网标识
     */
    private boolean isTransfer;

    /**
     * 签名id
     */
    private Long signId;

    /**
     * 一小时校验时间毫秒值
     */
    private Long oneHourLimit;

    public StandardSubmit() {
    }

    public StandardSubmit(Long sequenceId, Long clientId, List<String> ip, String uid, String mobile, String sign, String text, LocalDateTime sendTime, Long fee, Integer operatorId, Integer areaCode, String area, String srcNumber, Long channelId, int reportState, String errorMsg, String realIp, Integer state, String apikey, boolean isTransfer, Long signId,Long oneHourLimit) {
        this.sequenceId = sequenceId;
        this.clientId = clientId;
        this.ip = ip;
        this.uid = uid;
        this.mobile = mobile;
        this.sign = sign;
        this.text = text;
        this.sendTime = sendTime;
        this.fee = fee;
        this.operatorId = operatorId;
        this.areaCode = areaCode;
        this.area = area;
        this.srcNumber = srcNumber;
        this.channelId = channelId;
        this.reportState = reportState;
        this.errorMsg = errorMsg;
        this.realIp = realIp;
        this.state = state;
        this.apikey = apikey;
        this.isTransfer = isTransfer;
        this.signId = signId;
        this.oneHourLimit = oneHourLimit;
    }

    public boolean isTransfer() {
        return isTransfer;
    }

    public void setTransfer(boolean transfer) {
        isTransfer = transfer;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getSignId() {
        return signId;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }

    public String getRealIp() {
        return realIp;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public void setRealIp(String realIp) {
        this.realIp = realIp;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<String> getIp() {
        return ip;
    }

    public void setIp(List<String> ip) {
        this.ip = ip;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSrcNumber() {
        return srcNumber;
    }

    public void setSrcNumber(String srcNumber) {
        this.srcNumber = srcNumber;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public int getReportState() {
        return reportState;
    }

    public void setReportState(int reportState) {
        this.reportState = reportState;
    }

    public Long getOneHourLimit() {
        return oneHourLimit;
    }

    public void setOneHourLimit(Long oneHourLimit) {
        this.oneHourLimit = oneHourLimit;
    }

    @Override
    public String toString() {
        return "StandardSubmit{" +
                "sequenceId=" + sequenceId +
                ", clientId=" + clientId +
                ", ip='" + ip + '\'' +
                ", uid='" + uid + '\'' +
                ", mobile='" + mobile + '\'' +
                ", sign='" + sign + '\'' +
                ", text='" + text + '\'' +
                ", sendTime=" + sendTime +
                ", fee=" + fee +
                ", operatorId=" + operatorId +
                ", areaCode=" + areaCode +
                ", area='" + area + '\'' +
                ", srcNumber='" + srcNumber + '\'' +
                ", channelId=" + channelId +
                ", reportState=" + reportState +
                ", errorMsg='" + errorMsg + '\'' +
                ", realIp='" + realIp + '\'' +
                ", state=" + state +
                ", apikey='" + apikey + '\'' +
                ", isTransfer=" + isTransfer +
                ", signId=" + signId +
                ", oneHourLimit=" + oneHourLimit +
                '}';
    }
}
