package com.lorettax.analy;

/**
 * Created by li on 2018/12/31.
 */
public class Usageinfo {
    private String timeValue;//发生的时间
    private String appId;//应用id
    private String appVersion;//版本号
    private String appChannel;//渠道
    private String appPlatform;//平台
    private Long singleUseDurationSecs;//单次使用时长，单位是秒
    private Long cnts;
    private String datamap;//结果map

    private String groupfield;//分组字段
private  String logtype;////0,app端 1，pc端 2，微信小程序......
    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
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

    public Long getSingleUseDurationSecs() {
        return singleUseDurationSecs;
    }

    public void setSingleUseDurationSecs(Long singleUseDurationSecs) {
        this.singleUseDurationSecs = singleUseDurationSecs;
    }

    public Long getCnts() {
        return cnts;
    }

    public void setCnts(Long cnts) {
        this.cnts = cnts;
    }

    public String getDatamap() {
        return datamap;
    }

    public void setDatamap(String datamap) {
        this.datamap = datamap;
    }

    public String getGroupfield() {
        return groupfield;
    }

    public void setGroupfield(String groupfield) {
        this.groupfield = groupfield;
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }
}
