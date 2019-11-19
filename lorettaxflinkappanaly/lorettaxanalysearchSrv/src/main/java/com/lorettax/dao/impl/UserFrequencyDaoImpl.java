package com.lorettax.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.*;
import com.lorettax.dao.UserFrequencyDao;
import com.lorettax.utils.DateTimeTools;
import com.lorettax.utils.HBaseUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by li on 2018/12/16.
 */
@Component
public class UserFrequencyDaoImpl implements UserFrequencyDao {

    @Override
    public List<UserFrequencyResultInfo> listUserFrequencyinfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform) {
        List<UserFrequencyResultInfo> listfinal = new ArrayList<UserFrequencyResultInfo>();
        if(StringUtils.isBlank(appId)){
            return listfinal;
        }

        if(timeUnit == null || timeUnit.trim().isEmpty()){
            timeUnit = "daily";
        }
        Long timeNow = System.currentTimeMillis();

        if(timeFrom==null || timeFrom.trim().isEmpty()) {
            timeFrom = DateTimeTools.getstrbyday(timeNow - (long)7*24*3600*1000);
        }
        if(timeTo==null || timeTo.trim().isEmpty()) {
            timeTo = DateTimeTools.getstrbyday(timeNow);
        }
        try {
            Set<String> timeset = DateTimeTools.gettimebyfromandto(timeFrom,timeTo);
            for(String time:timeset){
                String rowkey = appId + "=="+time;
                String tablename = "userfrequencyinfo";
                String famliyname = "listinfo";
                String colunm = "list";
                String datastring = HBaseUtil.getdata(tablename,rowkey,famliyname,colunm);
                if(StringUtils.isBlank(datastring)){
                    continue;
                }

                List<UserFrequencyResultInfo> listdata = JSONObject.parseArray(datastring,UserFrequencyResultInfo.class);
                for(UserFrequencyResultInfo userFrequencyResultInfo:listdata){
                    String appVersionentity = userFrequencyResultInfo.getAppversion();
                    String appChannelentity = userFrequencyResultInfo.getAppchannel();
                    String appPlatformentity = userFrequencyResultInfo.getAppplatform();
                    if(StringUtils.isNotBlank(appVersion)){
                        if(!appVersionentity.equals(appVersion)){
                            continue;
                        }
                    }
                    if(StringUtils.isNotBlank(appChannel)){
                        if(!appChannelentity.equals(appChannel)){
                            continue;
                        }
                    }
                    if(StringUtils.isNotBlank(appPlatform)){
                        if(!appPlatformentity.equals(appPlatform)){
                            continue;
                        }
                    }
                    listfinal.add(userFrequencyResultInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listfinal;
    }
}
