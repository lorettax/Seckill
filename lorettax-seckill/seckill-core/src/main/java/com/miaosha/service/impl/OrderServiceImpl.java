package com.miaosha.service.impl;

import com.miaosha.dao.OrderDoMapper;
import com.miaosha.dao.SequenceDoMapper;
import com.miaosha.dao.StockLogDOMapper;
import com.miaosha.dataobject.OrderDo;
import com.miaosha.dataobject.SequenceDo;
import com.miaosha.dataobject.StockLogDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.OrderService;
import com.miaosha.service.UserService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private SequenceDoMapper sequenceDoMapper;

    @Autowired
    private StockLogDOMapper stockLogDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount, String stockLogId) throws BusinessException {
        //1.校验下单状态，下单的商品是否存在，用户是否合法，购买数量是否正确
//        ItemModel itemModel = itemService.getItemById(itemId);
        ItemModel itemModel = itemService.getItemByIdInCache(itemId);
        if(itemModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }
//        UserModel userModel = userService.getUserByIdInCache(userId);
//        if(userModel==null){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
//        }
        if(amount<=0 || amount>99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }

        //校验活动信息
//        if(promoId!=null){
//            // (1).校验对应活动是否存在这个适应商品
//            if(promoId.intValue() !=itemModel.getPromoModel().getId()){
//                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
//            }else if (itemModel.getPromoModel().getStatus().intValue()!=2){
//            //(2).校验活动信息是否在进行中
//                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息还未开始");
//            }
//        }


        //2.落单减库存
        Boolean result = itemService.decreaseStock(itemId,amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setAmount(amount);
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        if(promoId!=null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            orderModel.setItemPrice(itemModel.getPrice());

        }
        orderModel.setPromoId(promoId);
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        //--生成交易流水号，订单号
        orderModel.setId(generateOrderNo());

        OrderDo orderDo = convertDataObjectFromOrderModel(orderModel);
        orderDoMapper.insertSelective(orderDo);

        //销量增加
        itemService.increaseSales(itemId,amount);

        //设置库存流水状态为成功
        StockLogDO stockLogDO = stockLogDOMapper.selectByPrimaryKey(stockLogId);
        if(stockLogDO==null){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
        stockLogDO.setStatus(2);
        stockLogDOMapper.updateByPrimaryKeySelective(stockLogDO);


//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//            @Override
//            public void afterCommit() {//一旦commit后，异步消息发送失败 ，再没有机会回滚了
//                try {
//                    boolean mqresult = itemService.decreaseStock(itemId,amount);
//                } catch (BusinessException e) {
//                    e.printStackTrace();
//                }
////                if(!mqresult){
////                    itemService.increaseStock(itemId,amount);
////                    throw new BusinessException(EmBusinessError.MQ_SEND_ERROR);
////                }
//
//            }
//        });


        //4.返回前端
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo(){
        //--订单号16位，
        StringBuilder stringBuilder = new StringBuilder();
        // 前8位为时间信息
        LocalDateTime localDateTime = LocalDateTime.now();
        String nowDate = localDateTime.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //--中间六位为订单序列号
        //获取当前sequence
        int sequence = 0;
        SequenceDo sequenceDo = sequenceDoMapper.getSequenceByName("order_info");
        sequence = sequenceDo.getCurrentValue();
        sequenceDo.setCurrentValue(sequenceDo.getCurrentValue()+sequenceDo.getStep());
        sequenceDoMapper.updateByPrimaryKeySelective(sequenceDo);
        String sequenecStr = String.valueOf(sequence);
        for (int i=0;i<6-sequenecStr.length();i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenecStr);
        //--最后两位为分库分表
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderDo convertDataObjectFromOrderModel(OrderModel orderModel){
        if(orderModel==null){
            return null;
        }
        OrderDo orderDo = new OrderDo();
        BeanUtils.copyProperties(orderModel,orderDo);
        orderDo.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDo.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDo;
    }
}
