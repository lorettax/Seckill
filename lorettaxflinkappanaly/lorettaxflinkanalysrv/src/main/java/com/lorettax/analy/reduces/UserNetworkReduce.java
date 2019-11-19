package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.NetworkUser;
import org.apache.flink.api.common.functions.ReduceFunction;

public class UserNetworkReduce implements ReduceFunction<NetworkUser> {

    @Override
    public NetworkUser reduce(NetworkUser networkUser, NetworkUser t1) throws Exception {
        String appid = networkUser.getAppId();
        String appchannel = networkUser.getAppChannel();
        String appplatform = networkUser.getAppPlatform();
        String appversion = networkUser.getAppVersion();
        String timevalue  = networkUser.getTimevalue();

        Long cnts1 = networkUser.getCnts();
        Long newusers1 = networkUser.getNewusers();
        Long activeusers1 = networkUser.getActiveusers();
        Long cnts2 = t1.getCnts();
        Long newusers2 = t1.getNewusers();
        Long activeusers2 = t1.getActiveusers();

        NetworkUser networkUserfinal = new NetworkUser();
        networkUserfinal.setAppId(appid);
        networkUserfinal.setAppVersion(appversion);
        networkUserfinal.setAppChannel(appchannel);
        networkUserfinal.setAppPlatform(appplatform);
        networkUserfinal.setTimevalue(timevalue);
        networkUserfinal.setCnts(cnts1+cnts2);
        networkUserfinal.setNewusers(newusers1+newusers2);
        networkUserfinal.setActiveusers(activeusers1+activeusers2);
        return networkUserfinal;
    }
}
