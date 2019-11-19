package com.lorettax.control;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.BrandUser;
import com.lorettax.analy.NewUser;
import com.lorettax.analy.UserbehvaviorInfo;
import com.lorettax.entity.UserBehaviordata;
import com.lorettax.service.BrandService;
import com.lorettax.service.UserBehaviorService;
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
@RequestMapping("userBehaviorControl")
@CrossOrigin
public class UserBehaviorControl {

    @Autowired
    private UserBehaviorService userBehaviorService;

    @RequestMapping(value = "listuserBehaviorcntsby",method = RequestMethod.POST)
    public String listuserBehaviorcntsby(HttpServletRequest req, HttpServletResponse resp){

        String userid = req.getParameter("userid");
        //调用第三方接口查询用户的设备
        // String[] deviceIds =  deviceidsservcie.getbyiuserid(userid)
        //查询用户名
        //String name = deviceidsservcie.getnamebyuserid(userid)
        String[] deviceIds = new String[]{"devcie001","device002"};
        String username = "xiaogao";
        List<UserbehvaviorInfo> list = userBehaviorService.listUserBehaviorInfoby("lorettaxflink","20180819","20181010",null,deviceIds);

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
        for(UserbehvaviorInfo userbehvaviorInfo :list){
            String timevalue = userbehvaviorInfo.getTimevalue();
            long cnts = userbehvaviorInfo.getCnts();
            Long precnts = mapdata.get(timevalue)==null?0l:mapdata.get(timevalue);
            mapdata.put(timevalue, cnts+precnts);
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
        UserBehaviordata userBehaviordata = new UserBehaviordata();
        userBehaviordata.setXlist(xlist);
        HashMap<String,Object> datamap = new HashMap<String,Object>();
        datamap.put("name",username);
        datamap.put("data",datalist);
        List<Map<String,Object>> datamaplist  = new ArrayList<Map<String, Object>>();
        datamaplist.add(datamap);
        userBehaviordata.setDatalist(datamaplist);
        String jsonstring = JSONObject.toJSONString(userBehaviordata);
        return jsonstring;
    }

    @RequestMapping(value = "listuserBehavioractivitiesby",method = RequestMethod.POST)
    public String listuserBehavioractivitiesby(HttpServletRequest req, HttpServletResponse resp){

        String userid = req.getParameter("userid");
        //调用第三方接口查询用户的设备
        // String[] deviceIds =  deviceidsservcie.getbyiuserid(userid)
        String[] deviceIds = new String[]{"devcie001","device002"};
        List<UserbehvaviorInfo> list = userBehaviorService.listUserBehaviorInfoby("lorettaxflink","20180819","20181010",null,deviceIds);

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
        for(UserbehvaviorInfo userbehvaviorInfo :list){
            String timevalue = userbehvaviorInfo.getTimevalue();
            long activties = userbehvaviorInfo.getActivties();
            Long preactivties = mapdata.get(timevalue)==null?0l:mapdata.get(timevalue);
            mapdata.put(timevalue, activties+preactivties);
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
        UserBehaviordata userBehaviordata = new UserBehaviordata();
        userBehaviordata.setXlist(xlist);
        HashMap<String,Object> datamap = new HashMap<String,Object>();
        datamap.put("name","活跃设备数");
        datamap.put("data",datalist);
        List<Map<String,Object>> datamaplist  = new ArrayList<Map<String, Object>>();
        datamaplist.add(datamap);
        userBehaviordata.setDatalist(datamaplist);
        String jsonstring = JSONObject.toJSONString(userBehaviordata);
        return jsonstring;
    }



}
