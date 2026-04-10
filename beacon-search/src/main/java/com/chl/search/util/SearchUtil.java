package com.chl.search.util;

import com.chl.common.model.StandardReport;

import java.time.LocalDateTime;

public class SearchUtil {
    public static final String INDEX = "sms_submit_log_";

    public static String getYear(){
        return LocalDateTime.now().getYear() + "";
    }

    public static String getCurrentYearIndex(){
        return INDEX + getYear();
    }

    private static ThreadLocal<StandardReport> standardReportThreadLocal = new ThreadLocal<>();

    /**
     * 往当前线程中存入report对象
     */
    public static void set(StandardReport report){
        standardReportThreadLocal.set(report);
    }

    /**
     * 从当前线程中获取report对象
     */
    public static StandardReport get(){
        return standardReportThreadLocal.get();
    }

    /**
     * 从当前线程中删除report对象
     */
    public static void remove(){
        standardReportThreadLocal.remove();
    }
}
