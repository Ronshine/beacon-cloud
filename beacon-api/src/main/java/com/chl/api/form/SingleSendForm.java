package com.chl.api.form;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class SingleSendForm {

    /** 服务方提供的用户唯一标识 */
    @NotNull(message = "apikey不能为空")
    private String apikey;
    /** 手机号 */
    @NotNull(message = "手机号不能为空")
    private String mobile;
    /** 短信内容 */
    @NotNull(message = "短信内容不能为空")
    private String text;
    /** 业务系统中的id */
    private String uid;
    /** 0-验证码短信 1-通知类短信 2-营销类短信 */
    @Range(min = 0,max = 2)
    @NotNull(message = "短信类型不能为空")
    private Integer state;

    public SingleSendForm() {
    }

    public SingleSendForm(String apikey, String mobile, String text, String uid, Integer state) {
        this.apikey = apikey;
        this.mobile = mobile;
        this.text = text;
        this.uid = uid;
        this.state = state;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
