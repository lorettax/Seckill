package com.lorettax.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.Startup;
import com.lorettax.dao.UserDao;
import com.lorettax.analy.NewUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    public List<List<NewUser>> listnewuserby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform) {

            return userDao.listnewuserby(appId,timeFrom, timeTo, timeUnit, appVersion, appChannel, appPlatform);
    }

    public List<List<Startup>> listStarupby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform) {

        return userDao.listStarupby(appId,timeFrom, timeTo, timeUnit, appVersion, appChannel, appPlatform);
    }
    public List<List<ActivitieUser>> listActivitieby(String appId, String timeFrom, String timeTo, String timeUnit, String appVersion, String appChannel, String appPlatform) {

        return userDao.listActivitieby(appId,timeFrom, timeTo, timeUnit, appVersion, appChannel, appPlatform);
    }
}
