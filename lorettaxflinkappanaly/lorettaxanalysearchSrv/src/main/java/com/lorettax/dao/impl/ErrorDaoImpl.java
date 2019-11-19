package com.lorettax.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.ErrorInfo;
import com.lorettax.analy.NewUser;
import com.lorettax.analy.Startup;
import com.lorettax.base.BaseMongo;
import com.lorettax.dao.ErrorDao;
import com.lorettax.utils.DateTimeTools;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Component
public class ErrorDaoImpl extends BaseMongo implements ErrorDao {

    @Override
    public List<List<ErrorInfo>> listErrorinfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform, String osType, String deviceStyle) {
        List<List<ErrorInfo>> result = new ArrayList<List<ErrorInfo>>();

        if (appId == null || appId.trim().isEmpty()) {
            return result;
        }

        MongoDatabase db = mongoClient.getDatabase(appId);
        if(timeUnit == null || timeUnit.trim().isEmpty()){
            timeUnit = "daily";
        }
        if("daily".equals(timeUnit)){
            MongoCollection<Document> collectionapp = db.getCollection("errorinfodailyapp");
            MongoCollection<Document> collectionpc = db.getCollection("errorinfodailypc");
            MongoCollection<Document> collectionxiaocx = db.getCollection("errorinfodailyxiaocx");
            List<ErrorInfo> listapp = listErrorinfobytablename(collectionapp,timeFrom,timeTo,appVersion,appChannel,appPlatform,osType, deviceStyle);

            List<ErrorInfo> listpc = listErrorinfobytablename(collectionpc,timeFrom,timeTo,appVersion,appChannel,appPlatform,osType, deviceStyle);
            List<ErrorInfo> listxiaocx = listErrorinfobytablename(collectionxiaocx,timeFrom,timeTo,appVersion,appChannel,appPlatform,osType, deviceStyle);
            if(listapp==null){
                listapp = new ArrayList<ErrorInfo>();
            }
            if(listpc==null){
                listpc = new ArrayList<ErrorInfo>();
            }
            if(listxiaocx==null){
                listxiaocx = new ArrayList<ErrorInfo>();
            }
            result.add(listapp);
            result.add(listpc);
            result.add(listxiaocx);
        }else{
            return result;
        }

        return result;
    }



    private List<ErrorInfo> listErrorinfobytablename(MongoCollection<Document> collection,String timeFrom,String timeTo,String appVersion, String appChannel, String appPlatform,String osType,String deviceStyle){
        List<ErrorInfo> result = new ArrayList<ErrorInfo>();

        Document matchFields = new Document();
        matchFields.put("timevalue",
                new Document().append("$gte", timeFrom)
                        .append("$lte", timeTo));
        if (appVersion != null && !appVersion.trim().isEmpty()) {
            matchFields.put("appVersion", new Document().append("$eq", appVersion));
        }
        if (appChannel != null && !appChannel.trim().isEmpty()) {
            matchFields.put("appChannel", new Document().append("$eq", appChannel));
        }

        if (appPlatform != null && !appPlatform.trim().isEmpty()) {
            matchFields.put("appPlatform", new Document().append("$eq", appPlatform));
        }
        if (osType != null && !osType.trim().isEmpty()) {
            matchFields.put("ostype", new Document().append("$eq", osType));
        }
        if (deviceStyle != null && !deviceStyle.trim().isEmpty()) {
            matchFields.put("deviceStyle", new Document().append("$eq", deviceStyle));
        }
        Document match = new Document("$match", matchFields);


        Document sort = new Document("$sort", Sorts.ascending("timeValue"));


        Document groupFields = new Document();
        Document idFields = new Document();
        idFields.put("timevalue", "$timevalue");
        idFields.put("appId", "$appId");
        if (appVersion != null && !appVersion.trim().isEmpty()) {
            idFields.put("appVersion", "$appVersion");
        }
        if (appChannel != null && !appChannel.trim().isEmpty()) {
            idFields.put("appChannel", "$appChannel");
        }

        if (appPlatform != null && !appPlatform.trim().isEmpty()) {
            idFields.put("appPlatform", "$appPlatform");
        }
        if (osType != null && !osType.trim().isEmpty()) {
            idFields.put("ostype", "$ostype");
        }
        if (deviceStyle != null && !deviceStyle.trim().isEmpty()) {
            idFields.put("deviceStyle", "$deviceStyle");
        }

        groupFields.put("_id", idFields);



        groupFields.put("cnts", new Document("$sum", "$cnts"));
        Document group = new Document("$group", groupFields);


        Document projectFields = new Document();
        projectFields.put("_id", false);
        projectFields.put("timevalue", "$_id.timevalue");
        projectFields.put("appId", "$_id.appId");
        if (appVersion != null && !appVersion.trim().isEmpty()) {
            projectFields.put("appVersion", "$_id.appVersion");
        }
        if (appChannel != null && !appChannel.trim().isEmpty()) {
            projectFields.put("appChannel", "$_id.appChannel");
        }

        if (appPlatform != null && !appPlatform.trim().isEmpty()) {
            projectFields.put("appPlatform", "$_id.appPlatform");
        }
        if (osType != null && !osType.trim().isEmpty()) {
            projectFields.put("ostype", "$_id.ostype");
        }
        if (deviceStyle != null && !deviceStyle.trim().isEmpty()) {
            projectFields.put("deviceStyle", "$_id.deviceStyle");
        }




        projectFields.put("cnts", true);
        Document project = new Document("$project", projectFields);
        AggregateIterable<Document> iterater = collection.aggregate(
                (List<Document>) Arrays.asList(match,  sort, group, project)
        );

        MongoCursor<Document> cursor = iterater.iterator();
        while(cursor.hasNext()){
            Document document = cursor.next();
            String jsonString = JSONObject.toJSONString(document);
            ErrorInfo errorInfo = JSONObject.parseObject(jsonString,ErrorInfo.class);
            result.add(errorInfo);
        }
        return result;
    }
}
