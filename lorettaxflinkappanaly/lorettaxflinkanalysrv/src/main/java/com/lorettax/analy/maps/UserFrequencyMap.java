package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.UserFrequencyInfo;
import com.lorettax.analy.test.DateTimeTools;
import com.lorettax.login.StartupLog;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class UserFrequencyMap implements FlatMapFunction<String, UserFrequencyInfo> {
    @Override
    public void flatMap(String s, Collector<UserFrequencyInfo> collector) throws Exception {
        StartupLog startupLog = JSONObject.parseObject(s,StartupLog.class);
        String appid = startupLog.getAppId();
        String deviceid = startupLog.getDeviceId();
        String appPlatform = startupLog.getAppPlatform();
        String appversion  = startupLog.getAppVersion();
        String appchannel = startupLog.getAppChannel();
        Long createtime = startupLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        UserFrequencyInfo userFrequencyInfo = new UserFrequencyInfo();
        userFrequencyInfo.setAppid(appid);
        userFrequencyInfo.setAppChannel(appchannel);
        userFrequencyInfo.setAppPlatform(appPlatform);
        userFrequencyInfo.setAppVersion(appversion);
        userFrequencyInfo.setTimeValue(timevalue);
        userFrequencyInfo.setDeviceid(deviceid);
        collector.collect(userFrequencyInfo);
    }
}
