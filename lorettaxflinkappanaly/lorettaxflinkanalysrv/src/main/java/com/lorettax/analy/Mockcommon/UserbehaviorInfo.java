package com.lorettax.analy.Mockcommon;

import java.io.Serializable;

public class UserbehaviorInfo implements Serializable {

    //--时间，以天为单位
    private String timevalue;
    //--应用唯一标识
    private String appId;
    //--设备唯一标识
    private String deviceId;
    //--分组字段
    private String groupbyfield;
    //--启动次数
    private Long cnts;
    //--用户 id
    private String userId;
    //--用户名字
    private String username;
    //--活跃次数
    private Long activties;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getActivties() {
        return activties;
    }

    public void setActivties(Long activties) {
        this.activties = activties;
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
