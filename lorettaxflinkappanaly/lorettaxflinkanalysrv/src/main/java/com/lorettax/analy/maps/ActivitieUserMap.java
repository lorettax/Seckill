package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.ActivitieUser;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.StartupLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class ActivitieUserMap extends RichMapFunction<KafkaEvent, ActivitieUser>  {
    @Override
    public ActivitieUser map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        StartupLog startupLog = JSONObject.parseObject(message,StartupLog.class);
        String logtype = startupLog.getLogtype();//0,app端 1，pc端 2，微信小程序...
        ActivitieUser activitieUser = new ActivitieUser();
        activitieUser.setAppId(startupLog.getAppId());
        activitieUser.setAppPlatform(startupLog.getAppPlatform());
        activitieUser.setAppVersion(startupLog.getAppVersion());
        activitieUser.setAppChannel(startupLog.getAppChannel());
        Long createtime = startupLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        activitieUser.setTimevalue(timevalue);
        activitieUser.setLogtype(logtype);
        String fieldgroup = timevalue + "==activitieuser=="+logtype+"=="+startupLog.getAppId();
        activitieUser.setGroupbyfield(fieldgroup);
        Long activitieusers = 0L;
        if (startupLog.getFirstVisitToday()){
            activitieusers= 1L;
        }

        activitieUser.setActivtieusers(activitieusers);
        return activitieUser;
    }
}
