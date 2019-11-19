package com.lorettax.service;

import com.lorettax.analy.Channeluser;
import com.lorettax.analy.Versionuser;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface VersionService {

    public List<Versionuser> listVersionInfoby(String appId,
                                               String timeFrom,
                                               String timeTo,
                                               String timeUnit,
                                               String appChannel,
                                               String appPlatform);

}
