package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.Mockcommon.VersionUser;
import org.apache.flink.api.common.functions.ReduceFunction;

public class VersionUserReduce implements ReduceFunction<VersionUser> {

    @Override
    public VersionUser reduce(VersionUser versionUser, VersionUser t1) throws Exception {
        String appid = versionUser.getAppId();
        String appchannel = versionUser.getAppChannel();
        String appplatform = versionUser.getAppPlatform();
        String appversion = versionUser.getAppVersion();
        String timevalue  = versionUser.getTimevalue();
        String logtype = versionUser.getLogtype();

        Long cnts1 = versionUser.getCnts();
        Long cnts2 = t1.getCnts();
        Long newusers1 = versionUser.getNewusers();
        Long newusers2 = t1.getNewusers();
        Long activeuser1 = versionUser.getActiveuser();
        Long activeuser2 = t1.getActiveuser();

        VersionUser versionUserfinal = new VersionUser();
        versionUserfinal.setAppId(appid);
        versionUserfinal.setAppVersion(appversion);
        versionUserfinal.setAppChannel(appchannel);
        versionUserfinal.setAppPlatform(appplatform);
        versionUserfinal.setTimevalue(timevalue);

        versionUserfinal.setCnts(cnts1+cnts2);
        versionUserfinal.setActiveuser(activeuser1+activeuser2);
        versionUserfinal.setNewusers(newusers1+newusers2);

        versionUserfinal.setLogtype(logtype);
        return versionUserfinal;
    }
}
