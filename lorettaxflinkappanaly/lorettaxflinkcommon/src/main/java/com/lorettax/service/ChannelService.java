package com.lorettax.service;

import com.lorettax.analy.Channeluser;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface ChannelService {

    public List<Channeluser> listChannelInfoby(String appId,
                                               String timeFrom,
                                               String timeTo,
                                               String timeUnit,
                                               String appChannel,
                                               String appPlatform);

}
