package com.lorettax.login;

import java.io.Serializable;

public class ErrorInfoLog implements Serializable {
    //发生的时间
    private Long createdAtMs;
    //0,app端 1，pc端 2，微信小程序......
    private String logtype;
    //引用唯一标识，包含所有终端的应用的统称，比如pc端网站和小程序对用的应用id和app端是一样的
    private String appId;
    //设备唯一标识,包含：app端的设备号，pc端mac地址,微信小程序微信唯一标示
    private String deviceId;
    //版本
    private String appVersion;
    //渠道
    private String appChannel;
    //平台
    private String appPlatform;
    //机型
    private String deviceStyle;
    //操作系统
    private String osType;

    //错误摘要
    private String errorBrief;
    //错误详情
    private String errorDetail;

    public Long getCreatedAtMs() {
        return createdAtMs;
    }

    public void setCreatedAtMs(Long createdAtMs) {
        this.createdAtMs = createdAtMs;
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public void setAppChannel(String appChannel) {
        this.appChannel = appChannel;
    }

    public String getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(String appPlatform) {
        this.appPlatform = appPlatform;
    }

    public String getDeviceStyle() {
        return deviceStyle;
    }

    public void setDeviceStyle(String deviceStyle) {
        this.deviceStyle = deviceStyle;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getErrorBrief() {
        return errorBrief;
    }

    public void setErrorBrief(String errorBrief) {
        this.errorBrief = errorBrief;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }


    @Override
    public String toString() {
        return "ErrorInfoLog{" +
                "createdAtMs=" + createdAtMs +
                ", logtype='" + logtype + '\'' +
                ", appId='" + appId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appChannel='" + appChannel + '\'' +
                ", appPlatform='" + appPlatform + '\'' +
                ", deviceStyle='" + deviceStyle + '\'' +
                ", osType='" + osType + '\'' +
                ", errorBrief='" + errorBrief + '\'' +
                ", errorDetail='" + errorDetail + '\'' +
                '}';
    }
}
