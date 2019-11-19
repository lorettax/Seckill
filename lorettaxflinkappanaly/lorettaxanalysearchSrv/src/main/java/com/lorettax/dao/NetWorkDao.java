package com.lorettax.dao;

import com.lorettax.analy.BrandUser;
import com.lorettax.analy.NetworkUser;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface NetWorkDao {

    public List<NetworkUser> listNetworkInfoby(String appId,
                                               String timeFrom,
                                               String timeTo,
                                               String timeUnit,
                                               String appChannel,
                                               String appVersion,
                                               String appPlatform);
}
