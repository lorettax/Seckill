package com.lorettax.service;

import com.lorettax.analy.Usageinfo;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface UserUsageService {

    public List<List<Usageinfo>> listUsageinfoby(String appId,
                                                 String timeFrom,
                                                 String timeTo,
                                                 String timeUnit,
                                                 String appVersion,
                                                 String appChannel,
                                                 String appPlatform);
}
