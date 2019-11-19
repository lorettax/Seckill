package com.lorettax.analy.maps;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.ErrorInfo;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.tasks.KafkaEvent;
import com.lorettax.login.ErrorInfoLog;
import com.lorettax.login.StartupLog;
import com.lorettax.utils.DateTimeTools;
import org.apache.flink.api.common.functions.RichMapFunction;

public class ErrorMap extends RichMapFunction<KafkaEvent, ErrorInfo>  {
    @Override
    public ErrorInfo map(KafkaEvent kafkaEvent) throws Exception {
        String message = kafkaEvent.getMessage();
        ErrorInfoLog errorInfoLog = JSONObject.parseObject(message,ErrorInfoLog.class);
        String logtype = errorInfoLog.getLogtype();//0,app端 1，pc端 2，微信小程序...
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setAppId(errorInfoLog.getAppId());
        errorInfo.setAppPlatform(errorInfoLog.getAppPlatform());
        errorInfo.setAppVersion(errorInfoLog.getAppVersion());
        errorInfo.setAppChannel(errorInfoLog.getAppChannel());
        Long createtime = errorInfoLog.getCreatedAtMs();
        String timevalue = DateTimeTools.getstrbyday(createtime);
        errorInfo.setTimevalue(timevalue);
        errorInfo.setLogtype(logtype);
        String errorBrief = errorInfoLog.getErrorBrief();
        String errorDetail = errorInfoLog.getErrorDetail();


        /**
         * at cn.xxx.appto.control.CommandUtil.getInfo(CommandUtil.java:67) "+
         "at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)" +
         " at java.lang.reflect.Method.invoke(Method.java:606)


         * at cn.xxx.appto.control.CommandUtil.getInfo(CommandUtil.java:67) "+
         "at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)" +
         " at java.lang.reflect.Method.invoke(Method.java:606)\n
         at cn.xxx.appto.control.CommandUtil.getInfo(CommandUtil.java:67) "+
         */
        String errorid = errorDetail.substring(0,errorDetail.indexOf("\n")==-1?
                errorDetail.length():
                errorDetail.indexOf("\n"))+"=====>"+errorBrief;

        errorInfo.setErrorid(errorid);
        errorInfo.setCnts(1L);

        String fieldgroup =errorid +"=="+logtype;
        errorInfo.setGroupbyfield(fieldgroup);
        errorInfo.setCnts(1L);

        return errorInfo;
    }
}
