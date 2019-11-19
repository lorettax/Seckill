package com.lorettax.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.UserbehvaviorInfo;
import com.lorettax.analy.Versionuser;
import com.lorettax.dao.UserBehaviorDao;
import com.lorettax.utils.DateTimeTools;
import com.lorettax.utils.HBaseUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by li on 2018/12/16.
 */
@Component
public class UserBehaviorDaoImpl implements UserBehaviorDao {


    @Override
    public List<UserbehvaviorInfo> listUserBehaviorInfoby(String appId, String timeFrom, String timeTo, String timeUnit, String[] deviceIds) {
        List<UserbehvaviorInfo> list = new ArrayList<UserbehvaviorInfo>();
        if(StringUtils.isBlank(appId)||deviceIds==null ||deviceIds.length<1){
            return list;
        }
        String tablename = "userbehaviorinfo";
        String famliyname = "info";
        Map<String,Long> cntsfinal = new HashMap<String,Long>();
        Map<String,Long> activitesfinal = new HashMap<String,Long>();
        Set<String> timeset = new HashSet<String>();
        for(String deviceid: deviceIds){
            String rowkey = appId+"=="+deviceid;
            try {
                String jsoncnts = HBaseUtil.getdata(tablename,rowkey,famliyname,"cnts");
                Map<String,Long> datamap = new HashMap<String,Long>();
                if(StringUtils.isNotBlank(jsoncnts)){
                    datamap = JSONObject.parseObject(jsoncnts,Map.class);
                }
                Set<Map.Entry<String,Long>> entrySet = datamap.entrySet();
                for(Map.Entry<String,Long> entry:entrySet){
                    String key = entry.getKey();
                    Long value = entry.getValue();
                    Long prevalue = cntsfinal.get(key);
                    if(prevalue ==null){
                        prevalue = 0l;
                    }
                    timeset.add(key);
                    cntsfinal.put(key,prevalue+value);
                }

                datamap = new HashMap<String,Long>();
                String jsonactivites = HBaseUtil.getdata(tablename,rowkey,famliyname,"activties");
                if(StringUtils.isNotBlank(jsonactivites)){
                    datamap = JSONObject.parseObject(jsonactivites,Map.class);
                }
                entrySet = datamap.entrySet();
                for(Map.Entry<String,Long> entry:entrySet){
                    String key = entry.getKey();
                    Long value = entry.getValue();
                    Long prevalue = activitesfinal.get(key);
                    if(prevalue ==null){
                        prevalue = 0l;
                    }
                    activitesfinal.put(key,prevalue+value);
                    timeset.add(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        for(String timevalue:timeset){
            UserbehvaviorInfo userbehvaviorInfo = new UserbehvaviorInfo();
            userbehvaviorInfo.setTimevalue(timevalue);
            userbehvaviorInfo.setAppId(appId);
            userbehvaviorInfo.setCnts(cntsfinal.get(timevalue));
            userbehvaviorInfo.setActivties(activitesfinal.get(timevalue));
            list.add(userbehvaviorInfo);
        }

        return list;
    }
}
