package org.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String,Date> getStartAndEnd(){
        Map<String,Date> map=new HashMap<>();
        Date endTime=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(endTime);
        cal.add(Calendar.MINUTE, -5);
        Date startTime=cal.getTime();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        return map;
    }
    public static Map<String,Date> getStartAndEnd10(){
        Map<String,Date> map=new HashMap<>();
        Date endTime=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(endTime);
        cal.add(Calendar.MINUTE, -10);
        Date startTime=cal.getTime();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        return map;
    }


}

