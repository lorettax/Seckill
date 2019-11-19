package com.lorettax.service;

import com.lorettax.analy.BrandUser;
import com.lorettax.analy.Channeluser;

import java.util.List;

/**
 * Created by li on 2018/12/16.
 */
public interface BrandService {

    public List<BrandUser> listBrandInfoby(String appId,
                                           String timeFrom,
                                           String timeTo,
                                           String timeUnit,
                                           String appChannel,
                                           String appVersion,
                                           String appPlatform);

}
