package com.miaosha.service;

import com.miaosha.service.model.PromoModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public interface PromoService {

    //根据itemId即将进行或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);

    //活动发布
    void publishPromo(Integer promoId);

    //生成秒杀令牌
    String generateSecondKilltoken(Integer promoId,Integer itemId,Integer userId);

}
