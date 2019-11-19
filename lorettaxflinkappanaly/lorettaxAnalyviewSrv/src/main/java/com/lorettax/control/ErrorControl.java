package com.lorettax.control;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.analy.ErrorInfo;
import com.lorettax.entity.ErrorAnalydata;
import com.lorettax.service.ErrorService;
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
@RequestMapping("errorControl")
@CrossOrigin
public class ErrorControl {

    @Autowired
    private ErrorService errorService;

    @RequestMapping(value = "listerrorinfoby",method = RequestMethod.POST)
    public String listerrorinfoby(HttpServletRequest req, HttpServletResponse resp){
        List<List<ErrorInfo>> list = errorService.listErrorinfoby("lorettaxflink","20180819","20181010",null,null,null,null,null,null);
        List<ErrorInfo> applist = list.get(0);
        List<ErrorInfo> pclist = list.get(1);
        List<ErrorInfo> xiaocxlist = list.get(2);
        //处理时间
        Set<String> timeset = new HashSet<String>();
        for(ErrorInfo errorInfo :applist){
            String timevalue = errorInfo.getTimevalue();
            timeset.add(timevalue);
        }
        for(ErrorInfo errorInfo  :pclist){
            String timevalue = errorInfo.getTimevalue();
            timeset.add(timevalue);
        }
        for(ErrorInfo errorInfo :xiaocxlist){
            String timevalue = errorInfo.getTimevalue();
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
        Map<String,Object> datamap = getmapby(applist,mapdataapp,"App端");
        datamaplist.add(datamap);
        datamap = getmapby(pclist,mapdatapc,"pc端");
        datamaplist.add(datamap);
        datamap = getmapby(xiaocxlist,mapdataxiaocx,"小程序端");
        datamaplist.add(datamap);



        List<String> xlist = new ArrayList<String>();
        xlist.addAll(timeset);


        ErrorAnalydata errorAnalydata = new ErrorAnalydata();
        errorAnalydata.setXlist(xlist);
        errorAnalydata.setDatalist(datamaplist);
        String jsonstring = JSONObject.toJSONString(errorAnalydata);
        return jsonstring;
    }

    private Map<String,Object> getmapby(List<ErrorInfo> errorInfolist ,Map<String,Long> mapdata,String name){
        for(ErrorInfo errorInfo :errorInfolist){
                String timevalue = errorInfo.getTimevalue();
                long cnts = errorInfo.getCnts();
                Long precnts = mapdata.get(timevalue)==null?0l:mapdata.get(timevalue);
                mapdata.put(timevalue, precnts+cnts);
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
}
