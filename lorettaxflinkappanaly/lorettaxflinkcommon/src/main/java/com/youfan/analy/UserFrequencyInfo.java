package com.lorettax.analy;

/**
 * Created by li on 2018/12/31.
 */
public class UserFrequencyInfo {
    private String appid;
    private String deviceid;
    private String appplatform;
    private  String appversion;
    private String appchannel;
    private String timeValue;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
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
}
