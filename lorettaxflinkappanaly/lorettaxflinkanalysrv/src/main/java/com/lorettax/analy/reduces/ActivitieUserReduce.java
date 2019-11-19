package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.ActivitieUser;
import com.lorettax.analy.Mockcommon.Startup;
import org.apache.flink.api.common.functions.ReduceFunction;

public class ActivitieUserReduce implements ReduceFunction<ActivitieUser> {
    @Override
    public ActivitieUser reduce(ActivitieUser activitieUser, ActivitieUser t1) throws Exception {
        String appid = activitieUser.getAppId();
        String appchannel = activitieUser.getAppChannel();
        String appplatform = activitieUser.getAppPlatform();
        String appversion = activitieUser.getAppVersion();
        String timevalue  = activitieUser.getTimevalue();
        String logtype = activitieUser.getLogtype();
        Long activtieusers = activitieUser.getActivtieusers();

        Long activtieusers1 = t1.getActivtieusers();
        ActivitieUser activitieUserfinal = new ActivitieUser();
        activitieUserfinal.setAppId(appid);
        activitieUserfinal.setAppVersion(appversion);
        activitieUserfinal.setAppChannel(appchannel);
        activitieUserfinal.setAppPlatform(appplatform);
        activitieUserfinal.setTimevalue(timevalue);
        activitieUserfinal.setLogtype(logtype);
        activitieUserfinal.setActivtieusers(activtieusers+activtieusers1);
        return activitieUserfinal;
    }
}
