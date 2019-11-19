package com.lorettax.control;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.BrandUser;
import com.lorettax.analy.NetworkUser;
import com.lorettax.entity.NetworkAnalydata;
import com.lorettax.service.BrandService;
import com.lorettax.service.NetworkService;
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
@RequestMapping("networkControl")
@CrossOrigin
public class NetworkControl {

    @Autowired
    private NetworkService networkService;

    @RequestMapping(value = "listnewUserby",method = RequestMethod.POST)
    public String listnewUserby(HttpServletRequest req, HttpServletResponse resp){
        List<NetworkUser> list = networkService.listNetworkInfoby("lorettaxflink","20180819","20181010",null,null,null,null);
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
        for(NetworkUser networkUser :list){
            String timevalue = networkUser.getTimevalue();
            timeinitmap.put(timevalue,0l);
            timeset.add(timevalue);
        }

        Set<String> networkSet = new HashSet<String>();
        for(NetworkUser networkUser :list){
            String network = networkUser.getNetwork();
            networkSet.add(network);
        }

        Map<String,Map<String,Long>> datamap = new HashMap<String,Map<String,Long>>();
        for(String network:networkSet){
            Map<String,Long> timevaluemap = new HashMap<String,Long>();
            timevaluemap.putAll(timeinitmap);
            datamap.put(network,timevaluemap);
        }

        for(NetworkUser networkUser :list){
            String timevalue = networkUser.getTimevalue();
            long newusers = networkUser.getNewusers();
            String network = networkUser.getNetwork();
            Map<String,Long> innermap =  datamap.get(network);
            Long value = innermap.get(timevalue);
            value = value+newusers;
            innermap.put(timevalue, value);
            datamap.put(network,innermap);
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
        NetworkAnalydata networkAnalydata = new NetworkAnalydata();
        ArrayList timeArraylist = new ArrayList();
        timeArraylist.addAll(timeset);
        networkAnalydata.setXlist(timeArraylist);
        networkAnalydata.setDatalist(datalist);
        String jsonstring = JSONObject.toJSONString(networkAnalydata);
        return jsonstring;
    }

    @RequestMapping(value = "liststartupinfoby",method = RequestMethod.POST)
    public String liststartupinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<NetworkUser> list = networkService.listNetworkInfoby("lorettaxflink","20180819","20181010",null,null,null,null);
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
        for(NetworkUser networkUser :list){
            String timevalue = networkUser.getTimevalue();
            timeinitmap.put(timevalue,0l);
            timeset.add(timevalue);
        }

        Set<String> networkSet = new HashSet<String>();
        for(NetworkUser networkUser :list){
            String network = networkUser.getNetwork();
            networkSet.add(network);
        }

        Map<String,Map<String,Long>> datamap = new HashMap<String,Map<String,Long>>();
        for(String network:networkSet){
            Map<String,Long> timevaluemap = new HashMap<String,Long>();
            timevaluemap.putAll(timeinitmap);
            datamap.put(network,timevaluemap);
        }

        for(NetworkUser networkUser :list){
            String timevalue = networkUser.getTimevalue();
            long cnts = networkUser.getCnts();
            String network = networkUser.getNetwork();
            Map<String,Long> innermap =  datamap.get(network);
            Long value = innermap.get(timevalue);
            value = value+cnts;
            innermap.put(timevalue, value);
            datamap.put(network,innermap);
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
        NetworkAnalydata networkAnalydata = new NetworkAnalydata();
        ArrayList timeArraylist = new ArrayList();
        timeArraylist.addAll(timeset);
        networkAnalydata.setXlist(timeArraylist);
        networkAnalydata.setDatalist(datalist);
        String jsonstring = JSONObject.toJSONString(networkAnalydata);
        return jsonstring;
    }

    @RequestMapping(value = "listactivitieinfoby",method = RequestMethod.POST)
    public String listactivitieinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<NetworkUser> list = networkService.listNetworkInfoby("lorettaxflink","20180819","20181010",null,null,null,null);
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
        for(NetworkUser networkUser :list){
            String timevalue = networkUser.getTimevalue();
            timeinitmap.put(timevalue,0l);
            timeset.add(timevalue);
        }

        Set<String> networkSet = new HashSet<String>();
        for(NetworkUser networkUser :list){
            String network = networkUser.getNetwork();
            networkSet.add(network);
        }

        Map<String,Map<String,Long>> datamap = new HashMap<String,Map<String,Long>>();
        for(String network:networkSet){
            Map<String,Long> timevaluemap = new HashMap<String,Long>();
            timevaluemap.putAll(timeinitmap);
            datamap.put(network,timevaluemap);
        }

        for(NetworkUser networkUser :list){
            String timevalue = networkUser.getTimevalue();
            long activeusers = networkUser.getActiveusers();
            String network = networkUser.getNetwork();
            Map<String,Long> innermap =  datamap.get(network);
            Long value = innermap.get(timevalue);
            value = value+activeusers;
            innermap.put(timevalue, value);
            datamap.put(network,innermap);
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
        NetworkAnalydata networkAnalydata = new NetworkAnalydata();
        ArrayList timeArraylist = new ArrayList();
        timeArraylist.addAll(timeset);
        networkAnalydata.setXlist(timeArraylist);
        networkAnalydata.setDatalist(datalist);
        String jsonstring = JSONObject.toJSONString(networkAnalydata);
        return jsonstring;
    }


}
