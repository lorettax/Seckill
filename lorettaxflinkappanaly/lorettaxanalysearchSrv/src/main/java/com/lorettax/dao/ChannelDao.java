package com.lorettax.dao;

import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.Channeluser;
import com.lorettax.analy.NewUser;
import com.lorettax.analy.Startup;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface ChannelDao {

    public List<Channeluser> listChannelInfoby(String appId,
                                               String timeFrom,
                                               String timeTo,
                                               String timeUnit,
                                               String appChannel,
                                               String appPlatform);
}
