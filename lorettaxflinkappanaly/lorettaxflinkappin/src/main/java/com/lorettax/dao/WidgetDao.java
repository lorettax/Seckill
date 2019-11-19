package com.lorettax.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.lorettax.util.Readporperities;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by li on 2018/12/16.
 */
public class WidgetDao {
    private MongoClient mongoClient = new MongoClient(Readporperities.getKey("mongoaddr"),Integer.valueOf(Readporperities.getKey("mongoport")));

    public WidgetDao(){
        System.out.println("create WidgetDao!!");

    }

    public Document findBy(String appId, String deviceId) {
        MongoDatabase db = mongoClient.getDatabase(appId);
        FindIterable<Document> iterable = db.getCollection("Widget").find(
                new Document("deviceId", deviceId));
        if (iterable.iterator().hasNext()) {
            return iterable.first();
        } else {
            return null;
        }
    }

    public void saveOrUpdate(Document widgetDoc) {
        String tableName = "Widget";
        MongoDatabase db = mongoClient.getDatabase(widgetDoc.getString("appId"));
        MongoCollection<Document> collection = db.getCollection(tableName);

        if (!widgetDoc.containsKey("_id")) {
            ObjectId objId = new ObjectId();
            widgetDoc.put("_id", objId);
            collection.insertOne(widgetDoc);
            return;
        }

        String objectId =  widgetDoc.get("_id").toString();
        Document matchFields = new Document();
        matchFields.put("_id", new ObjectId(objectId));
        if(collection.find(matchFields).iterator().hasNext()){
            collection.updateOne(matchFields, new Document("$set",widgetDoc));
        }else{
            collection.insertOne(widgetDoc);//没有数据就执行添加操作
        }
    }
}
