package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;

import com.lorettax.analy.Mockcommon.UsageInfo;
import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.UsageInfoLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class UsageInfoMap extends RichMapFunction<KafkaEvent, UsageInfo>  {
    @Override
    public UsageInfo map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        UsageInfoLog startupLog = JSONObject.parseObject(message,UsageInfoLog.class);
        String logtype = UsageInfoLog.getLogtype();//0,app端 1，pc端 2，微信小程序...
        UsageInfo usageInfo = new UsageInfo();
        usageInfo.setAppId(UsageInfoLog.getAppId());
        usageInfo.setAppPlatform(UsageInfoLog.getAppPlatform());
        usageInfo.setAppVersion(UsageInfoLog.getAppVersion());
        usageInfo.setAppChannel(UsageInfoLog.getAppChannel());
        Long createtime = UsageInfoLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        usageInfo.setTimevalue(timevalue);
        usageInfo.setLogtype(logtype);
        String fieldgroup = timevalue + "==usageinfo=="+logtype+"=="+ UsageInfoLog.getAppId();
        usageInfo.setGroupbyfield(fieldgroup);
        usageInfo.setCnts(1L);

        return usageInfo;
    }
}
