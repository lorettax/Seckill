package com.lorettax.analy.dao;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.lorettax.utils.Readporperities;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * Created by li on 2018/12/16.
 */
public class ChanneluserDao implements Serializable{
    private static MongoClient mongoClient = new MongoClient(Readporperities.getKey("mongoaddr"),Integer.valueOf(Readporperities.getKey("mongoport")));



    public static Document findoneby(String tablename, String appId, String appChannel,
                              String appPlatform, String timeValue){
        MongoDatabase mongoDatabase = mongoClient.getDatabase(appId);
        MongoCollection mongoCollection = mongoDatabase.getCollection(tablename);
        Document  doc = new Document();
        doc.put("appId", appId);
        doc.put("appChannel", appChannel);
        doc.put("appPlatform", appPlatform);
        doc.put("timeValue", timeValue);
        FindIterable<Document> itrer = mongoCollection.find(doc);
        MongoCursor<Document> mongocursor = itrer.iterator();
        if(mongocursor.hasNext()){
            return mongocursor.next();
        }else{
            return null;
        }
    }


    public static void saveorupdatemongo(String tablename, Document doc) {
        String appId = doc.getString("appId");
        MongoDatabase mongoDatabase = mongoClient.getDatabase(appId);
        MongoCollection<Document> mongocollection = mongoDatabase.getCollection(tablename);
        if(!doc.containsKey("_id")){
            ObjectId objectid = new ObjectId();
            doc.put("_id", objectid);
            mongocollection.insertOne(doc);
            return;
        }
        Document matchDocument = new Document();
        String objectid = doc.get("_id").toString();
        matchDocument.put("_id", new ObjectId(objectid));
        FindIterable<Document> findIterable =  mongocollection.find(matchDocument);
        if(findIterable.iterator().hasNext()){
            mongocollection.updateOne(matchDocument, new Document("$set",doc));
            try {
                System.out.println("come into saveorupdatemongo ---- update---"+ JSONObject.toJSONString(doc));
            } catch (Exception e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            mongocollection.insertOne(doc);
            try {
                System.out.println("come into saveorupdatemongo ---- insert---"+JSONObject.toJSONString(doc));
            }catch (Exception e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
