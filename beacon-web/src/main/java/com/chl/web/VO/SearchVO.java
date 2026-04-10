package com.chl.web.VO;

public class SearchVO {
    private String corpname;
    private String sendTimeStr;
    private Integer reportState;
    private Integer operatorId;
    private String errorMsg;
    private String srcNumber;
    private String mobile;
    private String text;

    public SearchVO() {
    }

    public SearchVO(String corpname, String sendTimeStr, Integer reportState, Integer operatorId, String errorMsg, String srcNumber, String mobile, String text) {
        this.corpname = corpname;
        this.sendTimeStr = sendTimeStr;
        this.reportState = reportState;
        this.operatorId = operatorId;
        this.errorMsg = errorMsg;
        this.srcNumber = srcNumber;
        this.mobile = mobile;
        this.text = text;
    }

    public String getCorpname() {
        return corpname;
    }

    public void setCorpname(String corpname) {
        this.corpname = corpname;
    }

    public String getSendTimeStr() {
        return sendTimeStr;
    }

    public void setSendTimeStr(String sendTimeStr) {
        this.sendTimeStr = sendTimeStr;
    }

    public Integer getReportState() {
        return reportState;
    }

    public void setReportState(Integer reportState) {
        this.reportState = reportState;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSrcNumber() {
        return srcNumber;
    }

    public void setSrcNumber(String srcNumber) {
        this.srcNumber = srcNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SearchVO{" +
                "corpname='" + corpname + '\'' +
                ", sendTimeStr=" + sendTimeStr +
                ", reportState=" + reportState +
                ", operatorId=" + operatorId +
                ", errorMsg='" + errorMsg + '\'' +
                ", srcNumber='" + srcNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
