package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.BrandUser;
import com.lorettax.analy.Mockcommon.VersionUser;
import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.StartupLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class BrandMap extends RichMapFunction<KafkaEvent, BrandUser>  {
    @Override
    public BrandUser map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        StartupLog startupLog = JSONObject.parseObject(message,StartupLog.class);
        String logtype = startupLog.getLogtype();//0,app端 1，pc端 2，微信小程序...
        BrandUser brandUser = new BrandUser();
        brandUser.setBrand(startupLog.getBrand());
        brandUser.setAppId(startupLog.getAppId());
        brandUser.setAppPlatform(startupLog.getAppPlatform());
        brandUser.setAppVersion(startupLog.getAppVersion());
        brandUser.setAppChannel(startupLog.getAppChannel());
        Long createtime = startupLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        brandUser.setTimevalue(timevalue);
        brandUser.setLogtype(logtype);
        String fieldgroup = timevalue + "==branduser=="+logtype+"=="+startupLog.getAppId();
        brandUser.setGroupbyfield(fieldgroup);
        brandUser.setCnts(1L);
        Long newusers= 0L;
        Long activeusers = 0L;
        if (startupLog.getNewUser()){
            newusers = 1L;
        }
        if(startupLog.getFirstVisitToday()){
            activeusers = 1L;
        }
        brandUser.setNewusers(newusers);
        brandUser.setActiveuser(activeusers);

        return brandUser;
    }
}
