package com.chl.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class StandardReport implements Serializable {
    private static final long serialVersionUID = 3579610180145991163L;

    /**
     * 针对当前短信的唯一标识
     */
    private Long sequenceId;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 客户业务内的uid
     */
    private String uid;

    /**
     * 目标手机号
     */
    private String mobile;

    /**
     * 短信的发送时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;

    /**
     * 短信的发送状态， 0-等待ing，1-成功，2-失败
     */
    private int reportState;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 是否回调状态码
     */
    private Integer isCallback;

    /**
     * 回调发送地址url
     */
    private String callbackUrl;

    /**
     * 推送模块重试次数
     */
    private int resendCount = 0;

    /**
     * 服务方提供的用户唯一标识
     */
    private String apikey;

    /**
     * 搜索模块判断是否是第一次进行推送默认值是false
     */
    private boolean isReSearch;

    public int getResendCount() {
        return resendCount;
    }

    public void setResendCount(int resendCount) {
        this.resendCount = resendCount;
    }

    public StandardReport() {
    }

    public StandardReport(Long sequenceId, Long clientId, String uid, String mobile, LocalDateTime sendTime, int reportState, String errorMsg, Integer isCallback, String callbackUrl, int resendCount, String apikey, boolean isReSearch) {
        this.sequenceId = sequenceId;
        this.clientId = clientId;
        this.uid = uid;
        this.mobile = mobile;
        this.sendTime = sendTime;
        this.reportState = reportState;
        this.errorMsg = errorMsg;
        this.isCallback = isCallback;
        this.callbackUrl = callbackUrl;
        this.resendCount = resendCount;
        this.apikey = apikey;
        this.isReSearch = isReSearch;
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

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public int getReportState() {
        return reportState;
    }

    public void setReportState(int reportState) {
        this.reportState = reportState;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getIsCallback() {
        return isCallback;
    }

    public void setIsCallback(Integer isCallback) {
        this.isCallback = isCallback;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public boolean isReSearch() {
        return isReSearch;
    }

    public void setReSearch(boolean reSearch) {
        isReSearch = reSearch;
    }

    @Override
    public String toString() {
        return "StandardReport{" +
                "sequenceId=" + sequenceId +
                ", clientId=" + clientId +
                ", uid='" + uid + '\'' +
                ", mobile='" + mobile + '\'' +
                ", sendTime=" + sendTime +
                ", reportState=" + reportState +
                ", errorMsg='" + errorMsg + '\'' +
                ", isCallback=" + isCallback +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", resendCount=" + resendCount +
                ", apikey='" + apikey + '\'' +
                ", isReSearch=" + isReSearch +
                '}';
    }
}
