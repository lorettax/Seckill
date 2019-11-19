package com.lorettax.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lorettax.analy.BrandUser;
import com.lorettax.analy.Channeluser;
import com.lorettax.dao.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandDao brandDao;


    @Override
    public List<BrandUser> listBrandInfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appChannel, String appVersion, String appPlatform) {
        return brandDao.listBrandInfoby(appId, timeFrom, timeTo, timeUnit, appChannel, appVersion, appPlatform);
    }
}
