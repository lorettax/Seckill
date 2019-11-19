package com.lorettax.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.NewUser;
import com.lorettax.analy.Startup;
import com.lorettax.analy.UserFrequencyResultInfo;
import com.lorettax.dao.UserFrequencyDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Service
public class UserFrequencyServiceImpl implements UserFrequencyService {

    @Autowired
    UserFrequencyDao userFrequencyDao;

    @Override
    public List<UserFrequencyResultInfo> listUserFrequencyinfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform) {
        return userFrequencyDao.listUserFrequencyinfoby(appId, timeFrom, timeTo, timeUnit, appVersion, appChannel, appPlatform);
    }
}
