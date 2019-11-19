package com.lorettax.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.Startup;
import com.lorettax.base.BaseMongo;
import com.lorettax.dao.UserDao;
import com.lorettax.analy.NewUser;
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
public class UserDaoImpl extends BaseMongo implements UserDao {
    public List<List<NewUser>> listnewuserby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform) {
        List<List<NewUser>> result = new ArrayList<List<NewUser>>();

        if (appId == null || appId.trim().isEmpty()) {
            return result;
        }

        MongoDatabase db = mongoClient.getDatabase(appId);
        if(timeUnit == null || timeUnit.trim().isEmpty()){
            timeUnit = "daily";
        }
        if("daily".equals(timeUnit)){
            MongoCollection<Document> collectionapp = db.getCollection("newuserinfodailyapp");
            MongoCollection<Document> collectionpc = db.getCollection("newuserinfodailypc");
            MongoCollection<Document> collectionxiaocx = db.getCollection("newuserinfodailyxiaocx");
            List<NewUser> listapp = listNewUserbytablename(collectionapp,timeFrom,timeTo,appVersion,appChannel,appPlatform);

            List<NewUser> listpc = listNewUserbytablename(collectionpc,timeFrom,timeTo,appVersion,appChannel,appPlatform);
            List<NewUser> listxiaocx = listNewUserbytablename(collectionxiaocx,timeFrom,timeTo,appVersion,appChannel,appPlatform);
            if(listapp==null){
                listapp = new ArrayList<NewUser>();
            }
            if(listpc==null){
                listpc = new ArrayList<NewUser>();
            }
            if(listxiaocx==null){
                listxiaocx = new ArrayList<NewUser>();
            }
            result.add(listapp);
            result.add(listpc);
            result.add(listxiaocx);
        }else{
            return result;
        }

        return result;
    }

    private List<NewUser> listNewUserbytablename(MongoCollection<Document> collection,String timeFrom,String timeTo,String appVersion, String appChannel, String appPlatform){
        List<NewUser> result = new ArrayList<NewUser>();

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

        groupFields.put("_id", idFields);



        groupFields.put("newusers", new Document("$sum", "$newusers"));
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



        projectFields.put("newusers", true);
        Document project = new Document("$project", projectFields);
        AggregateIterable<Document> iterater = collection.aggregate(
                (List<Document>) Arrays.asList(match,  sort, group, project)
        );

        MongoCursor<Document> cursor = iterater.iterator();
        while(cursor.hasNext()){
            Document document = cursor.next();
            String jsonString = JSONObject.toJSONString(document);
            NewUser newUser = JSONObject.parseObject(jsonString,NewUser.class);
            result.add(newUser);
        }
        return result;
    }


    public List<List<Startup>> listStarupby(String appId,
                                      String timeFrom, String timeTo,
                                      String timeUnit, String appVersion,
                                      String appChannel,
                                      String appPlatform){
        List<List<Startup>> result = new ArrayList<List<Startup>>();

        if (appId == null || appId.trim().isEmpty()) {
            return result;
        }

        MongoDatabase db = mongoClient.getDatabase(appId);
        if(timeUnit == null || timeUnit.trim().isEmpty()){
            timeUnit = "daily";
        }
        if("daily".equals(timeUnit)){
            MongoCollection<Document> collectionapp = db.getCollection("userstartupinfodailyapp");
            MongoCollection<Document> collectionpc = db.getCollection("userstartupinfodailypc");
            MongoCollection<Document> collectionxiaocx = db.getCollection("userstartupinfodailyxiaocx");
            List<Startup> listapp = liststartupbytablename(collectionapp,timeFrom,timeTo,appVersion,appChannel,appPlatform);

            List<Startup> listpc = liststartupbytablename(collectionpc,timeFrom,timeTo,appVersion,appChannel,appPlatform);
            List<Startup> listxiaocx = liststartupbytablename(collectionxiaocx,timeFrom,timeTo,appVersion,appChannel,appPlatform);
            if(listapp==null){
                listapp = new ArrayList<Startup>();
            }
            if(listpc==null){
                listpc = new ArrayList<Startup>();
            }
            if(listxiaocx==null){
                listxiaocx = new ArrayList<Startup>();
            }
            result.add(listapp);
            result.add(listpc);
            result.add(listxiaocx);
        }else{
            return result;
        }

        return result;
    }

    private List<Startup> liststartupbytablename(MongoCollection<Document> collection,String timeFrom,String timeTo,String appVersion, String appChannel, String appPlatform){
        List<Startup> result = new ArrayList<Startup>();

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



        projectFields.put("cnts", true);
        Document project = new Document("$project", projectFields);
        AggregateIterable<Document> iterater = collection.aggregate(
                (List<Document>) Arrays.asList(match,  sort, group, project)
        );

        MongoCursor<Document> cursor = iterater.iterator();
        while(cursor.hasNext()){
            Document document = cursor.next();
            String jsonString = JSONObject.toJSONString(document);
            Startup startup = JSONObject.parseObject(jsonString,Startup.class);
            result.add(startup);
        }
        return result;
    }

    @Override
    public List<List<ActivitieUser>> listActivitieby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform) {
        List<List<ActivitieUser>> result = new ArrayList<List<ActivitieUser>>();

        if (appId == null || appId.trim().isEmpty()) {
            return result;
        }

        MongoDatabase db = mongoClient.getDatabase(appId);
        if(timeUnit == null || timeUnit.trim().isEmpty()){
            timeUnit = "daily";
        }
        if("daily".equals(timeUnit)){
            MongoCollection<Document> collectionapp = db.getCollection("useractivtieinfodailyapp");
            MongoCollection<Document> collectionpc = db.getCollection("useractivtieinfodailypc");
            MongoCollection<Document> collectionxiaocx = db.getCollection("useractivtieinfodailyxiaocx");
            List<ActivitieUser> listapp = listActivitieUserbytablename(collectionapp,timeFrom,timeTo,appVersion,appChannel,appPlatform);

            List<ActivitieUser> listpc = listActivitieUserbytablename(collectionpc,timeFrom,timeTo,appVersion,appChannel,appPlatform);
            List<ActivitieUser> listxiaocx = listActivitieUserbytablename(collectionxiaocx,timeFrom,timeTo,appVersion,appChannel,appPlatform);
            if(listapp==null){
                listapp = new ArrayList<ActivitieUser>();
            }
            if(listpc==null){
                listpc = new ArrayList<ActivitieUser>();
            }
            if(listxiaocx==null){
                listxiaocx = new ArrayList<ActivitieUser>();
            }
            result.add(listapp);
            result.add(listpc);
            result.add(listxiaocx);
        }else{
            return result;
        }

        return result;
    }

    private List<ActivitieUser> listActivitieUserbytablename(MongoCollection<Document> collection,String timeFrom,String timeTo,String appVersion, String appChannel, String appPlatform){
        List<ActivitieUser> result = new ArrayList<ActivitieUser>();

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

        groupFields.put("_id", idFields);



        groupFields.put("activtieusers", new Document("$sum", "$activtieusers"));
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



        projectFields.put("activtieusers", true);
        Document project = new Document("$project", projectFields);
        AggregateIterable<Document> iterater = collection.aggregate(
                (List<Document>) Arrays.asList(match,  sort, group, project)
        );

        MongoCursor<Document> cursor = iterater.iterator();
        while(cursor.hasNext()){
            Document document = cursor.next();
            String jsonString = JSONObject.toJSONString(document);
            ActivitieUser activitieUser = JSONObject.parseObject(jsonString,ActivitieUser.class);
            result.add(activitieUser);
        }
        return result;
    }
}
