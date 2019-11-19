package com.lorettax.service;

import com.lorettax.analy.ErrorInfo;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface ErrorService {

    public List<List<ErrorInfo>> listErrorinfoby(String appId,
                                                 String timeFrom,
                                                 String timeTo,
                                                 String timeUnit,
                                                 String appVersion,
                                                 String appChannel,
                                                 String appPlatform,
                                                 String osType,
                                                 String deviStyle
    );

}
