package com.lorettax.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lorettax.analy.UserbehvaviorInfo;
import com.lorettax.analy.Versionuser;
import com.lorettax.dao.UserBehaviorDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {

    @Autowired
    UserBehaviorDao userBehaviorDao;


    @Override
    public List<UserbehvaviorInfo> listUserBehaviorInfoby(String appId, String timeFrom, String timeTo, String timeUnit, String[] deviceIds) {
        return userBehaviorDao.listUserBehaviorInfoby(appId, timeFrom,timeTo, timeUnit, deviceIds);
    }
}
