package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.ChannelUser;
import org.apache.flink.api.common.functions.ReduceFunction;

public class ChannelUserReduce implements ReduceFunction<ChannelUser> {

    @Override
    public ChannelUser reduce(ChannelUser channelUser, ChannelUser t1) throws Exception {
        String appid = channelUser.getAppId();
        String appchannel = channelUser.getAppChannel();
        String appplatform = channelUser.getAppPlatform();
        String appversion = channelUser.getAppVersion();
        String timevalue  = channelUser.getTimevalue();
        String logtype = channelUser.getLogtype();

        Long cnts1 = channelUser.getCnts();
        Long cnts2 = t1.getCnts();
        Long newusers1 = channelUser.getNewusers();
        Long newusers2 = t1.getNewusers();
        Long activeuser1 = channelUser.getActiveuser();
        Long activeuser2 = t1.getActiveuser();

        ChannelUser  channelUserfinal= new ChannelUser();
        channelUserfinal.setAppId(appid);
        channelUserfinal.setAppVersion(appversion);
        channelUserfinal.setAppChannel(appchannel);
        channelUserfinal.setAppPlatform(appplatform);
        channelUserfinal.setTimevalue(timevalue);

        channelUserfinal.setCnts(cnts1+cnts2);
        channelUserfinal.setActiveuser(activeuser1+activeuser2);
        channelUserfinal.setNewusers(newusers1+newusers2);

        channelUserfinal.setLogtype(logtype);
        return channelUserfinal;
    }
}
