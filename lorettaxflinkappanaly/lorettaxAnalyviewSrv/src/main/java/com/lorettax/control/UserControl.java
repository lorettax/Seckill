package com.lorettax.control;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.NewUser;
import com.lorettax.entity.UserAnalydata;
import com.lorettax.service.UserService;
import com.lorettax.utils.MapDeepCopy;
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
@RequestMapping("userControl")
@CrossOrigin
public class UserControl {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "listnewUserby",method = RequestMethod.POST)
    public String listnewUserby(HttpServletRequest req, HttpServletResponse resp){
        List<List<NewUser>> list = userService.listnewuserby("lorettaxflink","20180819","20181010",null,null,null,null);
        List<NewUser> applist = list.get(0);
        List<NewUser> pclist = list.get(1);
        List<NewUser> xiaocxlist = list.get(2);
        //处理时间
        Set<String> timeset = new HashSet<String>();
        for(NewUser newUser :applist){
            String timevalue = newUser.getTimevalue();
            timeset.add(timevalue);
        }
        for(NewUser newUser :pclist){
            String timevalue = newUser.getTimevalue();
            timeset.add(timevalue);
        }
        for(NewUser newUser :xiaocxlist){
            String timevalue = newUser.getTimevalue();
            timeset.add(timevalue);
        }
        Comparator<String> comparator = new Comparator<String>() {
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
        };
        Map<String,Long> mapdataapp = new TreeMap<String,Long>(comparator);
        for(String timevalue:timeset){
            mapdataapp.put(timevalue,0l);
        }
        Map<String,Long> mapdatapc = MapDeepCopy.deepcopy(mapdataapp,comparator);
        Map<String,Long> mapdataxiaocx = MapDeepCopy.deepcopy(mapdataapp,comparator);

        List<Map<String,Object>> datamaplist  = new ArrayList<Map<String, Object>>();
        Map<String,Object> datamap = getmapby(JSONObject.toJSONString(applist),mapdataapp,"app端",NewUser.class);
        datamaplist.add(datamap);
        datamap = getmapby(JSONObject.toJSONString(pclist),mapdatapc,"pc端",NewUser.class);
        datamaplist.add(datamap);
        datamap = getmapby(JSONObject.toJSONString(xiaocxlist),mapdataxiaocx,"小程序端",NewUser.class);
        datamaplist.add(datamap);



        List<String> xlist = new ArrayList<String>();
        xlist.addAll(timeset);
        Collections.sort(xlist,comparator);

        UserAnalydata startupdata = new UserAnalydata();
        startupdata.setXlist(xlist);
        startupdata.setDatalist(datamaplist);
        String jsonstring = JSONObject.toJSONString(startupdata);
        return jsonstring;
    }

    @RequestMapping(value = "liststartupinfoby",method = RequestMethod.POST)
    public String liststartupinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<List<Startup>> list = userService.listStarupby("lorettaxflink","20180819","20181010",null,null,null,null);
        List<Startup> applist = list.get(0);
        List<Startup> pclist = list.get(1);
        List<Startup> xiaocxlist = list.get(2);
        //处理时间
        Set<String> timeset = new HashSet<String>();
        for(Startup startup :applist){
            String timevalue = startup.getTimevalue();
            timeset.add(timevalue);
        }
        for(Startup startup :pclist){
            String timevalue = startup.getTimevalue();
            timeset.add(timevalue);
        }
        for(Startup startup :xiaocxlist){
            String timevalue = startup.getTimevalue();
            timeset.add(timevalue);
        }
        Comparator<String> comparator = new Comparator<String>() {
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
        };
        Map<String,Long> mapdataapp = new TreeMap<String,Long>(comparator);
        for(String timevalue:timeset){
            mapdataapp.put(timevalue,0l);
        }
        Map<String,Long> mapdatapc = MapDeepCopy.deepcopy(mapdataapp,comparator);
        Map<String,Long> mapdataxiaocx = MapDeepCopy.deepcopy(mapdataapp,comparator);

        List<Map<String,Object>> datamaplist  = new ArrayList<Map<String, Object>>();
        Map<String,Object> datamap = getmapby(JSONObject.toJSONString(applist),mapdataapp,"App端",Startup.class);
        datamaplist.add(datamap);
        datamap = getmapby(JSONObject.toJSONString(pclist),mapdatapc,"pc端",Startup.class);
        datamaplist.add(datamap);
        datamap = getmapby(JSONObject.toJSONString(xiaocxlist),mapdataxiaocx,"小程序端",Startup.class);
        datamaplist.add(datamap);



        List<String> xlist = new ArrayList<String>();
        xlist.addAll(timeset);
        Collections.sort(xlist,comparator);


        UserAnalydata startupdata = new UserAnalydata();
        startupdata.setXlist(xlist);
        startupdata.setDatalist(datamaplist);
        String jsonstring = JSONObject.toJSONString(startupdata);
        return jsonstring;
    }

    private Map<String,Object> getmapby(String listjsonString,Map<String,Long> mapdata,String name,Class clazz){
        List<Object> list = JSONObject.parseArray(listjsonString,clazz);
        for(Object object :list){
            if(object instanceof Startup){
                Startup startup = (Startup)object;
                String timevalue = startup.getTimevalue();
                long cnts = startup.getCnts();
                Long precnts = mapdata.get(timevalue)==null?0l:mapdata.get(timevalue);
                mapdata.put(timevalue, precnts+cnts);
            }else if(object instanceof NewUser){
                NewUser newUser = (NewUser)object;
                String timevalue = newUser.getTimevalue();
                long newusers = newUser.getNewusers();
                Long prenewusers = mapdata.get(timevalue)==null?0l:mapdata.get(timevalue);
                mapdata.put(timevalue, prenewusers+newusers);
            }else  if (object instanceof ActivitieUser){
                ActivitieUser activitieUser = (ActivitieUser)object;
                String timevalue = activitieUser.getTimevalue();
                long activtieusers = activitieUser.getActivtieusers();
                Long preactivtieusers = mapdata.get(timevalue)==null?0l:mapdata.get(timevalue);
                mapdata.put(timevalue, preactivtieusers+activtieusers);
            }

        }
        Set<Map.Entry<String,Long>> set = mapdata.entrySet();
        List<Long> datalist = new ArrayList<Long>();
        for(Map.Entry<String,Long> entry:set){
            Long value = entry.getValue();
            datalist.add(value);
        }
        HashMap<String,Object> datamap = new HashMap<String,Object>();
        datamap.put("name",name);
        datamap.put("data",datalist);
        return datamap;
    }

    @RequestMapping(value = "listactivitieinfoby",method = RequestMethod.POST)
    public String listactivitieinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<List<ActivitieUser>> list = userService.listActivitieby("lorettaxflink","20180819","20181010",null,null,null,null);
        List<ActivitieUser> applist = list.get(0);
        List<ActivitieUser> pclist = list.get(1);
        List<ActivitieUser> xiaocxlist = list.get(2);
        //处理时间
        Set<String> timeset = new HashSet<String>();
        for(ActivitieUser activitieUser :applist){
            String timevalue = activitieUser.getTimevalue();
            timeset.add(timevalue);
        }
        for(ActivitieUser activitieUser :pclist){
            String timevalue = activitieUser.getTimevalue();
            timeset.add(timevalue);
        }
        for(ActivitieUser activitieUser :xiaocxlist){
            String timevalue = activitieUser.getTimevalue();
            timeset.add(timevalue);
        }
        Comparator<String> comparator = new Comparator<String>() {
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
        };
        Map<String,Long> mapdataapp = new TreeMap<String,Long>(comparator);
        for(String timevalue:timeset){
            mapdataapp.put(timevalue,0l);
        }
        Map<String,Long> mapdatapc = MapDeepCopy.deepcopy(mapdataapp,comparator);
        Map<String,Long> mapdataxiaocx = MapDeepCopy.deepcopy(mapdataapp,comparator);

        List<Map<String,Object>> datamaplist  = new ArrayList<Map<String, Object>>();
        Map<String,Object> datamap = getmapby(JSONObject.toJSONString(applist),mapdataapp,"App端",ActivitieUser.class);
        datamaplist.add(datamap);
        datamap = getmapby(JSONObject.toJSONString(pclist),mapdatapc,"pc端",ActivitieUser.class);
        datamaplist.add(datamap);
        datamap = getmapby(JSONObject.toJSONString(xiaocxlist),mapdataxiaocx,"小程序端",ActivitieUser.class);
        datamaplist.add(datamap);



        List<String> xlist = new ArrayList<String>();
        xlist.addAll(timeset);
        Collections.sort(xlist,comparator);

        UserAnalydata startupdata = new UserAnalydata();
        startupdata.setXlist(xlist);
        startupdata.setDatalist(datamaplist);
        String jsonstring = JSONObject.toJSONString(startupdata);
        return jsonstring;
    }


}
