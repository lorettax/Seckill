package com.lorettax.service;

import com.lorettax.analy.BrandUser;
import com.lorettax.analy.UserbehvaviorInfo;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface UserBehaviorService {

    public List<UserbehvaviorInfo> listUserBehaviorInfoby(String appId,
                                                          String timeFrom,
                                                          String timeTo,
                                                          String timeUnit, String[] deviceIds);

}
