package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.StartupLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class StartupMap extends RichMapFunction<KafkaEvent, Startup>  {
    @Override
    public Startup map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        StartupLog startupLog = JSONObject.parseObject(message,StartupLog.class);
        String logtype = startupLog.getLogtype();//0,app端 1，pc端 2，微信小程序...
        Startup startup = new Startup();
        startup.setAppId(startupLog.getAppId());
        startup.setAppPlatform(startupLog.getAppPlatform());
        startup.setAppVersion(startupLog.getAppVersion());
        startup.setAppChannel(startupLog.getAppChannel());
        Long createtime = startupLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        startup.setTimevalue(timevalue);
        startup.setLogtype(logtype);
        String fieldgroup = timevalue + "==startup=="+logtype+"=="+startupLog.getAppId();
        startup.setGroupbyfield(fieldgroup);
        startup.setCnts(1L);
        return startup;
    }
}
