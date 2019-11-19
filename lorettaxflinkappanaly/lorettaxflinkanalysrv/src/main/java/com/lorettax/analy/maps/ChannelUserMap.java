package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.ChannelUser;
import com.lorettax.analy.Mockcommon.VersionUser;
import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.StartupLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class ChannelUserMap extends RichMapFunction<KafkaEvent, ChannelUser>  {
    @Override
    public ChannelUser map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        StartupLog startupLog = JSONObject.parseObject(message,StartupLog.class);
        String logtype = startupLog.getLogtype();//0,app端 1，pc端 2，微信小程序...
        ChannelUser channelUser = new ChannelUser();
        channelUser.setAppId(startupLog.getAppId());
        channelUser.setAppPlatform(startupLog.getAppPlatform());
        channelUser.setAppVersion(startupLog.getAppVersion());
        channelUser.setAppChannel(startupLog.getAppChannel());
        Long createtime = startupLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        channelUser.setTimevalue(timevalue);
        channelUser.setLogtype(logtype);
        String fieldgroup = timevalue + "==channeluser=="+logtype+"=="+startupLog.getAppId();
        channelUser.setGroupbyfield(fieldgroup);
        channelUser.setCnts(1L);
        Long newusers= 0L;
        Long activeusers = 0L;
        if (startupLog.getNewUser()){
            newusers = 1L;
        }
        if(startupLog.getFirstVisitToday()){
            activeusers = 1L;
        }
        channelUser.setNewusers(newusers);
        channelUser.setActiveuser(activeusers);
        return channelUser;
    }
}
