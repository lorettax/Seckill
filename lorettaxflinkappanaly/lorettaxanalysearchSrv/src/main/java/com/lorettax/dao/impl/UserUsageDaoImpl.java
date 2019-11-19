package com.lorettax.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.lorettax.analy.*;
import com.lorettax.base.BaseMongo;
import com.lorettax.dao.UserUsageDao;
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
public class UserUsageDaoImpl extends BaseMongo implements UserUsageDao {

    @Override
    public List<List<Usageinfo>> listUsageinfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform) {
        List<List<Usageinfo>> result = new ArrayList<List<Usageinfo>>();

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
            List<Usageinfo> listapp = listUsageinfobytablename(collectionapp,timeFrom,timeTo,appVersion,appChannel,appPlatform);

            List<Usageinfo> listpc = listUsageinfobytablename(collectionpc,timeFrom,timeTo,appVersion,appChannel,appPlatform);
            List<Usageinfo> listxiaocx = listUsageinfobytablename(collectionxiaocx,timeFrom,timeTo,appVersion,appChannel,appPlatform);
            if(listapp==null){
                listapp = new ArrayList<Usageinfo>();
            }
            if(listpc==null){
                listpc = new ArrayList<Usageinfo>();
            }
            if(listxiaocx==null){
                listxiaocx = new ArrayList<Usageinfo>();
            }
            result.add(listapp);
            result.add(listpc);
            result.add(listxiaocx);
        }else{
            return result;
        }

        return result;
    }


    private List<Usageinfo> listUsageinfobytablename(MongoCollection<Document> collection, String timeFrom, String timeTo, String appVersion, String appChannel, String appPlatform){
        List<Usageinfo> result = new ArrayList<Usageinfo>();

        Document matchFields = new Document();
        matchFields.put("timeValue",
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
        Document match = new Document("$match", matchFields);


        Document sort = new Document("$sort", Sorts.ascending("timeValue"));


        Document groupFields = new Document();
        Document idFields = new Document();
        idFields.put("timeValue", "$timeValue");
        idFields.put("appId", "$appId");
        idFields.put("datamap","$datamap");
        if (appVersion != null && !appVersion.trim().isEmpty()) {
            idFields.put("appVersion", "$appVersion");
        }
        if (appChannel != null && !appChannel.trim().isEmpty()) {
            idFields.put("appChannel", "$appChannel");
        }

        if (appPlatform != null && !appPlatform.trim().isEmpty()) {
            idFields.put("appPlatform", "$appPlatform");
        }

        groupFields.put("_id", idFields);
        Document group = new Document("$group", groupFields);


        Document projectFields = new Document();
        projectFields.put("_id", false);
        projectFields.put("timevalue", "$_id.timevalue");
        projectFields.put("appId", "$_id.appId");
        projectFields.put("datamap", "$_id.datamap");
        if (appVersion != null && !appVersion.trim().isEmpty()) {
            projectFields.put("appVersion", "$_id.appVersion");
        }
        if (appChannel != null && !appChannel.trim().isEmpty()) {
            projectFields.put("appChannel", "$_id.appChannel");
        }

        if (appPlatform != null && !appPlatform.trim().isEmpty()) {
            projectFields.put("appPlatform", "$_id.appPlatform");
        }

        Document project = new Document("$project", projectFields);
        AggregateIterable<Document> iterater = collection.aggregate(
                (List<Document>) Arrays.asList(match,  sort, group, project)
        );

        MongoCursor<Document> cursor = iterater.iterator();
        while(cursor.hasNext()){
            Document document = cursor.next();
            String jsonString = JSONObject.toJSONString(document);
            Usageinfo usageinfo = JSONObject.parseObject(jsonString,Usageinfo.class);
            result.add(usageinfo);
        }
        return result;
    }


}
