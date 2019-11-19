package com.lorettax.analy.Mockcommon;

import java.io.Serializable;

public class Startup implements Serializable {

    //--时间，以天为单位
    private String timevalue;
    //--应用唯一标识
    private String appId;
    //--版本
    private String appVersion;
    //--渠道
    private String appChannel;
    //--平台
    private String appPlatform;
    //--分组字段
    private String groupbyfield;
    //0,app端 1，pc端 2，微信小程序......
    private String logtype;
    //--启动次数
    private Long cnts;

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

    public String getGroupbyfield() {
        return groupbyfield;
    }

    public void setGroupbyfield(String groupbyfield) {
        this.groupbyfield = groupbyfield;
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }

    public Long getCnts() {
        return cnts;
    }

    public void setCnts(Long cnts) {
        this.cnts = cnts;
    }
}
