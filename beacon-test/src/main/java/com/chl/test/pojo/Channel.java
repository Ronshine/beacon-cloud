package com.chl.test.pojo;

import java.util.Date;
import java.io.Serializable;

/**
 * 通道表(Channel)实体类
 *
 * @author makejava
 * @since 2026-03-14 14:23:42
 */
public class Channel implements Serializable {
    private static final long serialVersionUID = -80515204808577332L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 通道名称 如：北京移动、上海联通、深圳电信
     */
    private String channelName;
    /**
     * 通道类型：0-三网 1-移动 2-联通 3-电信
     */
    private Integer channelType;
    /**
     * 通道地区 如：北京 北京、湖北 荆门
     */
    private String channelArea;
    /**
     * 地区号段
     */
    private String channelAreaCode;
    /**
     * 通道短信成本价格（厘/条）
     */
    private Long channelPrice;
    /**
     * 通道协议类型 1-cmpp、2-sgip、3-smgp
     */
    private Integer channelProtocal;
    /**
     * 通道IP地址
     */
    private String channelIp;
    /**
     * 通道端口号
     */
    private Integer channelPort;
    /**
     * 通道账号
     */
    private String channelUsername;
    /**
     * 通道密码
     */
    private String channelPassword;
    /**
     * 账户接入号，如1069777、10684376
     */
    private String channelNumber;
    /**
     * 是否启动 0-已停用 1-启用中
     */
    private Integer isAvailable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public String getChannelArea() {
        return channelArea;
    }

    public void setChannelArea(String channelArea) {
        this.channelArea = channelArea;
    }

    public String getChannelAreaCode() {
        return channelAreaCode;
    }

    public void setChannelAreaCode(String channelAreaCode) {
        this.channelAreaCode = channelAreaCode;
    }

    public Long getChannelPrice() {
        return channelPrice;
    }

    public void setChannelPrice(Long channelPrice) {
        this.channelPrice = channelPrice;
    }

    public Integer getChannelProtocal() {
        return channelProtocal;
    }

    public void setChannelProtocal(Integer channelProtocal) {
        this.channelProtocal = channelProtocal;
    }

    public String getChannelIp() {
        return channelIp;
    }

    public void setChannelIp(String channelIp) {
        this.channelIp = channelIp;
    }

    public Integer getChannelPort() {
        return channelPort;
    }

    public void setChannelPort(Integer channelPort) {
        this.channelPort = channelPort;
    }

    public String getChannelUsername() {
        return channelUsername;
    }

    public void setChannelUsername(String channelUsername) {
        this.channelUsername = channelUsername;
    }

    public String getChannelPassword() {
        return channelPassword;
    }

    public void setChannelPassword(String channelPassword) {
        this.channelPassword = channelPassword;
    }

    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }
}

