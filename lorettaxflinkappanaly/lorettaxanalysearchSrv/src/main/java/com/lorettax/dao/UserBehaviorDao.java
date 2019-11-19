package com.lorettax.dao;

import com.lorettax.analy.BrandUser;
import com.lorettax.analy.UserbehvaviorInfo;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface UserBehaviorDao {

    public List<UserbehvaviorInfo> listUserBehaviorInfoby(String appId,
                                                          String timeFrom,
                                                          String timeTo,
                                                          String timeUnit, String[] deviceIds);
}
