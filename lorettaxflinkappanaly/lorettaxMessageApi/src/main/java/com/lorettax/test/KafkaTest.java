package com.lorettax.test;

import com.lorettax.message.KafkaUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by li on 2018/12/12.
 */
public class KafkaTest {
    public static void main(String[] args) {
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/application-context.xml");
        KafkaUtils producer = (KafkaUtils) ctx.getBean("kafkaUtils");
        producer.sendMessage("test1", "hehe");
        ctx.registerShutdownHook();
    }


}
