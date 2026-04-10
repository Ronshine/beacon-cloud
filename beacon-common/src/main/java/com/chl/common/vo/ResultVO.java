package com.chl.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultVO {
    private Integer code;

    private String msg;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Object data;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Long total;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Object rows;

    public ResultVO() {
    }

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(Integer code, String msg, Object data, Long total, Object rows) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.total = total;
        this.rows = rows;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", total=" + total +
                ", rows=" + rows +
                '}';
    }
}
