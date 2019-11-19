package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.UserbehaviorInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class UserBehaviorReduce implements ReduceFunction<UserbehaviorInfo> {
    @Override
    public UserbehaviorInfo reduce(UserbehaviorInfo userbehaviorInfo, UserbehaviorInfo t1) throws Exception {
        String appid = userbehaviorInfo.getAppId();
        String timevalue  = userbehaviorInfo.getTimevalue();
        Long cnts1 = userbehaviorInfo.getCnts();
        String deviceId1 = userbehaviorInfo.getDeviceId();
        Long activites1 = userbehaviorInfo.getActivties();
        Long activites2 = userbehaviorInfo.getActivties();

        Long cnts2 = t1.getCnts();
        String deviceId2 = t1.getDeviceId();
        UserbehaviorInfo userbehaviorInfofinal = new UserbehaviorInfo();
        userbehaviorInfofinal.setAppId(appid);
        userbehaviorInfofinal.setTimevalue(timevalue);
        userbehaviorInfofinal.setCnts(cnts1+cnts2);
        userbehaviorInfofinal.setDeviceId(deviceId1+deviceId2);
        userbehaviorInfofinal.setActivties(activites1+activites2);
        return userbehaviorInfofinal;
    }
}
