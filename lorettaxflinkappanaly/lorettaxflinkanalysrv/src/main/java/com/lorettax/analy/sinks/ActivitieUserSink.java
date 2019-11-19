package com.lorettax.analy.sinks;


import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.ActivitieUser;
import com.lorettax.analy.dao.UserDao;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ActivitieUserSink implements SinkFunction<ActivitieUser> {

    @Override
    public void invoke(ActivitieUser value, Context context) throws Exception {
        Long activtieusers = value.getActivtieusers();
        String timevalue = value.getTimevalue();
        String appversion = value.getAppVersion();
        String appPlatform = value.getAppPlatform();
        String appchannel = value.getAppChannel();
        String appid = value.getAppId();
        String logtype = value.getLogtype();

        String tablename = "useractivtieinfodaily";
        if("0".equals(logtype)){
            tablename = "useractivtieinfodailyapp";
        }else if("1".equals(logtype)){
            tablename = "useractivtieinfodailypc";
        }else if("2".equals(logtype)){
            tablename = "useractivtieinfodailyxiaocx";
        }
        Document doc = UserDao.findoneby(tablename, appid, appversion,  appchannel,appPlatform,timevalue);
        if(doc == null ){
            doc = new Document();
            doc = Document.parse(JSONObject.toJSONString(value));
            ObjectId objectId = new  ObjectId();
            doc.put("_id",objectId);
        }else {
            Long activitiespre = 0l;
            try {
                activitiespre  = (Long) doc.get("activtieusers");
            }catch (ClassCastException e){
                activitiespre = Long.valueOf((Integer)doc.get("activtieusers"));
            }
            doc.put("activtieusers",activitiespre+activtieusers);
        }
        UserDao.saveorupdatemongo(tablename,doc);
    }
}
