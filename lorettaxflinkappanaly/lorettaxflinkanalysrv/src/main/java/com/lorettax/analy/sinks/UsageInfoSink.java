package com.lorettax.analy.sinks;


import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.Mockcommon.UsageInfo;
import com.lorettax.analy.dao.UsageInfoDao;
import com.lorettax.analy.dao.UserDao;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

public class UsageInfoSink implements SinkFunction<UsageInfo> {

    @Override
    public void invoke(UsageInfo value, Context context) throws Exception {
        Long cnts = value.getCnts();
        Long singleUseDurationSecs = value.getSingleUseDurationSecs();
        String timevalue = value.getTimevalue();
        String appversion = value.getAppVersion();
        String appPlatform = value.getAppPlatform();
        String appchannel = value.getAppChannel();
        String appid = value.getAppId();
        String logtype = value.getLogtype();
        Map<Long,Long> datamap = new HashMap<>();
        datamap.put(singleUseDurationSecs,cnts);

        String tablename = "usageinfodaily";
        if("0".equals(logtype)){
            tablename = "usageinfodailyapp";
        }else if("1".equals(logtype)){
            tablename = "usageinfodailypc";
        }else if("2".equals(logtype)){
            tablename = "usageinfodailyxiaocx";
        }

        Document doc = UsageInfoDao.findoneby(tablename, appid, appversion,  appchannel,appPlatform,timevalue);
        if(doc == null ){
            doc = new Document();
            doc = Document.parse(JSONObject.toJSONString(value));
            ObjectId objectId = new  ObjectId();
            doc.put("_id",objectId);
            String jsondatamap = JSONObject.toJSONString(datamap);
            doc.put("datamap",jsondatamap);
        }else {
            String datamapstring = doc.getString("datamap");
            Map<Long,Long> datamappre = JSONObject.parseObject(datamapstring,Map.class)==null?new HashMap<Long,Long>():JSONObject.parseObject(datamapstring,Map.class);
            Long precnts = datamappre.get(singleUseDurationSecs);
            if(precnts == null){
                precnts = 0l;
            }
            cnts = cnts + precnts;
            datamappre.put(singleUseDurationSecs,cnts);
            String mapjson = JSONObject.toJSONString(datamappre);
            doc.put("datamap",mapjson);
        }
        UsageInfoDao.saveorupdatemongo(tablename,doc);

    }
}
