package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.Startup;
import org.apache.flink.api.common.functions.ReduceFunction;

public class StartupReduce implements ReduceFunction<Startup> {
    @Override
    public Startup reduce(Startup startup, Startup t1) throws Exception {
        String appid = startup.getAppId();
        String appchannel = startup.getAppChannel();
        String appplatform = startup.getAppPlatform();
        String appversion = startup.getAppVersion();
        String timevalue  = startup.getTimevalue();
        String logtype = startup.getLogtype();
        Long cnts1 = startup.getCnts();

        Long cnts2 = t1.getCnts();
        Startup startupfinal = new Startup();
        startupfinal.setAppId(appid);
        startupfinal.setAppVersion(appversion);
        startupfinal.setAppChannel(appchannel);
        startupfinal.setAppPlatform(appplatform);
        startupfinal.setTimevalue(timevalue);
        startupfinal.setCnts(cnts1+cnts2);
        startupfinal.setLogtype(logtype);
        return startupfinal;
    }
}
