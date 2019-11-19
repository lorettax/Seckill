package com.lorettax.analy;

import java.io.Serializable;

/**
 * Created by li on 2018/12/16.
 */
public class BrandUser implements Serializable{
    private String timevalue;//时间，以天为单位
    private String appId;//应用唯一标识
    private String appVersion;//版本
    private String appChannel;//渠道
    private String appPlatform;//平台
    private String brand;//品牌
    private String groupbyfield;//分组字段
    private Long cnts;//启动次数
    private Long newusers;//新增数
    private Long activeusers;//活跃数

    public Long getCnts() {
        return cnts;
    }

    public void setCnts(Long cnts) {
        this.cnts = cnts;
    }

    public Long getActiveusers() {
        return activeusers;
    }

    public void setActiveusers(Long activeusers) {
        this.activeusers = activeusers;
    }

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGroupbyfield() {
        return groupbyfield;
    }

    public void setGroupbyfield(String groupbyfield) {
        this.groupbyfield = groupbyfield;
    }

    public Long getNewusers() {
        return newusers;
    }

    public void setNewusers(Long newusers) {
        this.newusers = newusers;
    }

    @Override
    public String toString() {
        return "BrandUser{" +
                "timevalue='" + timevalue + '\'' +
                ", appId='" + appId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appChannel='" + appChannel + '\'' +
                ", appPlatform='" + appPlatform + '\'' +
                ", brand='" + brand + '\'' +
                ", groupbyfield='" + groupbyfield + '\'' +
                ", cnts=" + cnts +
                ", newusers=" + newusers +
                ", activeusers=" + activeusers +
                '}';
    }
}
