package com.miaosha.service.impl;

import com.miaosha.MQ.MqProducer;
import com.miaosha.dao.ItemDOMapper;
import com.miaosha.dao.ItemStockDoMapper;
import com.miaosha.dao.StockLogDOMapper;
import com.miaosha.dataobject.ItemDO;
import com.miaosha.dataobject.ItemStockDo;
import com.miaosha.dataobject.StockLogDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.PromoService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.PromoModel;
import com.miaosha.validator.ValidationResult;
import com.miaosha.validator.ValidatorImpl;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Autowired
    private PromoService promoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MqProducer producer;

    @Autowired
    private StockLogDOMapper stockLogDOMapper;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult validationResult = validator.validate(itemModel);

        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }
        //转化itemModel为dataObject
        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);

        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());

        ItemStockDo itemStockDo = convertItemStockDoFromItemModel(itemModel);
        itemStockDoMapper.insertSelective(itemStockDo);

        return this.getItemById(itemModel.getId());
    }

    private ItemStockDo convertItemStockDoFromItemModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        ItemStockDo itemStockDo = new ItemStockDo();
        itemStockDo.setItemId(itemModel.getId());
        itemStockDo.setStock(itemModel.getStock());
        return itemStockDo;
    }

    private ItemDO convertItemDOFromItemModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;

    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem();
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDO.getId());
        ItemModel itemModel = this.convertModelFromDataObject(itemDO,itemStockDo);
        return itemModel;
        }).collect(Collectors.toList());

        return itemModelList;
    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO,ItemStockDo itemStockDo){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDo.getStock());
        return itemModel;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if(itemDO==null){
            return null;
        }
        //操作获得库存数量
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDO.getId());
        //将dataobject->model
        ItemModel itemModel = convertModelFromDataObject(itemDO,itemStockDo);

        //获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if(promoModel!=null && promoModel.getStatus().intValue()!=3){
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
//        int affectedRow = itemStockDoMapper.decreaseStock(itemId,amount);
        long result = redisTemplate.opsForValue().increment("promo_item_stock_"+itemId,amount.intValue()*-1);
        if(result>0){
            //--更新库存成功
//            Boolean mqResult = producer.asyncReduceStock(itemId,amount);
//            if(!mqResult){//如果异步消息发送失败，则缓存进行回滚
//                redisTemplate.opsForValue().increment("promo_item_stock_"+itemId, amount.intValue());
//            }
            return true;
        }else if(result==0){
            redisTemplate.opsForValue().set("promo_item_stock_invail_"+itemId,"true");
            return true;
        }else{
            //更新库存失败，缓存也会进行回滚
            increaseStock(itemId,amount);
            return false;
        }
    }

    @Override
    public boolean increaseStock(Integer itemId, Integer amount) throws BusinessException {
        redisTemplate.opsForValue().increment("promo_item_stock_"+itemId,amount.intValue());
        return true;
    }

    @Override
    public boolean asyncDecreaseStock(Integer itemId, Integer amount) {
        Boolean mqResult = producer.asyncReduceStock(itemId,amount);

        return mqResult;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId,amount);
    }

    @Override
    public ItemModel getItemByIdInCache(Integer id) {
        ItemModel itemModel =(ItemModel) redisTemplate.opsForValue().get("item_validate_"+id);
        if(itemModel==null){
            itemModel = this.getItemById(id);
            redisTemplate.opsForValue().set("item_validate_"+id,itemModel);
            redisTemplate.expire("item_validate_"+id,10, TimeUnit.MINUTES);
        }
        return itemModel;
    }

    @Override
    @Transactional
    public String initStockLog(Integer itemId, Integer amount) {
        StockLogDO stockLogDO = new StockLogDO();
        stockLogDO.setItemId(itemId);
        stockLogDO.setAmount(amount);
        stockLogDO.setStockLogId(UUID.randomUUID().toString().replace("-",""));
        stockLogDO.setStatus(1);

        stockLogDOMapper.insertSelective(stockLogDO);

        return stockLogDO.getStockLogId();

    }
}
