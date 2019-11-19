package com.lorettax.login;

import java.io.Serializable;

/**
 * @author lorettax
 */
public class StartupLog implements Serializable {
    //发生的时间
    private Long createdAtMs;
    //0,app端 1，pc端 2，微信小程序......
    private String logtype;
    //引用唯一标识，包含所有终端的应用的统称，比如pc端网站和小程序对用的应用id和app端是一样的
    private String appId;
    //设备唯一标识,包含：app端的设备号，pc端mac地址,微信小程序微信唯一标示
    private String deviceId;
    //版本
    private String appVersion;
    //渠道
    private String appChannel;
    //平台
    private String appPlatform;
    //网络
    private String network;
    //运营商
    private String carrier;

    //品牌
    private String brand;
    //机型
    private String deviceStyle;
    //分辨率
    private String screenSize;
    //操作系统
    private String osType;

    //国家
    private String country;
    //省份
    private String province;
    //城市
    private String city;

    //当日首次访问
    private Boolean isFirstVisitToday = false;
    //新安装用户
    private Boolean isNewUser = false;

    public Long getCreatedAtMs() {
        return createdAtMs;
    }

    public void setCreatedAtMs(Long createdAtMs) {
        this.createdAtMs = createdAtMs;
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
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

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDeviceStyle() {
        return deviceStyle;
    }

    public void setDeviceStyle(String deviceStyle) {
        this.deviceStyle = deviceStyle;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getFirstVisitToday() {
        return isFirstVisitToday;
    }

    public void setFirstVisitToday(Boolean firstVisitToday) {
        isFirstVisitToday = firstVisitToday;
    }

    public Boolean getNewUser() {
        return isNewUser;
    }

    public void setNewUser(Boolean newUser) {
        isNewUser = newUser;
    }

    @Override
    public String toString() {
        return "StartupLog{" +
                "createdAtMs=" + createdAtMs +
                ", logtype='" + logtype + '\'' +
                ", appId='" + appId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appChannel='" + appChannel + '\'' +
                ", appPlatform='" + appPlatform + '\'' +
                ", network='" + network + '\'' +
                ", carrier='" + carrier + '\'' +
                ", brand='" + brand + '\'' +
                ", deviceStyle='" + deviceStyle + '\'' +
                ", screenSize='" + screenSize + '\'' +
                ", osType='" + osType + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", isFirstVisitToday=" + isFirstVisitToday +
                ", isNewUser=" + isNewUser +
                '}';
    }
}
