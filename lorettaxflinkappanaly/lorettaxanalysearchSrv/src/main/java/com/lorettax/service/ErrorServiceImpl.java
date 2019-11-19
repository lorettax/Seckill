package com.lorettax.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.ErrorInfo;
import com.lorettax.analy.NewUser;
import com.lorettax.analy.Startup;
import com.lorettax.dao.ErrorDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Service
public class ErrorServiceImpl implements ErrorService {

    @Autowired
    ErrorDao errorDao;


    @Override
    public List<List<ErrorInfo>> listErrorinfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform, String osType, String deviStyle) {
        return errorDao.listErrorinfoby(appId, timeFrom, timeTo, timeUnit, appVersion, appChannel, appPlatform, osType, deviStyle);
    }
}
