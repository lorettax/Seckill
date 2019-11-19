package com.lorettax.service;

import com.lorettax.analy.ActivitieUser;
import com.lorettax.analy.NewUser;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface UserService {

    public List<List<NewUser>> listnewuserby(String appId,
                                             String timeFrom,
                                             String timeTo,
                                             String timeUnit,
                                             String appVersion,
                                             String appChannel,
                                             String appPlatform);
    public List<List<Startup>> listStarupby(String appId,
                                            String timeFrom, String timeTo,
                                            String timeUnit, String appVersion,
                                            String appChannel,
                                            String appPlatform
    );

    public List<List<ActivitieUser>> listActivitieby(String appId,
                                                     String timeFrom,
                                                     String timeTo,
                                                     String timeUnit,
                                                     String appVersion,
                                                     String appChannel,
                                                     String appPlatform
    );
}
