package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.NewUser;
import com.lorettax.analy.Mockcommon.Startup;
import org.apache.flink.api.common.functions.ReduceFunction;

public class NewUserReduce implements ReduceFunction<NewUser> {

    @Override
    public NewUser reduce(NewUser newUser, NewUser t1) throws Exception {
        String appid = newUser.getAppId();
        String appchannel = newUser.getAppChannel();
        String appplatform = newUser.getAppPlatform();
        String appversion = newUser.getAppVersion();
        String timevalue  = newUser.getTimevalue();
        String logtype = newUser.getLogtype();

        Long newusers1 = newUser.getNewusers();
        Long newusers2 = t1.getNewusers();

        NewUser newUserfinal = new NewUser();
        newUserfinal.setAppId(appid);
        newUserfinal.setAppVersion(appversion);
        newUserfinal.setAppChannel(appchannel);
        newUserfinal.setAppPlatform(appplatform);
        newUserfinal.setTimevalue(timevalue);

        newUserfinal.setNewusers(newusers1+newusers2);

        newUserfinal.setLogtype(logtype);
        return newUserfinal;
    }
}
