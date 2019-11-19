package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.Mockcommon.UserbehaviorInfo;
import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.StartupLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class UserbehaviorMap extends RichMapFunction<KafkaEvent, UserbehaviorInfo>  {
    @Override
    public UserbehaviorInfo map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        StartupLog startupLog = JSONObject.parseObject(message,StartupLog.class);
        String logtype = startupLog.getLogtype();//0,app端 1，pc端 2，微信小程序...
        UserbehaviorInfo userbehaviorInfo = new UserbehaviorInfo();
        userbehaviorInfo.setAppId(startupLog.getAppId());
        Long createtime = startupLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        userbehaviorInfo.setTimevalue(timevalue);
        String fieldgroup = timevalue + "==userbehavior=="+logtype+"=="+startupLog.getAppId();
        userbehaviorInfo.setGroupbyfield(fieldgroup);
        userbehaviorInfo.setCnts(1L);

        Long activeusers = 0L;
        if(startupLog.getFirstVisitToday()){
            activeusers = 1L;
        }

        return userbehaviorInfo;
    }
}
