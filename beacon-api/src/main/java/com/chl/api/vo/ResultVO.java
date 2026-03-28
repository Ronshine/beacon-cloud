package com.chl.api.vo;

public class ResultVO {
    /** 状态码 0-成功 其他代表出错 */
    private Integer code;
    /** 响应信息 */
    private String msg;
    /** 短信计费条数 */
    private Integer count;
    /** 费用 */
    private Long fee;
    /** 用户携带的id */
    private String uid;
    /** 短信的id */
    private Long sid;

    public ResultVO() {
    }

    public ResultVO(Integer code, String msg, Integer count, Long fee, String uid, Long sid) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.fee = fee;
        this.uid = uid;
        this.sid = sid;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }
}
