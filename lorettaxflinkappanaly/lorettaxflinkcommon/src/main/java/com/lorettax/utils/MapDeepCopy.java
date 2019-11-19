package com.lorettax.utils;

import java.util.*;

/**
 * Created by li on 2019/1/1.
 */
public class MapDeepCopy {

    public static Map<String,Long> deepcopy(Map<String,Long> map,Comparator<String> comparator){
            Map mapdata = new TreeMap<String,Long>();

            if(map==null||map.isEmpty()){
                return mapdata;
            }
            if(comparator!=null){
                mapdata = new TreeMap<String,Long>(comparator);
            }
            Set<Map.Entry<String,Long>> set = map.entrySet();
            for(Map.Entry<String,Long> entry:set){
                String key = entry.getKey();
                Long value = entry.getValue();
                mapdata.put(key,value);
            }
        return mapdata;
    }

    public static Map<Long,Long> deepcopybylong(Map<Long,Long> map,Comparator<Long> comparator){
        Map mapdata = new TreeMap<String,Long>();

        if(map==null||map.isEmpty()){
            return mapdata;
        }
        if(comparator!=null){
            mapdata = new TreeMap<Long,Long>(comparator);
        }
        Set<Map.Entry<Long,Long>> set = map.entrySet();
        for(Map.Entry<Long,Long> entry:set){
            Long key = entry.getKey();
            Long value = entry.getValue();
            mapdata.put(key,value);
        }
        return mapdata;
    }
}
