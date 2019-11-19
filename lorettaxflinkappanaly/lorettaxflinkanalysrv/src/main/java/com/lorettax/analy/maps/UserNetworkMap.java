package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.NetworkUser;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.StartupLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class UserNetworkMap extends RichMapFunction<KafkaEvent, NetworkUser>  {
    @Override
    public NetworkUser map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        StartupLog startupLog = JSONObject.parseObject(message,StartupLog.class);

        NetworkUser networkUser = new NetworkUser();
        networkUser.setAppId(startupLog.getAppId());
        networkUser.setAppPlatform(startupLog.getAppPlatform());
        networkUser.setAppVersion(startupLog.getAppVersion());
        networkUser.setNetwork(startupLog.getNetwork());
        Long createtime = startupLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        networkUser.setTimevalue(timevalue);
        String fieldgroup = timevalue + "=="+startupLog.getNetwork()+"==branduser=="+startupLog.getAppId();
        networkUser.setGroupbyfield(fieldgroup);

        networkUser.setCnts(1L);
        Long newusers = 0L;
        Long activeusers = 0L;
        if(startupLog.getNewUser()){
            newusers=1L;
        }
        if(startupLog.getFirstVisitToday()){
            activeusers =1L;
        }

        networkUser.setNewusers(newusers);
        networkUser.setActiveusers(activeusers);

        return networkUser;

    }
}
