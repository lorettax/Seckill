package com.lorettax.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.NewUser;
import com.lorettax.analy.Startup;
import com.lorettax.analy.Usageinfo;
import com.lorettax.dao.UserUsageDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Service
public class UserUsageServiceImpl implements UserUsageService {

    @Autowired
    UserUsageDao userUsageDao;


    @Override
    public List<List<Usageinfo>> listUsageinfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform) {
        return userUsageDao.listUsageinfoby(appId, timeFrom, timeTo, timeUnit, appVersion,appChannel,appPlatform);
    }
}
