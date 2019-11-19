package com.lorettax.analy.Mockcommon;

import java.io.Serializable;

public class NetworkUser implements Serializable {

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
    //--网络
    private String network;
    //--新增数
    private Long newusers;
    //--活跃数
    private Long activeusers;
    //--启动次数
    private Long cnts;


    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Long getNewusers() {
        return newusers;
    }

    public void setNewusers(Long newusers) {
        this.newusers = newusers;
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

    public String getGroupbyfield() {
        return groupbyfield;
    }

    public void setGroupbyfield(String groupbyfield) {
        this.groupbyfield = groupbyfield;
    }


    public Long getCnts() {
        return cnts;
    }

    public void setCnts(Long cnts) {
        this.cnts = cnts;
    }
}
