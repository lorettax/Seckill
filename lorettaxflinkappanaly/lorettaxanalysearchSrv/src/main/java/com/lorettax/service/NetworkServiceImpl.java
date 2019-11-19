package com.lorettax.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lorettax.analy.BrandUser;
import com.lorettax.analy.NetworkUser;
import com.lorettax.dao.NetWorkDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
@Service
public class NetworkServiceImpl implements NetworkService {

    @Autowired
    NetWorkDao netWorkDao;


    @Override
    public List<NetworkUser> listNetworkInfoby(String appId, String timeFrom, String timeTo, String timeUnit, String appChannel, String appVersion, String appPlatform) {
        return netWorkDao.listNetworkInfoby(appId, timeFrom, timeTo,timeUnit, appChannel, appVersion, appPlatform);
    }
}
