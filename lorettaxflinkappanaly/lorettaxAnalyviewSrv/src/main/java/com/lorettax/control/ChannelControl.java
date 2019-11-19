package com.lorettax.control;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.Channeluser;
import com.lorettax.entity.ChannelAnalydata;
import com.lorettax.service.ChannelService;
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
@RequestMapping("channelControl")
@CrossOrigin
public class ChannelControl {

    @Autowired
    private ChannelService channelService;

    @RequestMapping(value = "listnewUserby",method = RequestMethod.POST)
    public String listnewUserby(HttpServletRequest req, HttpServletResponse resp){
        List<Channeluser> list = channelService.listChannelInfoby("lorettaxflink","20180819","20181010",null,null,null);
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
        for(Channeluser channeluser :list){
            String timevalue = channeluser.getTimevalue();
            timeinitmap.put(timevalue,0l);
            timeset.add(timevalue);
        }

        Set<String> channelSet = new HashSet<String>();
        for(Channeluser channeluser :list){
            String channel = channeluser.getAppChannel();
            channelSet.add(channel);
        }

        Map<String,Map<String,Long>> datamap = new HashMap<String,Map<String,Long>>();
        for(String channel:channelSet){
            Map<String,Long> timevaluemap = new HashMap<String,Long>();
            timevaluemap.putAll(timeinitmap);
            datamap.put(channel,timevaluemap);
        }

        for(Channeluser channeluser :list){
            String timevalue = channeluser.getTimevalue();
            long newusers = channeluser.getNewusers();
            String channel = channeluser.getAppChannel();
            Map<String,Long> innermap =  datamap.get(channel);
            Long value = innermap.get(timevalue);
            value = value+newusers;
            innermap.put(timevalue, value);
            datamap.put(channel,innermap);
        }

        ChannelAnalydata channelAnalydata = new ChannelAnalydata();
        ArrayList timeArraylist = new ArrayList();
        timeArraylist.addAll(timeset);
        channelAnalydata.setXlist(timeArraylist);
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
        ChannelAnalydata channelAnalydata1 = new ChannelAnalydata();
        channelAnalydata1.setXlist(timeArraylist);
        channelAnalydata1.setDatalist(datalist);
        String jsonstring = JSONObject.toJSONString(channelAnalydata1);
        return jsonstring;
    }

    @RequestMapping(value = "liststartupinfoby",method = RequestMethod.POST)
    public String liststartupinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<Channeluser> list = channelService.listChannelInfoby("lorettaxflink","20180819","20181010",null,null,null);
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
        for(Channeluser channeluser :list){
            String timevalue = channeluser.getTimevalue();
            timeinitmap.put(timevalue,0l);
            timeset.add(timevalue);
        }

        Set<String> channelSet = new HashSet<String>();
        for(Channeluser channeluser :list){
            String channel = channeluser.getAppChannel();
            channelSet.add(channel);
        }

        Map<String,Map<String,Long>> datamap = new HashMap<String,Map<String,Long>>();
        for(String channel:channelSet){
            Map<String,Long> timevaluemap = new HashMap<String,Long>();
            timevaluemap.putAll(timeinitmap);
            datamap.put(channel,timevaluemap);
        }

        for(Channeluser channeluser :list){
            String timevalue = channeluser.getTimevalue();
            long cnts = channeluser.getCnts();
            String channel = channeluser.getAppChannel();
            Map<String,Long> innermap =  datamap.get(channel);
            Long value = innermap.get(timevalue);
            value = value+cnts;
            innermap.put(timevalue, value);
            datamap.put(channel,innermap);
        }

        ChannelAnalydata channelAnalydata = new ChannelAnalydata();
        ArrayList timeArraylist = new ArrayList();
        timeArraylist.addAll(timeset);
        channelAnalydata.setXlist(timeArraylist);
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
        ChannelAnalydata channelAnalydata1 = new ChannelAnalydata();
        channelAnalydata1.setXlist(timeArraylist);
        channelAnalydata1.setDatalist(datalist);
        String jsonstring = JSONObject.toJSONString(channelAnalydata1);
        return jsonstring;
    }

    @RequestMapping(value = "listactivitieinfoby",method = RequestMethod.POST)
    public String listactivitieinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<Channeluser> list = channelService.listChannelInfoby("lorettaxflink","20180819","20181010",null,null,null);
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
        for(Channeluser channeluser :list){
            String timevalue = channeluser.getTimevalue();
            timeinitmap.put(timevalue,0l);
            timeset.add(timevalue);
        }

        Set<String> channelSet = new HashSet<String>();
        for(Channeluser channeluser :list){
            String channel = channeluser.getAppChannel();
            channelSet.add(channel);
        }

        Map<String,Map<String,Long>> datamap = new HashMap<String,Map<String,Long>>();
        for(String channel:channelSet){
            Map<String,Long> timevaluemap = new HashMap<String,Long>();
            timevaluemap.putAll(timeinitmap);
            datamap.put(channel,timevaluemap);
        }

        for(Channeluser channeluser :list){
            String timevalue = channeluser.getTimevalue();
            long activeusers = channeluser.getActiveusers();
            String channel = channeluser.getAppChannel();
            Map<String,Long> innermap =  datamap.get(channel);
            Long value = innermap.get(timevalue);
            value = value+activeusers;
            innermap.put(timevalue, value);
            datamap.put(channel,innermap);
        }

        ChannelAnalydata channelAnalydata = new ChannelAnalydata();
        ArrayList timeArraylist = new ArrayList();
        timeArraylist.addAll(timeset);
        channelAnalydata.setXlist(timeArraylist);
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
        ChannelAnalydata channelAnalydata1 = new ChannelAnalydata();
        channelAnalydata1.setXlist(timeArraylist);
        channelAnalydata1.setDatalist(datalist);
        String jsonstring = JSONObject.toJSONString(channelAnalydata1);
        return jsonstring;
    }


}
