package com.lorettax.analy;

import java.io.Serializable;

/**
 * Created by li on 2018/12/16.
 */
public class ErrorInfo implements Serializable{
    private String timevalue;//时间，以天为单位
    private String appId;//应用唯一标识
    private String appVersion;//版本
    private String appChannel;//渠道
    private String appPlatform;//平台
    private String osType;//操作系统
    private String deviceStyle;//机型
    private String errorid;//错误唯一标示
    private Long cnts;//错误次数
    private String logtype;//0,app端 1，pc端 2，微信小程序......
    private String fieldgroup;
    public String getTimevalue() {
        return timevalue;
    }

    public void setTimevalue(String timevalue) {
        this.timevalue = timevalue;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getDeviceStyle() {
        return deviceStyle;
    }

    public void setDeviceStyle(String deviceStyle) {
        this.deviceStyle = deviceStyle;
    }


    public String getErrorid() {
        return errorid;
    }

    public void setErrorid(String errorid) {
        this.errorid = errorid;
    }

    public Long getCnts() {
        return cnts;
    }

    public void setCnts(Long cnts) {
        this.cnts = cnts;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "timevalue='" + timevalue + '\'' +
                ", appId='" + appId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appChannel='" + appChannel + '\'' +
                ", appPlatform='" + appPlatform + '\'' +
                ", osType='" + osType + '\'' +
                ", deviceStyle='" + deviceStyle + '\'' +
                ", errorid='" + errorid + '\'' +
                ", cnts=" + cnts +
                '}';
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }

    public String getFieldgroup() {
        return fieldgroup;
    }

    public void setFieldgroup(String fieldgroup) {
        this.fieldgroup = fieldgroup;
    }
}
