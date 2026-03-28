package com.chl.common.enums;

public enum CMPPDeliverEnums {

    DELIVRD("DELIVRD", "Message is delivered to destination"),
    EXPIRED("EXPIRED", "Message validity period has expired"),
    DELETED("DELETED", "Message has been deleted."),
    UNDELIV("UNDELIV", "Message is undeliverable"),
    ACCEPTD("ACCEPTD", "Message is in accepted state"),
    UNKNOWN("UNKNOWN", "Message is in invalid state"),
    REJECTD("REJECTD", "Message is in a rejected state"),
    ;

    private final String stat;

    private final String description;

    CMPPDeliverEnums(String stat, String description) {
        this.stat = stat;
        this.description = description;
    }

    public String getStat() {
        return stat;
    }

    public String getDescription() {
        return description;
    }
}
