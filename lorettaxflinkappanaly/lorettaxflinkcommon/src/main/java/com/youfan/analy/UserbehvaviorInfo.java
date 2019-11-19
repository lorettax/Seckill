package com.lorettax.analy;

import java.io.Serializable;

/**
 * Created by li on 2018/12/16.
 */
public class UserbehvaviorInfo implements Serializable{
    private String timevalue;//时间，以天为单位
    private String appId;//应用唯一标识
    private String deviceId;//设备唯一标识
    private Long cnts;//启动次数
    private Long activties;//活跃次数
    private String groupfield;//分组
    private String userid;//用户id
    private String username;//用户名称

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getCnts() {
        return cnts;
    }

    public void setCnts(Long cnts) {
        this.cnts = cnts;
    }

    public Long getActivties() {
        return activties;
    }

    public void setActivties(Long activties) {
        this.activties = activties;
    }

    public String getGroupfield() {
        return groupfield;
    }

    public void setGroupfield(String groupfield) {
        this.groupfield = groupfield;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "UserbehvaviorInfo{" +
                "timevalue='" + timevalue + '\'' +
                ", appId='" + appId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", cnts=" + cnts +
                ", activties=" + activties +
                ", groupfield='" + groupfield + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
