package com.lorettax.mockdata;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.login.UsageInfoLog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UsageinfoLogdata {

    private static String url = "http://127.0.0.1:8080/lorettaxappin/appin/startupinfo";
    private static  Long[] createdAtMs = getCreatedAtMs("20191115","yyyyMMdd",10);
    private static String appid = "lorettaxflink";
    private static String[] deviceIds = new String[]{"ehdevice1232","eodevice2123","eodevice3221","eodevice4123","eodevice5123"};
    private static String[] appVersions = new String[]{"2.3.1","2.3.2","2.3.3"};
    private static String[] appChannels = new String[]{"appstore","小米商城","安卓应用市场"};
    private static String[] appPlatforms = new String[]{"ios","android"};

    private static Long[] singleUseDurationSecses = new Long[]{2L,5L,6L,25L,250L,560L,780L,920L};


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

    public static void main(String[] args) {
        for(int i =0;i<10;i++){
            UsageInfoLog usageInfoLog = new UsageInfoLog();
            usageInfoLog.setCreatedAtMs(createdAtMs[i]);
            usageInfoLog.setDeviceId(deviceIds[random.nextInt(deviceIds.length)]);
            usageInfoLog.setAppVersion(deviceIds[random.nextInt(deviceIds.length)]);
            usageInfoLog.setAppChannel(appChannels[random.nextInt(appChannels.length)]);
            usageInfoLog.setAppPlatform(appPlatforms[random.nextInt(appPlatforms.length)]);
            usageInfoLog.setSingleUseDurationSecs(singleUseDurationSecses[random.nextInt(singleUseDurationSecses.length)]);
            String params = JSONObject.toJSONString(usageInfoLog);
            HTTPUtils.httppost(url,params);

        }
    }

}
