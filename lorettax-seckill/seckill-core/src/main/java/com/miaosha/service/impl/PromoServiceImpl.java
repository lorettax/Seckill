package com.miaosha.service.impl;

import com.miaosha.dao.PromoDOMapper;
import com.miaosha.dataobject.PromoDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.PromoService;
import com.miaosha.service.UserService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.PromoModel;
import com.miaosha.service.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDOMapper promoDOMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的秒杀信息活动
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        //dataobject --> model
        PromoModel promoModel = convertPromoModelFromDo(promoDO);

        //判断当前活动是否是秒杀活动即将开始或者正在进行
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    @Override
    public void publishPromo(Integer promoId) {
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        if(promoDO.getItemId()==null || promoDO.getItemId().intValue()==0){
            return;
        }
        ItemModel itemModel = itemService.getItemById(promoDO.getItemId());

        redisTemplate.opsForValue().set("promo_item_stock_"+itemModel.getId(),itemModel.getStock());

        //将大闸的限制数字设置到redis内
        redisTemplate.opsForValue().set("promo_door_count_"+promoId,itemModel.getStock().intValue()*10);



    }

    //生成秒杀令牌
    @Override
    public String generateSecondKilltoken(Integer promoId,Integer itemId,Integer userId) {


        //判断库存 是否已经售罄
        if(redisTemplate.hasKey("promo_item_stock_invail_"+itemId)){
            return null;
        }

        //获取对应商品的秒杀信息活动
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);

        //dataobject --> model
        PromoModel promoModel = convertPromoModelFromDo(promoDO);
        if(promoModel==null){
            return null;
        }

        //判断当前活动是否是秒杀活动即将开始或者正在进行
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }

        //判断活动是否正在进行
        if(promoModel.getStatus().intValue()!=2){
            return null;
        }
        //判断用户信息是否存在
        ItemModel itemModel = itemService.getItemByIdInCache(itemId);
        if(itemModel==null){
            return null;
        }
        //判断item信息是否存在
        UserModel userModel = userService.getUserByIdInCache(userId);
        if(userModel==null){
            return null;
        }

        //获取秒杀大闸的count数量
        long result = redisTemplate.opsForValue().increment("promo_door_count_"+promoId,-1);
        if(result<0){
            return null;
        }
        //生成token存入redis内 并且给一个5分钟的有效期
        String token = UUID.randomUUID().toString().replace("-","");
        redisTemplate.opsForValue().set("promo_token_"+promoId+"_userId_"+userId+"_itemId_"+itemId,token);
        redisTemplate.expire("promo_token_"+promoId+"_userId_"+userId+"_itemId_"+itemId,5, TimeUnit.MINUTES);

        return token;
    }

    private PromoModel convertPromoModelFromDo(PromoDO promoDO){
        if (promoDO==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));

        return promoModel;
    }
}
