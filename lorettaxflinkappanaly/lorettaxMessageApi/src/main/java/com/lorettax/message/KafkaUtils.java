package com.lorettax.message;

import com.lorettax.util.Readporperities;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * Created by li on 2018/12/12.
 */
public class KafkaUtils {
    private  Logger logger = LogManager.getLogger(KafkaUtils.class);
    private  KafkaProducer<String, String> producer = null;

    public KafkaUtils(){
        Properties pro = new Properties();
        pro.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Readporperities.getKey("brokerList"));
        pro.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        pro.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<String, String>(pro);
    }

    public void sendMessage(String topic,String message){
        ProducerRecord<String,String> producerRecord = new ProducerRecord<String,String>(topic,message);
        producer.send(producerRecord , new Callback() {

            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if(exception != null){
                    logger.error("发送消息失败！！");
                }else{
                    logger.info("发送消息成功！！"+metadata.offset()+"=="+metadata.partition()+"=="+metadata.topic());
                }

            }
        });
    }

    public void destroy() throws Exception {
        if(producer != null){
            producer.close();
        }

    }
}
