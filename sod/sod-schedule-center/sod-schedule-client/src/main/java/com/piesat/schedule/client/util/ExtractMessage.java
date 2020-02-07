package com.piesat.schedule.client.util;

import com.piesat.schedule.util.DateExpressionEngine;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-06 16:01
 **/
public class ExtractMessage {
    private static Pattern PATTERN = Pattern.compile("(\\{[^\\}]*\\})");
    public static List<String> extractMessageByRegular(String msg){

        List<String> list=new ArrayList<String>();
        Matcher m = PATTERN.matcher(msg);
        while(m.find()){
            list.add(m.group());
        }
        return list;
    }

    public static Map getIndexOf(String msg, String databaseId, String dataClassId, long time){
        List<String> list=extractMessageByRegular(msg);
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmm");
        HashSet set=new HashSet();
        LinkedHashMap timeMap=new LinkedHashMap();
        if(list.size()>0){
            for(String s:list){
                if(s.toUpperCase().indexOf("DATABASEID")!=-1){
                    msg=msg.replace(s,databaseId);
                }else if(s.toUpperCase().indexOf("DATACLASSID")!=-1){
                    msg=msg.replace(s,dataClassId);
                }else if(s.toUpperCase().indexOf("YYYYMMDD")!=-1){
                    String date=format.format(new Date(time));
                    String vlaue= DateExpressionEngine.formatDateExpression("$"+s,date);
                    msg=msg.replace(s,vlaue);
                    if(!vlaue.equals(s.toUpperCase())){
                        set.add(vlaue);
                    }
                }
            }


        }
        Map map=new HashMap();
        map.put("msg",msg);
        map.put("set",set);
        return map;
    }

    public static void main(String[] args) {


    }

}

