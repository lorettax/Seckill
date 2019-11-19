package com.lorettax.analy;

import java.io.Serializable;

/**
 * Created by li on 2018/12/16.
 */
public class NewUser implements Serializable{
    private String timevalue;//时间，以天为单位
    private String appId;//应用唯一标识
    private String appVersion;//版本
    private String appChannel;//渠道
    private String appPlatform;//平台
    private String groupbyfield;//分组字段
    private Long newusers;//新增用户数
    private String logtype;//0,app端 1，pc端 2，微信小程序......

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
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

    public Long getNewusers() {
        return newusers;
    }

    public void setNewusers(Long newusers) {
        this.newusers = newusers;
    }

    public String getGroupbyfield() {
        return groupbyfield;
    }

    public void setGroupbyfield(String groupbyfield) {
        this.groupbyfield = groupbyfield;
    }
}
