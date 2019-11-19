package com.lorettax.mockdata;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.login.ErrorInfoLog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ErrorLogdata {

    private static String url = "http://127.0.0.1:8080/lorettaxappin/appin/startupinfo";
    private static  Long[] createdAtMs = getCreatedAtMs("20191115","yyyyMMdd",10);
    private static String appid = "lorettaxflink";
    private static String[] deviceIds = new String[]{"ehdevice1232","eodevice2123","eodevice3221","eodevice4123","eodevice5123"};
    private static String[] appVersions = new String[]{"2.3.1","2.3.2","2.3.3"};
    private static String[] appChannels = new String[]{"appstore","小米商城","安卓应用市场"};
    private static String[] appPlatforms = new String[]{"ios","android"};
    private static String[] deviceStyles = new String[]{"华为meta9","小米6","iphone6s","锤子6"};
    private static String[] screenSizes = new String[]{"200*500","600*900"};
    private static String[] osTypes = new String[]{"android 4","android 5","ios 10","ios 11"};

    //--错误摘要
    private static String[] errorBriefs = {"at cn.xxx.appto55.web.AbstractBaseController.validInbound(AbstractBaseController.java:72)"
            ,"at cn.xxx.appto55.control.CommandUtil.getInfo(CommandUtil.java:67)"
    };
    //--错误详情
    private static String[] errorDetails ={"java.lang.NullPointerException    "
            + "at cn.sbz.appto55.web.AbstractBaseController.validInbound(AbstractBaseController.java:72) "+
            "at cn.xxx.appto.web.AbstractBaseController.validInbound",
            "at cn.xxx.appto.control.CommandUtil.getInfo(CommandUtil.java:67) "+
                    "at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)" +
                    " at java.lang.reflect.Method.invoke(Method.java:606)"};
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

        for (int i = 0; i <10 ; i++) {
            ErrorInfoLog errorInfoLog = new ErrorInfoLog();
            errorInfoLog.setCreatedAtMs(createdAtMs[i]);
            errorInfoLog.setAppId(appid);
            errorInfoLog.setDeviceId(deviceIds[random.nextInt(deviceIds.length)]);
            errorInfoLog.setAppVersion(deviceIds[random.nextInt(deviceIds.length)]);
            errorInfoLog.setAppChannel(appChannels[random.nextInt(appChannels.length)]);
            errorInfoLog.setAppPlatform(appPlatforms[random.nextInt(appPlatforms.length)]);
            errorInfoLog.setDeviceStyle(deviceStyles[random.nextInt(deviceStyles.length)]);
            errorInfoLog.setOsType(osTypes[random.nextInt(osTypes.length)]);
            errorInfoLog.setErrorBrief(errorBriefs[random.nextInt(errorBriefs.length)]);
            errorInfoLog.setErrorDetail(errorDetails[random.nextInt(errorDetails.length)]);
            String params = JSONObject.toJSONString(errorInfoLog);
            HTTPUtils.httppost(url,params);
        }


    }

}
