package org.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-04-01 18:28
 **/
public class DateUtil {

    public static Date sub(Date d){
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR_OF_DAY, -8);
        return cal.getTime();
    }
    public static Date add(Date d){
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR_OF_DAY, 8);
        return cal.getTime();
    }

}

