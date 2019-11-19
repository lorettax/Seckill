package com.lorettax.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by li on 2018/12/12.
 */
public class Readporperities {
        public final static Config config = ConfigFactory.load();
        public static String getKey(String key){
            return config.getString(key).trim();
        }
}
