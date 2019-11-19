package com.lorettax.analy;

/**
 * Created by li on 2018/12/31.
 */
public class UserFrequencyResultInfo {
    private String appid;
    private String appplatform;
    private String appversion;
    private String appchannel;
    private String timeValue;
    private String number;
    private String devicenums;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppplatform() {
        return appplatform;
    }

    public void setAppplatform(String appplatform) {
        this.appplatform = appplatform;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getAppchannel() {
        return appchannel;
    }

    public void setAppchannel(String appchannel) {
        this.appchannel = appchannel;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDevicenums() {
        return devicenums;
    }

    public void setDevicenums(String devicenums) {
        this.devicenums = devicenums;
    }

    @Override
    public String toString() {
        return "UserFrequencyResultInfo{" +
                "appid='" + appid + '\'' +
                ", appplatform='" + appplatform + '\'' +
                ", appversion='" + appversion + '\'' +
                ", appchannel='" + appchannel + '\'' +
                ", timeValue='" + timeValue + '\'' +
                ", number='" + number + '\'' +
                ", devicenums='" + devicenums + '\'' +
                '}';
    }
}
