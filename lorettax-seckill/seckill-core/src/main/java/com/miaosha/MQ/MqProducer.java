package com.miaosha.MQ;

import com.alibaba.fastjson.JSON;
import com.miaosha.dao.StockLogDOMapper;
import com.miaosha.dataobject.StockLogDO;
import com.miaosha.error.BusinessException;
import com.miaosha.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
@Component
public class MqProducer {

    private DefaultMQProducer defaultMQProducer;

    //事务型MQ
    private TransactionMQProducer transactionMQProducer;

    @Value("${mq.nameserver.addr}")
    private String nameAddr;

    @Value("${mq.topicname}")
    private String topicName;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockLogDOMapper stockLogDOMapper;

    @PostConstruct
    public void init() throws MQClientException {
        //做mq producer的初始化
        defaultMQProducer = new DefaultMQProducer("producer_group");
        defaultMQProducer.setNamesrvAddr(nameAddr);
        defaultMQProducer.start();

        //事务MQ
        transactionMQProducer = new TransactionMQProducer("transcation_producer_group");
        transactionMQProducer.setNamesrvAddr(nameAddr);
        transactionMQProducer.start();
        //类似二阶段提交
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                //创建订单，really work
                Integer userId = (Integer) ((Map)o).get("userId");
                Integer promoId = (Integer) ((Map)o).get("promoId");
                Integer itemId = (Integer) ((Map)o).get("itemId");
                Integer amount = (Integer) ((Map)o).get("amount");
                String stockLogId = (String) ((Map)o).get("stockLogId");

                try {
                    orderService.createOrder(userId,itemId,promoId,amount,stockLogId);
                } catch (BusinessException e) {
                    e.printStackTrace();
                    StockLogDO stockLogDO = stockLogDOMapper.selectByPrimaryKey(stockLogId);
                    stockLogDO.setStatus(3);
                    stockLogDOMapper.updateByPrimaryKeySelective(stockLogDO);
                    //创建订单的时候发生任何异常，事务都需要回滚
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                String jsonString = new String(messageExt.getBody());
                Map<String,Object> map = JSON.parseObject(jsonString,Map.class);
                Integer itemId = (Integer)map.get("itemId");
                Integer amount = (Integer)map.get("amount");
                String stockLogId = (String)map.get("stockLogId");
                StockLogDO stockLogDO = stockLogDOMapper.selectByPrimaryKey(stockLogId);
                if(stockLogDO==null){
                    return LocalTransactionState.UNKNOW;
                }else if(stockLogDO.getStatus().intValue()==2){
                    return LocalTransactionState.COMMIT_MESSAGE;
                }else if(stockLogDO.getStatus().intValue()==1){
                    return LocalTransactionState.UNKNOW;
                }

                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

    }

    public boolean transcationAsyncReduceStock(
            Integer userId, Integer itemId,Integer promoId,Integer amount,String stockLogId){

        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("itemId",itemId);
        bodyMap.put("amount",amount);
        bodyMap.put("stockLogId",stockLogId);

        Map<String,Object> argsMap = new HashMap<>();
        argsMap.put("itemId",itemId);
        argsMap.put("amount",amount);
        argsMap.put("userId",userId);
        argsMap.put("promoId",promoId);
        argsMap.put("stockLogId",stockLogId);

        Message message = new Message(topicName,"increase",
                JSON.toJSON(bodyMap).toString().getBytes(Charset.forName("UTF-8")));

        TransactionSendResult sendResult =null;
        try {
            //发送的是事务型消息，
            sendResult =  transactionMQProducer.sendMessageInTransaction(message,argsMap);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        }
        if(sendResult.getLocalTransactionState()==LocalTransactionState.ROLLBACK_MESSAGE){
            return false;
        }else if(sendResult.getLocalTransactionState()==LocalTransactionState.COMMIT_MESSAGE){
            return true;
        }else {
            return false;
        }
    }

    //同步库存扣减消息
    public boolean asyncReduceStock(Integer itemId, Integer amount){
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("itemId",itemId);
        bodyMap.put("amount",amount);
        Message message = new Message(topicName,"increase",
                JSON.toJSON(bodyMap).toString().getBytes(Charset.forName("UTF-8")));

        try {
            defaultMQProducer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        } catch (RemotingException e) {
            e.printStackTrace();
            return false;
        } catch (MQBrokerException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
