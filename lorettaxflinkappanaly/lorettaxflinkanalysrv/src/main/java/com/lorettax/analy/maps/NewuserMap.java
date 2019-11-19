package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.NewUser;

import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.StartupLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class NewuserMap extends RichMapFunction<KafkaEvent, NewUser> {
    @Override
    public NewUser map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        StartupLog startupLog = JSONObject.parseObject(message,StartupLog.class);
        String logtype = startupLog.getLogtype();//0,app端 1，pc端 2，微信小程序...
        NewUser newUser = new NewUser();
        newUser.setAppId(startupLog.getAppId());
        newUser.setAppPlatform(startupLog.getAppPlatform());
        newUser.setAppVersion(startupLog.getAppVersion());
        newUser.setAppChannel(newUser.getAppChannel());
        Long createtime = startupLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        newUser.setTimevalue(timevalue);
        newUser.setLogtype(logtype);
        String fieldgroup = timevalue + "==newUser=="+logtype+"=="+startupLog.getAppId();
        newUser.setGroupbyfield(fieldgroup);

        Long newuser = 0L;
        if(startupLog.getNewUser()){
            newuser = 1L;
        }
        newUser.setNewusers(newuser);
        return newUser;
    }
}
