package com.lorettax.login;

import java.io.Serializable;

public class UsageInfoLog implements Serializable {
    //发生的时间
    private Long createdAtMs;
    //0,app端 1，pc端 2，微信小程序......
    private String logtype;
    //应用唯一标识,包含所有终端的应用的统称，比如pc端网站和小程序对用的应用id和app端是一样的
    private String appId;
    //设备id
    private String deviceId;
    //版本号
    private String appVersion;
    //渠道
    private String appChannel;
    //平台
    private String appPlatform;
    //单次使用时长，单位是秒
    private Long singleUseDurationSecs;

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

    public Long getSingleUseDurationSecs() {
        return singleUseDurationSecs;
    }

    public void setSingleUseDurationSecs(Long singleUseDurationSecs) {
        this.singleUseDurationSecs = singleUseDurationSecs;
    }

    @Override
    public String toString() {
        return "UsageInfoLog{" +
                "createdAtMs=" + createdAtMs +
                ", logtype='" + logtype + '\'' +
                ", appId='" + appId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appChannel='" + appChannel + '\'' +
                ", appPlatform='" + appPlatform + '\'' +
                ", singleUseDurationSecs=" + singleUseDurationSecs +
                '}';
    }
}
