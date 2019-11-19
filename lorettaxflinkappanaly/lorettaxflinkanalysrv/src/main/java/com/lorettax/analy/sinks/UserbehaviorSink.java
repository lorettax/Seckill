package com.lorettax.analy.sinks;


import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Mockcommon.Startup;
import com.lorettax.analy.Mockcommon.UserbehaviorInfo;
import com.lorettax.analy.dao.UserDao;
import com.lorettax.utils.HBaseUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

public class UserbehaviorSink implements SinkFunction<UserbehaviorInfo> {

    @Override
    public void invoke(UserbehaviorInfo value, Context context) throws Exception {
        Long cnts = value.getCnts();
        String timevalue = value.getTimevalue();
        String deviceId = value.getDeviceId();
        String appid = value.getAppId();
        Long activties = value.getActivties();
        String rowkey = appid +"=="+deviceId;
        String familyname = "info";

        String tablename = "userbehaviorinfo";
        //--保存30天
        Map<String,Long> datamap = new HashMap<>();


        //启动次数
        String data = HBaseUtil.getdata(tablename, rowkey, familyname,"cnts");
        if(StringUtils.isNotBlank(data)){
            datamap = JSONObject.parseObject(data,Map.class);
        }
        Long cntsbrefore = datamap.get(timevalue);
        if(cntsbrefore == null){
            cntsbrefore = 0l;
        }
        cnts = cnts + cntsbrefore;
        datamap.put(timevalue,cnts);
        String cntsjson = JSONObject.toJSONString(datamap);

        //活跃次数
        datamap = new HashMap<String,Long>();
        data = HBaseUtil.getdata(tablename, rowkey, familyname,"activties");
        if(StringUtils.isNotBlank(data)){
            datamap = JSONObject.parseObject(data,Map.class);
        }
        Long activtiesbrefore = datamap.get(timevalue);
        if(activtiesbrefore == null){
            activtiesbrefore = 0l;
        }
        activties = activties + activtiesbrefore;
        datamap.put(timevalue,activties);
        String activtiesjson = JSONObject.toJSONString(datamap);

        Map<String,String> datamapfinal = new HashMap<String,String>();
        datamapfinal.put("cnts",cntsjson);
        datamapfinal.put("activties",activtiesjson);
        HBaseUtil.put(tablename,rowkey,familyname,datamapfinal);


    }
}
