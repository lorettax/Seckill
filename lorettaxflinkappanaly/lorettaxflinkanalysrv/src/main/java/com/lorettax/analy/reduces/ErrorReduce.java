package com.lorettax.analy.reduces;

import com.lorettax.analy.Mockcommon.ErrorInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class ErrorReduce implements ReduceFunction<ErrorInfo> {
    @Override
    public ErrorInfo reduce(ErrorInfo errorInfo, ErrorInfo t1) throws Exception {
        String appid = errorInfo.getAppId();
        String appchannel = errorInfo.getAppChannel();
        String appplatform = errorInfo.getAppPlatform();
        String appversion = errorInfo.getAppVersion();
        String timevalue  = errorInfo.getTimevalue();
        String logtype = errorInfo.getLogtype();
        Long cnts1 = errorInfo.getCnts();
        String devicedStyle = errorInfo.getDeviceStyle();
        String osType = errorInfo.getOsType();

        Long cnts2 = t1.getCnts();
        ErrorInfo errorInfofinal = new ErrorInfo();
        errorInfofinal.setAppId(appid);
        errorInfofinal.setAppVersion(appversion);
        errorInfofinal.setAppChannel(appchannel);
        errorInfofinal.setAppPlatform(appplatform);
        errorInfofinal.setTimevalue(timevalue);
        errorInfofinal.setCnts(cnts1+cnts2);
        errorInfofinal.setLogtype(logtype);
        errorInfofinal.setOsType(osType);
        errorInfofinal.setDeviceStyle(devicedStyle);

        return errorInfofinal;
    }
}
