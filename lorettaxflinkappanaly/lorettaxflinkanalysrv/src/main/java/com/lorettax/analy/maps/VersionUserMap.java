package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.Mockcommon.VersionUser;
import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.StartupLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class VersionUserMap extends RichMapFunction<KafkaEvent, VersionUser>  {
    @Override
    public VersionUser map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        StartupLog startupLog = JSONObject.parseObject(message,StartupLog.class);
        String logtype = startupLog.getLogtype();//0,app端 1，pc端 2，微信小程序...
        VersionUser versionUser = new VersionUser();
        versionUser.setAppId(startupLog.getAppId());
        versionUser.setAppPlatform(startupLog.getAppPlatform());
        versionUser.setAppVersion(startupLog.getAppVersion());
        versionUser.setAppChannel(startupLog.getAppChannel());
        Long createtime = startupLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        versionUser.setTimevalue(timevalue);
        versionUser.setLogtype(logtype);
        String fieldgroup = timevalue + "==versionuser=="+logtype+"=="+startupLog.getAppId();
        versionUser.setGroupbyfield(fieldgroup);
        versionUser.setCnts(1L);
        Long newusers= 0L;
        Long activeusers = 0L;
        if (startupLog.getNewUser()){
            newusers = 1L;
        }
        if(startupLog.getFirstVisitToday()){
            activeusers = 1L;
        }
        versionUser.setNewusers(newusers);
        versionUser.setActiveuser(activeusers);

        return versionUser;
    }
}
