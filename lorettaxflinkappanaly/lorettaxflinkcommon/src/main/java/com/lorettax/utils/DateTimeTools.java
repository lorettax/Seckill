package com.lorettax.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by li on 2018/12/16.
 */
public class DateTimeTools {
    public static long getThisDayStartedAtMs(long timeNowMs){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date nowmsdate = new Date(timeNowMs);
        String timetemp = dateFormat.format(nowmsdate);

        Date finaldate = null;
        try {
            finaldate = dateFormat.parse(timetemp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finaldate.getTime();
    }

    public static String getstrbyday(long times){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String daystring = dateFormat.format(times);
        return daystring;
    }

    public static Set<String> gettimebyfromandto(String from, String to) throws ParseException {
        Set<String> timeSet = new HashSet<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date fromdate = dateFormat.parse(from);
        Date todate = dateFormat.parse(to);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(fromdate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(todate);
        while(startCal.before(todate)){
            timeSet.add(dateFormat.format(startCal.getTime()));
            startCal.add(Calendar.DAY_OF_MONTH,1);
        }
        timeSet.add(dateFormat.format(startCal.getTime()));
        return timeSet;
    }


}
