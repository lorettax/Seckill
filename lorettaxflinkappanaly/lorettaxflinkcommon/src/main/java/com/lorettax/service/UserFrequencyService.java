package com.lorettax.service;

import com.lorettax.analy.ErrorInfo;
import com.lorettax.analy.UserFrequencyResultInfo;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface UserFrequencyService {

    public List<UserFrequencyResultInfo> listUserFrequencyinfoby(String appId,
                                                                 String timeFrom,
                                                                 String timeTo,
                                                                 String timeUnit,
                                                                 String appVersion,
                                                                 String appChannel,
                                                                 String appPlatform);

}
