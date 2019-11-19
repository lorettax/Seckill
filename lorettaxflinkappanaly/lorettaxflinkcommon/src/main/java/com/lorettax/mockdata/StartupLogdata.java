package com.lorettax.mockdata;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.login.StartupLog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author lorettax
 */
public class StartupLogdata {

    private static String url = "http://127.0.0.1:8080/lorettaxappin/appin/startupinfo";
    private static  Long[] createdAtMs = getCreatedAtMs("20191115","yyyyMMdd",10);
    private static String appid = "lorettaxflink";
    private static String[] deviceIds = new String[]{"ehdevice1232","eodevice2123","eodevice3221","eodevice4123","eodevice5123"};
    private static String[] appVersions = new String[]{"2.3.1","2.3.2","2.3.3"};
    private static String[] appChannels = new String[]{"appstore","小米商城","安卓应用市场"};
    private static String[] appPlatforms = new String[]{"ios","android"};
    private static String[] newworks = new String[]{"移动","联通","电信","wifi"};
    private static String[] carriers = new String[]{"移动","联通","电信"};
    private static String[] brands = new String[]{"小米手机","华为","锤子","iphone"};
    private static String[] deviceStyles = new String[]{"华为meta9","小米6","iphone6s","锤子6"};
    private static String[] screenSizes = new String[]{"200*500","600*900"};
    private static String[] osTypes = new String[]{"android 4","android 5","ios 10","ios 11"};
    private static String[] logtypes = new String[]{"0","1","2"};

    static Random random = new Random();



    /**
     * 发生时间
     * @param timevalue
     * @param dataformat
     * @param number
     * @return
     */
    private static Long[] getCreatedAtMs(String timevalue,String dataformat,int number){
        DateFormat dateFormat = new SimpleDateFormat(dataformat);
        Date date = null;
        try {
            date = dateFormat.parse(timevalue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long[] timearray = new Long[number];
        for(int i = 0;i<number;i++){
            long newdate = date.getTime() + i*3600*1000*241;
            timearray[i] = newdate;
        }
        return timearray;
    }

    public static void  main(String[] args){
       StartupLog startupLog = new StartupLog();
       startupLog.setCreatedAtMs(createdAtMs[random.nextInt(createdAtMs.length)]);
       startupLog.setAppId(appid);
       startupLog.setDeviceId(deviceIds[random.nextInt(deviceIds.length)]+ UUID.randomUUID());
       startupLog.setAppVersion(appVersions[random.nextInt(appVersions.length)]);
       startupLog.setAppChannel(appChannels[random.nextInt(appChannels.length)]);
       startupLog.setAppPlatform(appPlatforms[random.nextInt(appPlatforms.length)]);
       startupLog.setNetwork(newworks[random.nextInt(newworks.length)]);
       startupLog.setCarrier(carriers[random.nextInt(random.nextInt(carriers.length))]);
       startupLog.setBrand(brands[random.nextInt(brands.length)]);
       startupLog.setDeviceStyle(deviceStyles[random.nextInt(deviceStyles.length)]);
       startupLog.setScreenSize(screenSizes[random.nextInt(screenSizes.length)]);
       startupLog.setOsType(osTypes[random.nextInt(osTypes.length)]);
       startupLog.setLogtype(logtypes[random.nextInt(logtypes.length)]);
       String params = JSONObject.toJSONString(startupLog);
       HTTPUtils.httppost(url,params);

    }
}
