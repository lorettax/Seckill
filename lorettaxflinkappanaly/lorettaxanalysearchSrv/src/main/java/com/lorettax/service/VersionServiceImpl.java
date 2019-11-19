package com.lorettax.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lorettax.analy.Channeluser;
import com.lorettax.analy.Versionuser;
import com.lorettax.dao.VersionDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    VersionDao versionDao;


    @Override
    public List<Versionuser> listVersionInfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appChannel, String appPlatform) {
        return versionDao.listVersionInfoby(appId,timeFrom, timeTo, timeUnit, appChannel, appPlatform);
    }
}
