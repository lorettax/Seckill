package com.lorettax.control;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.*;
import com.lorettax.entity.UsageAnalydata;
import com.lorettax.service.UserService;
import com.lorettax.service.UserUsageService;
import com.lorettax.utils.MapDeepCopy;
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
@RequestMapping("userUasageControl")
@CrossOrigin
public class UserUasageControl {

    @Autowired
    private UserUsageService userUsageService;

    @RequestMapping(value = "listuserUsageInfoby",method = RequestMethod.POST)
    public String listuserUsageInfoby(HttpServletRequest req, HttpServletResponse resp){
        List<List<Usageinfo>> list = userUsageService.listUsageinfoby("lorettaxflink","20180819","20181010",null,null,null,null);
        List<Usageinfo> applist = list.get(0);
        List<Usageinfo> pclist = list.get(1);
        List<Usageinfo> xiaocxlist = list.get(2);
        //处理时间
        Set<Long> singleUseDurationSecsset = new HashSet<Long>();
        for(Usageinfo usageinfo :applist){
            Long singleUseDurationSecs = usageinfo.getSingleUseDurationSecs();
            singleUseDurationSecsset.add(singleUseDurationSecs);
        }
        for(Usageinfo usageinfo :pclist){
            Long singleUseDurationSecs = usageinfo.getSingleUseDurationSecs();
            singleUseDurationSecsset.add(singleUseDurationSecs);
        }
        for(Usageinfo usageinfo :xiaocxlist){
            Long singleUseDurationSecs = usageinfo.getSingleUseDurationSecs();
            singleUseDurationSecsset.add(singleUseDurationSecs);
        }

        Comparator<Long> comparator = new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1.compareTo(o2);
            }
        };

        Map<Long,Long> mapdataapp = new TreeMap<Long,Long>(comparator);
        for(Long singleUseDurationSecs:singleUseDurationSecsset){
            mapdataapp.put(singleUseDurationSecs,0l);
        }
        Map<Long,Long> mapdatapc = MapDeepCopy.deepcopybylong(mapdataapp,comparator);
        Map<Long,Long> mapdataxiaocx = MapDeepCopy.deepcopybylong(mapdataapp,comparator);

        List<Map<String,Object>> datamaplist  = new ArrayList<Map<String, Object>>();
        Map<String,Object> datamap = getmapby(applist,mapdataapp,"App端");
        datamaplist.add(datamap);
        datamap = getmapby(pclist,mapdatapc,"pc端");
        datamaplist.add(datamap);
        datamap = getmapby(xiaocxlist,mapdataxiaocx,"小程序端");
        datamaplist.add(datamap);



        List<Long> xlist = new ArrayList<Long>();
        xlist.addAll(singleUseDurationSecsset);


        UsageAnalydata errorAnalydata = new UsageAnalydata();
        errorAnalydata.setXlist(xlist);
        errorAnalydata.setDatalist(datamaplist);
        String jsonstring = JSONObject.toJSONString(errorAnalydata);
        return jsonstring;
    }

    private Map<String,Object> getmapby(List<Usageinfo> usageinfolist , Map<Long,Long> mapdata, String name){
        for(Usageinfo usageinfo :usageinfolist){
            String datamapstring = usageinfo.getDatamap();
            Map<Long,Long> datamap = JSONObject.parseObject(datamapstring,Map.class);
            Set<Map.Entry<Long,Long>> entrySet = datamap.entrySet();
            for(Map.Entry<Long,Long> entry:entrySet){
                Long single = entry.getKey();
                Long cnts = entry.getValue();
                Long precnts = mapdata.get(single)==null?0l:mapdata.get(single);
                mapdata.put(single, cnts+precnts);
            }
        }
        Set<Map.Entry<Long,Long>> set = mapdata.entrySet();
        List<Long> datalist = new ArrayList<Long>();
        for(Map.Entry<Long,Long> entry:set){
            Long value = entry.getValue();
            datalist.add(value);
        }
        HashMap<String,Object> datamap = new HashMap<String,Object>();
        datamap.put("name",name);
        datamap.put("data",datalist);
        return datamap;
    }


}
