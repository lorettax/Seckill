package com.lorettax.analy.sinks;


import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.dao.UserDao;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;
import org.bson.types.ObjectId;

public class StartupSink implements SinkFunction<Startup> {

    @Override
    public void invoke(Startup value, Context context) throws Exception {
        Long cnts = value.getCnts();
        String timevalue = value.getTimevalue();
        String appversion = value.getAppVersion();
        String appPlatform = value.getAppPlatform();
        String appchannel = value.getAppChannel();
        String appid = value.getAppId();
        String logtype = value.getLogtype();
        String tablename = "userstartupinfodaily";
        if ("0".equals(logtype)){
            tablename = "userstartupinfodailyapp";
        }else if ("1".equals(logtype)){
            tablename = "userstartupinfodailypc";
        }else if ("2".equals(logtype)){
            tablename = "userstartupinfodailyxiaocx";
        }

        Document doc = UserDao.findoneby(tablename, appid, appversion,  appchannel,appPlatform,timevalue);
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
        UserDao.saveorupdatemongo(tablename,doc);
    }
}
