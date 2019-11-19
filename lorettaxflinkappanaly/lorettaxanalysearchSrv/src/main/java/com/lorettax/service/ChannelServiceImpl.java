package com.lorettax.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.Channeluser;
import com.lorettax.analy.NewUser;
import com.lorettax.analy.Startup;
import com.lorettax.dao.ChannelDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    ChannelDao channelDao;


    @Override
    public List<Channeluser> listChannelInfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appChannel, String appPlatform) {
        return channelDao.listChannelInfoby(appId,timeFrom, timeTo, timeUnit,appChannel, appPlatform);
    }
}
