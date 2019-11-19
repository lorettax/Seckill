package com.lorettax.dao;

import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.NewUser;
import com.lorettax.analy.Startup;
import com.lorettax.analy.Usageinfo;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface UserUsageDao {
    public List<List<Usageinfo>> listUsageinfoby(String appId,
                                         String timeFrom,
                                         String timeTo,
                                         String timeUnit,
                                         String appVersion,
                                         String appChannel,
                                         String appPlatform);
}
