package com.lorettax.control;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Channeluser;
import com.lorettax.analy.Versionuser;
import com.lorettax.entity.VersionAnalydata;
import com.lorettax.service.ChannelService;
import com.lorettax.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by li on 2018/12/16.
 */
@RestController
@RequestMapping("versionControl")
@CrossOrigin
public class VersionControl {

    @Autowired
    private VersionService versionService;

    @RequestMapping(value = "listnewUserby",method = RequestMethod.POST)
    public String listnewUserby(HttpServletRequest req, HttpServletResponse resp){
        List<Versionuser> list = versionService.listVersionInfoby("lorettaxflink","20180819","20181010",null,null,null);
        Map<String,Long> mapdata = new TreeMap<String,Long>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Long o11 = 0l;

                Long o22 =0l;
                try {
                    o11 = dateFormat.parse(o1).getTime();

                    o22 = dateFormat.parse(o2).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return o11.compareTo(o22);
            }
        });
        Map<String,Long> timeinitmap = new HashMap<String,Long>();
        Set<String> timeset = new HashSet<String>();
        for(Versionuser versionuser :list){
            String timevalue = versionuser.getTimevalue();
            timeinitmap.put(timevalue,0l);
            timeset.add(timevalue);
        }

        Set<String> appversionSet = new HashSet<String>();
        for(Versionuser versionuser :list){
            String appVersion = versionuser.getAppVersion();
            appversionSet.add(appVersion);
        }

        Map<String,Map<String,Long>> datamap = new HashMap<String,Map<String,Long>>();
        for(String appversion:appversionSet){
            Map<String,Long> timevaluemap = new HashMap<String,Long>();
            timevaluemap.putAll(timeinitmap);
            datamap.put(appversion,timevaluemap);
        }

        for(Versionuser versionuser :list){
            String timevalue = versionuser.getTimevalue();
            long newusers = versionuser.getNewusers();
            String appversion = versionuser.getAppVersion();
            Map<String,Long> innermap =  datamap.get(appversion);
            Long value = innermap.get(timevalue);
            value = value+newusers;
            innermap.put(timevalue, value);
            datamap.put(appversion,innermap);
        }


        HashMap<String,Object> datamapfinal = new HashMap<String,Object>();
        Set<Map.Entry<String,Map<String,Long>>> setdata = datamap.entrySet();
        List<Map<String,Object>> datalist = new ArrayList<Map<String,Object>>();
        for(Map.Entry<String,Map<String,Long>> entry : setdata){
            String channel = entry.getKey();
            Map<String,Long> map = entry.getValue();
            datamapfinal.put("name",channel);
            datamapfinal.put("data",map.values());
            datalist.add(datamapfinal);
        }
        VersionAnalydata versionAnalydata = new VersionAnalydata();
        ArrayList timeArraylist = new ArrayList();
        timeArraylist.addAll(timeset);
        versionAnalydata.setXlist(timeArraylist);
        versionAnalydata.setDatalist(datalist);
        String jsonstring = JSONObject.toJSONString(versionAnalydata);
        return jsonstring;
    }

    @RequestMapping(value = "liststartupinfoby",method = RequestMethod.POST)
    public String liststartupinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<Versionuser> list = versionService.listVersionInfoby("lorettaxflink","20180819","20181010",null,null,null);
        Map<String,Long> mapdata = new TreeMap<String,Long>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Long o11 = 0l;

                Long o22 =0l;
                try {
                    o11 = dateFormat.parse(o1).getTime();

                    o22 = dateFormat.parse(o2).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return o11.compareTo(o22);
            }
        });
        Map<String,Long> timeinitmap = new HashMap<String,Long>();
        Set<String> timeset = new HashSet<String>();
        for(Versionuser versionuser :list){
            String timevalue = versionuser.getTimevalue();
            timeinitmap.put(timevalue,0l);
            timeset.add(timevalue);
        }

        Set<String> appversionSet = new HashSet<String>();
        for(Versionuser versionuser :list){
            String appVersion = versionuser.getAppVersion();
            appversionSet.add(appVersion);
        }

        Map<String,Map<String,Long>> datamap = new HashMap<String,Map<String,Long>>();
        for(String appversion:appversionSet){
            Map<String,Long> timevaluemap = new HashMap<String,Long>();
            timevaluemap.putAll(timeinitmap);
            datamap.put(appversion,timevaluemap);
        }

        for(Versionuser versionuser :list){
            String timevalue = versionuser.getTimevalue();
            long cnts = versionuser.getCnts();
            String appversion = versionuser.getAppVersion();
            Map<String,Long> innermap =  datamap.get(appversion);
            Long value = innermap.get(timevalue);
            value = value+cnts;
            innermap.put(timevalue, value);
            datamap.put(appversion,innermap);
        }


        HashMap<String,Object> datamapfinal = new HashMap<String,Object>();
        Set<Map.Entry<String,Map<String,Long>>> setdata = datamap.entrySet();
        List<Map<String,Object>> datalist = new ArrayList<Map<String,Object>>();
        for(Map.Entry<String,Map<String,Long>> entry : setdata){
            String channel = entry.getKey();
            Map<String,Long> map = entry.getValue();
            datamapfinal.put("name",channel);
            datamapfinal.put("data",map.values());
            datalist.add(datamapfinal);
        }
        VersionAnalydata versionAnalydata = new VersionAnalydata();
        ArrayList timeArraylist = new ArrayList();
        timeArraylist.addAll(timeset);
        versionAnalydata.setXlist(timeArraylist);
        versionAnalydata.setDatalist(datalist);
        String jsonstring = JSONObject.toJSONString(versionAnalydata);
        return jsonstring;
    }

    @RequestMapping(value = "listactivitieinfoby",method = RequestMethod.POST)
    public String listactivitieinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<Versionuser> list = versionService.listVersionInfoby("lorettaxflink","20180819","20181010",null,null,null);
        Map<String,Long> mapdata = new TreeMap<String,Long>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Long o11 = 0l;

                Long o22 =0l;
                try {
                    o11 = dateFormat.parse(o1).getTime();

                    o22 = dateFormat.parse(o2).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return o11.compareTo(o22);
            }
        });
        Map<String,Long> timeinitmap = new HashMap<String,Long>();
        Set<String> timeset = new HashSet<String>();
        for(Versionuser versionuser :list){
            String timevalue = versionuser.getTimevalue();
            timeinitmap.put(timevalue,0l);
            timeset.add(timevalue);
        }

        Set<String> appversionSet = new HashSet<String>();
        for(Versionuser versionuser :list){
            String appVersion = versionuser.getAppVersion();
            appversionSet.add(appVersion);
        }

        Map<String,Map<String,Long>> datamap = new HashMap<String,Map<String,Long>>();
        for(String appversion:appversionSet){
            Map<String,Long> timevaluemap = new HashMap<String,Long>();
            timevaluemap.putAll(timeinitmap);
            datamap.put(appversion,timevaluemap);
        }

        for(Versionuser versionuser :list){
            String timevalue = versionuser.getTimevalue();
            long activeusers = versionuser.getActiveusers();
            String appversion = versionuser.getAppVersion();
            Map<String,Long> innermap =  datamap.get(appversion);
            Long value = innermap.get(timevalue);
            value = value+activeusers;
            innermap.put(timevalue, value);
            datamap.put(appversion,innermap);
        }


        HashMap<String,Object> datamapfinal = new HashMap<String,Object>();
        Set<Map.Entry<String,Map<String,Long>>> setdata = datamap.entrySet();
        List<Map<String,Object>> datalist = new ArrayList<Map<String,Object>>();
        for(Map.Entry<String,Map<String,Long>> entry : setdata){
            String channel = entry.getKey();
            Map<String,Long> map = entry.getValue();
            datamapfinal.put("name",channel);
            datamapfinal.put("data",map.values());
            datalist.add(datamapfinal);
        }
        VersionAnalydata versionAnalydata = new VersionAnalydata();
        ArrayList timeArraylist = new ArrayList();
        timeArraylist.addAll(timeset);
        versionAnalydata.setXlist(timeArraylist);
        versionAnalydata.setDatalist(datalist);
        String jsonstring = JSONObject.toJSONString(versionAnalydata);
        return jsonstring;
    }


}
