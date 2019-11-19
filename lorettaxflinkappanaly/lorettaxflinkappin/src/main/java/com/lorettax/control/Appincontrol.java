package com.lorettax.control;

import com.alibaba.fastjson.JSONObject;
import com.lorettax.dao.WidgetDao;
import com.lorettax.entity.Widget;
import com.lorettax.logIn.ErrorInfoLog;
import com.lorettax.logIn.StartupLog;
import com.lorettax.logIn.UsageInfoLog;
import com.lorettax.message.KafkaUtils;
import com.lorettax.util.Readporperities;
import com.lorettax.utils.AreabyipUtil;
import com.lorettax.utils.DateTimeTools;
import com.lorettax.utils.HttpUtil;
import com.lorettax.utils.IpUtil;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by li on 2018/12/11.
 */
@Controller
@RequestMapping("appin")
public class Appincontrol {
    private WidgetDao widgetDao = new WidgetDao() ;
    private String startuptopic = Readporperities.getKey("startuptopic");
    private String errorinfotopic = Readporperities.getKey("errorinfotopic");
    private String usageinfotopic = Readporperities.getKey("usageinfotopic");

    /**
     * 启动行为日志
     * @param startupinfo
     * @param req
     * @param resp
     */
    @RequestMapping(value = "startupinfo",method = RequestMethod.POST)
    public void startupinfo(@RequestBody String startupinfo, HttpServletRequest req, HttpServletResponse resp){
        PrintWriter printWriter = HttpUtil.getprintWrite(resp);
        StartupLog startupLog =  JSONObject.parseObject(startupinfo,StartupLog.class);
        String ipstring = IpUtil.getIpAddress(req);
        String area = AreabyipUtil.getAddressByIp(ipstring);
        String []areaarray = area.split("==");
        String country = areaarray[0];
        String provice = areaarray[1];
        String city = areaarray[2];
        startupLog.setCountry(country);
        startupLog.setProvince(provice);
        startupLog.setCity(city);
        Widget widget = new Widget();
        Document widgetdoc = widgetDao.findBy(startupLog.getAppId(),startupLog.getDeviceId());
        if(widgetdoc ==null){
            widget.setAppId(startupLog.getAppId());
            widget.setAppPlatform(startupLog.getAppPlatform());
            widget.setDeviceId(startupLog.getDeviceId());
            widget.setFirstVisitAtMs(startupLog.getCreatedAtMs());
            widget.setLastVisitAtMs(startupLog.getCreatedAtMs());
            widget.setLastVistAtMsToday(startupLog.getCreatedAtMs());
        }else {
            Object objectid  = widgetdoc.get("_id");
            String documentstring = JSONObject.toJSONString(widgetdoc);
            widget = JSONObject.parseObject(documentstring,Widget.class);
            widget.setId(objectid.toString());
        }
        setupstaruplog( widget,startupLog);
        saveOrUpdateWidget(widget,startupLog);
        String startupLogstring = JSONObject.toJSONString(startupLog);
        new KafkaUtils().sendMessage(startuptopic,startupLogstring);
        resp.setStatus(HttpStatus.OK.value());
        printWriter.write("success");
        HttpUtil.closeStream(printWriter);
        System.out.println(startupLog);
    }

    private void setupstaruplog(Widget widget, StartupLog startupLog){
        Long firstVisitAtMs = widget.getFirstVisitAtMs();
        Long timeNowMs = startupLog.getCreatedAtMs();
//新增用户
        if (timeNowMs == firstVisitAtMs) {
            startupLog.setNewUser(true);
        }
//活跃用户
        if (widget.getLastVistAtMsToday() == null
                || widget.getLastVistAtMsToday() < DateTimeTools.getThisDayStartedAtMs(timeNowMs)) {
            startupLog.setFirstVisitToday(true);
        }

    }

    private void saveOrUpdateWidget(Widget widget, StartupLog startupLog){
        widget.setLastVisitAtMs(startupLog.getCreatedAtMs());
        if (startupLog.getFirstVisitToday()) {
            widget.setLastVistAtMsToday(startupLog.getCreatedAtMs());
        }
        Document widgetDoc = null;
        try {
            widgetDoc = Document.parse(JSONObject.toJSONString(widget));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(widget.getId()==null){
            widgetDoc.put("_id", new ObjectId());
        }else {
            widgetDoc.put("_id", new ObjectId(widget.getId()));
        }

        widgetDao.saveOrUpdate(widgetDoc);

    }


    /**
     * 错误行为日志
     * @param errorinfo
     * @param req
     * @param resp
     */
    @RequestMapping(value = "errorinfo",method = RequestMethod.POST)
    public void errorinfo(@RequestBody String errorinfo, HttpServletRequest req, HttpServletResponse resp){
        PrintWriter printWriter = HttpUtil.getprintWrite(resp);
        ErrorInfoLog errorInfoLog =  JSONObject.parseObject(errorinfo,ErrorInfoLog.class);
        resp.setStatus(HttpStatus.OK.value());
        printWriter.write("success");
        HttpUtil.closeStream(printWriter);
        new KafkaUtils().sendMessage(errorinfotopic,errorinfo);
        System.out.println(errorInfoLog);
    }

    /**
     * 使用时长行为日志
     * @param uasgeinfo
     * @param req
     * @param resp
     */
    @RequestMapping(value = "usasgeinfo",method = RequestMethod.POST)
    public void usasgeinfo(@RequestBody String uasgeinfo, HttpServletRequest req, HttpServletResponse resp){
        PrintWriter printWriter = HttpUtil.getprintWrite(resp);
        UsageInfoLog usageInfoLog =  JSONObject.parseObject(uasgeinfo,UsageInfoLog.class);
        resp.setStatus(HttpStatus.OK.value());
        printWriter.write("success");
        HttpUtil.closeStream(printWriter);
        new KafkaUtils().sendMessage(usageinfotopic,uasgeinfo);
        System.out.println(usageInfoLog);
    }
}
