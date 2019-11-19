package com.lorettax.entity;

/**
 * 设备信息
 * @author Administrator
 *
 */
public class Widget {
private String id;//对应mongodb中的_id属性
private String appId;       //应用id
private String deviceId;   //设备号
private String appPlatform; //平台
private Long firstVisitAtMs;//首次访问时间
private Long lastVisitAtMs;//最近一次访问时间
private Long lastVistAtMsToday;//每天第一次访问的时间
public String getId() {
return id;
}
public void setId(String id) {
this.id = id;
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
public String getAppPlatform() {
return appPlatform;
}
public void setAppPlatform(String appPlatform) {
this.appPlatform = appPlatform;
}
public Long getFirstVisitAtMs() {
return firstVisitAtMs;
}
public void setFirstVisitAtMs(Long firstVisitAtMs) {
this.firstVisitAtMs = firstVisitAtMs;
}
public Long getLastVisitAtMs() {
return lastVisitAtMs;
}
public void setLastVisitAtMs(Long lastVisitAtMs) {
this.lastVisitAtMs = lastVisitAtMs;
}
public Long getLastVistAtMsToday() {
return lastVistAtMsToday;
}
public void setLastVistAtMsToday(Long lastVistAtMsToday) {
this.lastVistAtMsToday = lastVistAtMsToday;
}



}
