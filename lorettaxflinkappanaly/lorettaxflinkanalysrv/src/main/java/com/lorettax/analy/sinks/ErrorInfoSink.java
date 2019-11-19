package com.lorettax.analy.sinks;


import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.ErrorInfo;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.dao.ErrorDao;
import com.lorettax.analy.dao.UserDao;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ErrorInfoSink implements SinkFunction<ErrorInfo> {

    @Override
    public void invoke(ErrorInfo value, Context context) throws Exception {
        Long cnts = value.getCnts();
        String timevalue = value.getTimevalue();
        String appversion = value.getAppVersion();
        String appPlatform = value.getAppPlatform();
        String appchannel = value.getAppChannel();
        String appid = value.getAppId();
        String logtype = value.getLogtype();
        String ostype = value.getOsType();
        String deviceStyle = value.getDeviceStyle();

        String tablename = "errorinfodaily";
        if("0".equals(logtype)){
            tablename = "errorinfodailyapp";
        }else if("1".equals(logtype)){
            tablename = "errorinfodailypc";
        }else if("2".equals(logtype)){
            tablename = "errorinfodailyxiaocx";
        }

        Document doc = ErrorDao.findoneby(tablename, appid, appversion,  appchannel,appPlatform,timevalue,ostype,deviceStyle);
        if(doc == null ){
            doc = new Document();
            doc = Document.parse(JSONObject.toJSONString(value));
            ObjectId objectId = new  ObjectId();
            doc.put("_id",objectId);
        }else {
            Long cntspre = 0l;
            try {
                cntspre  = (Long) doc.get("cnts");
            }catch (ClassCastException e){
                cntspre = Long.valueOf((Integer)doc.get("cnts"));
            }
            doc.put("cnts",cntspre+cnts);

        }
        ErrorDao.saveorupdatemongo(tablename,doc);
    }
}
