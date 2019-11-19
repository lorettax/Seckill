package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.BrandUser;
import org.apache.flink.api.common.functions.ReduceFunction;

public class BrandReduce implements ReduceFunction<BrandUser> {

    @Override
    public BrandUser reduce(BrandUser brandUser, BrandUser t1) throws Exception {
        String appid = brandUser.getAppId();
        String appchannel = brandUser.getAppChannel();
        String appplatform = brandUser.getAppPlatform();
        String appversion = brandUser.getAppVersion();
        String timevalue  = brandUser.getTimevalue();
        String logtype = brandUser.getLogtype();
        String brand = brandUser.getBrand();

        Long cnts1 = brandUser.getCnts();
        Long cnts2 = t1.getCnts();
        Long newusers1 = brandUser.getNewusers();
        Long newusers2 = t1.getNewusers();
        Long activeuser1 = brandUser.getActiveuser();
        Long activeuser2 = t1.getActiveuser();

        BrandUser brandUserfinal = new BrandUser();
        brandUserfinal.setAppId(appid);
        brandUserfinal.setAppVersion(appversion);
        brandUserfinal.setAppChannel(appchannel);
        brandUserfinal.setAppPlatform(appplatform);
        brandUserfinal.setTimevalue(timevalue);
        brandUserfinal.setBrand(brand);

        brandUserfinal.setCnts(cnts1+cnts2);
        brandUserfinal.setActiveuser(activeuser1+activeuser2);
        brandUserfinal.setNewusers(newusers1+newusers2);

        brandUserfinal.setLogtype(logtype);
        return brandUserfinal;
    }
}
