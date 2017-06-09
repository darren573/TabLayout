package com.darren.tablayout.utils;

import java.util.Date;

/**
 * Created by lenovo on 2017/6/9.
 */

public class TimeUtils {
    public static String getTime(){
        String time=String .valueOf(new Date().getTime());
        return time.substring(0,time.length()-3);
    }
}
