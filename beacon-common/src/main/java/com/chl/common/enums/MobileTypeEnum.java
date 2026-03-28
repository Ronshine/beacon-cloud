package com.chl.common.enums;

public enum MobileTypeEnum {

    CHINA_MOBILE(1,"移动"),
    CHINA_UNICOM(2,"联通"),
    CHINA_TELECOM(3,"电信"),
    UNKNOWN(0,"未知");

    private final Integer operatorId;

    private final String operatorName;

    MobileTypeEnum(Integer operatorId, String operatorName) {
        this.operatorId = operatorId;
        this.operatorName = operatorName;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }
}
