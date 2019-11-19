package com.lorettax.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.lorettax.analy.BrandUser;
import com.lorettax.analy.Versionuser;
import com.lorettax.base.BaseMongo;
import com.lorettax.dao.BrandDao;
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
public class BrandDaoImpl extends BaseMongo implements BrandDao {


    @Override
    public List<BrandUser> listBrandInfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appChannel, String appVersion, String appPlatform) {
        List<BrandUser> result = new ArrayList<BrandUser>();

        if (appId == null || appId.trim().isEmpty()) {
            return result;
        }

        MongoDatabase db = mongoClient.getDatabase(appId);
        MongoCollection<Document> collection =  null;
        if(timeUnit == null || timeUnit.trim().isEmpty()){
            timeUnit = "daily";
        }
        if("daily".equals(timeUnit)){
            collection = db.getCollection("branduserinfodaily");
        }else{
            return result;
        }

        Long timeNow = System.currentTimeMillis();

        if(timeFrom==null || timeFrom.trim().isEmpty()) {
            timeFrom = DateTimeTools.getstrbyday(timeNow - (long)7*24*3600*1000);
        }
        if(timeTo==null || timeTo.trim().isEmpty()) {
            timeTo = DateTimeTools.getstrbyday(timeNow);
        }

        Document matchFields = new Document();
        matchFields.put("timevalue",
                new Document().append("$gte", timeFrom)
                        .append("$lte", timeTo));
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
        if (appChannel != null && !appChannel.trim().isEmpty()) {
            idFields.put("appChannel", "$appChannel");
        }

        if (appPlatform != null && !appPlatform.trim().isEmpty()) {
            idFields.put("appPlatform", "$appPlatform");
        }

        groupFields.put("_id", idFields);

        groupFields.put("cnts", new Document("$sum", "$cnts"));
        groupFields.put("activeusers", new Document("$sum", "$activeusers"));
        groupFields.put("newusers", new Document("$sum", "$newusers"));
        Document group = new Document("$group", groupFields);


        Document projectFields = new Document();
        projectFields.put("_id", false);
        projectFields.put("timevalue", "$_id.timevalue");
        projectFields.put("appId", "$_id.appId");
        if (appChannel != null && !appChannel.trim().isEmpty()) {
            projectFields.put("appChannel", "$_id.appChannel");
        }

        if (appPlatform != null && !appPlatform.trim().isEmpty()) {
            projectFields.put("appPlatform", "$_id.appPlatform");
        }



        projectFields.put("cnts", true);
        projectFields.put("activeusers", true);
        projectFields.put("newusers", true);
        Document project = new Document("$project", projectFields);
        AggregateIterable<Document> iterater = collection.aggregate(
                (List<Document>) Arrays.asList(match,  sort, group, project)
        );

        MongoCursor<Document> cursor = iterater.iterator();
        while(cursor.hasNext()){
            Document document = cursor.next();
            String jsonString = JSONObject.toJSONString(document);
            BrandUser brandUser = JSONObject.parseObject(jsonString,BrandUser.class);
            result.add(brandUser);
        }
        return result;
    }
}
