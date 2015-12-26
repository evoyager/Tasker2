package com.gusar.tasker2;

import java.text.SimpleDateFormat;

/**
 * Created by Evgeniy on 26.12.2015.
 */
public class Utils {
    public static String getDate(long date) {
        return new SimpleDateFormat("dd.MM.yy").format(Long.valueOf(date));
    }

    public static String getTime(long time) {
        return new SimpleDateFormat("HH:mm").format(Long.valueOf(time));
    }

    public static String getFullDate(long date) {
        return new SimpleDateFormat("dd.MM.yy HH:mm").format(Long.valueOf(date));
    }
}
