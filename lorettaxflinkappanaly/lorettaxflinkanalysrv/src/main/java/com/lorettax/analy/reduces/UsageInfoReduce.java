package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.UsageInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class UsageInfoReduce implements ReduceFunction<UsageInfo> {
    @Override
    public UsageInfo reduce(UsageInfo usageInfo, UsageInfo t1) throws Exception {
        String appid = usageInfo.getAppId();
        String appchannel = usageInfo.getAppChannel();
        String appplatform = usageInfo.getAppPlatform();
        String appversion = usageInfo.getAppVersion();
        String timevalue  = usageInfo.getTimevalue();
        String logtype = usageInfo.getLogtype();
        Long cnts1 = usageInfo.getCnts();
        Long singleUseDrurationSecs = usageInfo.getSingleUseDurationSecs();

        Long cnts2 = t1.getCnts();
        UsageInfo usageInfofinal = new UsageInfo();
        usageInfofinal.setSingleUseDurationSecs(singleUseDrurationSecs);
        usageInfofinal.setAppId(appid);
        usageInfofinal.setAppVersion(appversion);
        usageInfofinal.setAppChannel(appchannel);
        usageInfofinal.setAppPlatform(appplatform);
        usageInfofinal.setTimevalue(timevalue);
        usageInfofinal.setCnts(cnts1+cnts2);
        usageInfofinal.setLogtype(logtype);
        return usageInfofinal;
    }
}
