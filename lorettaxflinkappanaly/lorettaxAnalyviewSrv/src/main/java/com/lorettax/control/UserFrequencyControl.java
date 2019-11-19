package com.lorettax.control;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.UserFrequencyResultInfo;
import com.lorettax.entity.UserFrequencyAnalydata;
import com.lorettax.service.UserFrequencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by li on 2018/12/16.
 */
@RestController
@RequestMapping("userFrequency")
@CrossOrigin
public class UserFrequencyControl {

    @Autowired
    private UserFrequencyService userFrequencyService;

    @RequestMapping(value = "listuserFrequencyinfoby",method = RequestMethod.POST)
    public String listuserFrequencyinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<UserFrequencyResultInfo> list = userFrequencyService.listUserFrequencyinfoby("lorettaxflink","20180819","20181010",null,null,null,null);
        Map<String,Long> mapdata = new TreeMap<String,Long>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Long.valueOf(o1).compareTo(Long.valueOf(o2));
            }
        });

        for(UserFrequencyResultInfo userFrequencyResultInfo :list){
            String number = userFrequencyResultInfo.getNumber();
            String devicenums = userFrequencyResultInfo.getDevicenums();
            Long predevicenums = mapdata.get(number)==null?0l:mapdata.get(number);
            mapdata.put(number, predevicenums+Long.valueOf(devicenums));
        }
        List<String> xlist = new ArrayList<String>();
        List<Long> datalist = new ArrayList<Long>();
        Set<Map.Entry<String,Long>> set = mapdata.entrySet();
        for(Map.Entry<String,Long> entry:set){
                String key = entry.getKey();
                Long value = entry.getValue();
            xlist.add(key);
            datalist.add(value);
        }
        UserFrequencyAnalydata userFrequencydata = new UserFrequencyAnalydata();
        userFrequencydata.setXlist(xlist);
        HashMap<String,Object> datamap = new HashMap<String,Object>();
        datamap.put("name","使用频率");
        datamap.put("data",datalist);
        List<Map<String,Object>> datamaplist  = new ArrayList<Map<String, Object>>();
        datamaplist.add(datamap);
        userFrequencydata.setDatalist(datamaplist);
        String jsonstring = JSONObject.toJSONString(userFrequencydata);
        return jsonstring;
    }


}
