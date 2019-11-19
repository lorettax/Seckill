package com.lorettax.analy.sinks;


import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.NewUser;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.dao.UserDao;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NewUserSink implements SinkFunction<NewUser> {

    @Override
    public void invoke(NewUser value, Context context) throws Exception {
        String timevalue = value.getTimevalue();
        String appversion = value.getAppVersion();
        String appPlatform = value.getAppPlatform();
        String appchannel = value.getAppChannel();
        String appid = value.getAppId();
        String logtype = value.getLogtype();
        Long newusers = value.getNewusers();

        String tablename = "newuserinfodaily";
        if ("0".equals(logtype)){
            tablename = "newuserinfodailyapp";
        }else if ("1".equals(logtype)){
            tablename = "newuserinfodailypc";
        }else if ("2".equals(logtype)){
            tablename = "newuserinfodailyxiaocx";
        }

        Document doc = UserDao.findoneby(tablename, appid, appversion,  appchannel,appPlatform,timevalue);
        if(doc == null ){
            doc = new Document();
            doc = Document.parse(JSONObject.toJSONString(value));
            ObjectId objectId = new  ObjectId();
            doc.put("_id",objectId);
        }else {
            Long newusespre = 0l;
            try {
                newusespre  = (Long) doc.get("newusers");
            }catch (ClassCastException e){
                newusespre = Long.valueOf((Integer)doc.get("newusers"));
            }
            doc.put("newusers",newusespre+newusers);

        }
        UserDao.saveorupdatemongo(tablename,doc);
    }
}
