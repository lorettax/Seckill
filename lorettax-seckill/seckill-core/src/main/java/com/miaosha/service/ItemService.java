package com.miaosha.service;

import com.miaosha.error.BusinessException;
import com.miaosha.service.model.ItemModel;
import java.util.List;

public interface ItemService {

    //--创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    //--库存扣减
    boolean decreaseStock(Integer itemId,Integer amount) throws BusinessException;

    //--库存扣减
    boolean increaseStock(Integer itemId,Integer amount) throws BusinessException;

    //--异步更新库存
    boolean asyncDecreaseStock(Integer itemId,Integer amount);

    //销量增加
    void increaseSales(Integer itemId,Integer amount) throws BusinessException;

    //-item及promo Model缓存模型
    ItemModel getItemByIdInCache(Integer id);

    //初始化库存流水
    String initStockLog(Integer itemId,Integer amount);

}
