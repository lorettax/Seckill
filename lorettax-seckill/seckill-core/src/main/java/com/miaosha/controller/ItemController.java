package com.miaosha.controller;


import com.miaosha.controller.viewobject.ItemVo;
import com.miaosha.error.BusinessException;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.CacheService;
import com.miaosha.service.ItemService;
import com.miaosha.service.PromoService;
import com.miaosha.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ItemController extends BaseController {


    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private PromoService promoService;

    //创建商品的Controller

    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "Description")String description,
                                       @RequestParam(name = "price")BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl) throws BusinessException {
        //封装service请求来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setTitle(title);
        itemModel.setImgUrl(imgUrl);
        itemModel.setStock(stock);

        ItemModel itemModelReturn = itemService.createItem(itemModel);
        ItemVo itemVo = convertItemVoFromItemModel(itemModelReturn);

        return CommonReturnType.create(itemVo);
    }

    public ItemVo convertItemVoFromItemModel(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel,itemVo);
        if(itemModel.getPromoModel()!=null){
            //有正在进行或者即将进行的秒杀活动
            itemVo.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVo.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVo.setPromoId(itemModel.getPromoModel().getItemId());
            itemVo.setStartTime(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        }else{
            itemVo.setPromoStatus(0);
        }


        return itemVo;
    }


    //--商品列表页面浏览
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    private CommonReturnType listItem(){
        List<ItemModel> itemModelList = itemService.listItem();
        List<ItemVo> itemVoList = itemModelList.stream().map(itemModel -> {
            ItemVo itemVo = convertItemVoFromItemModel(itemModel);
            return itemVo;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVoList);
    }

    //--商品详情页面浏览
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    @ResponseBody
    private CommonReturnType getItem(@RequestParam(name = "id")Integer id){

        ItemModel itemModel = null;
        //先取本地缓存 2级缓存
        itemModel =(ItemModel)cacheService.getFromCommonCache("item_"+id);
        if(itemModel==null){
            //根据商品id到redis中获取 一级缓存
            itemModel =(ItemModel) redisTemplate.opsForValue().get("item_"+id);
            if(itemModel==null){
                itemModel = itemService.getItemById(id);
                //设置ItemModel到Redis内
                redisTemplate.opsForValue().set("item_"+id,itemModel);
                redisTemplate.expire("item_"+id,10, TimeUnit.MINUTES);
            }
        }
        //填充本地缓存
        cacheService.setCommonCache("item_"+id,itemModel);

        ItemVo itemVo = convertItemVoFromItemModel(itemModel);

        return CommonReturnType.create(itemVo);
    }

    //--应该是运营后台调的类，同步库存至redis缓存内
    @RequestMapping(value = "/publishpromo",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType publishPromo(@RequestParam(name = "id") Integer id){
        promoService.publishPromo(id);
        return CommonReturnType.create(null);
    }

}
