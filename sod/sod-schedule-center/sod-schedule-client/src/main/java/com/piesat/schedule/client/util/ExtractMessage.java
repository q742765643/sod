package com.piesat.schedule.client.util;

import com.piesat.schedule.client.vo.ReplaceVo;
import com.piesat.schedule.util.DateExpressionEngine;
import org.bouncycastle.math.ec.ScaleYPointMap;

import java.text.DateFormat;
import java.text.ParseException;
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

    public static void getIndexOf(ReplaceVo replaceVo){
        String msg=replaceVo.getMsg();
        List<String> list=extractMessageByRegular(replaceVo.getMsg());
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashSet timeSet=new HashSet();
        if(list.size()>0){
            for(String s:list){
                if(s.toUpperCase().indexOf("DATABASEID")!=-1){
                    msg=msg.replace(s,replaceVo.getDatabaseId());
                }else if(s.toUpperCase().indexOf("DATACLASSID")!=-1){
                    msg=msg.replace(s,replaceVo.getDataClassId());
                }else if(s.toUpperCase().indexOf("YYYY")!=-1){
                    String date=format.format(new Date(replaceVo.getBackupTime()));
                    String vlaue= DateExpressionEngine.formatDateExpression("$"+s,date);

                    try {
                        if(s.split(",").length>1) {
                            String real = s.replace("{", "").replace("}", "").split(",")[0];
                            Date realDate = new SimpleDateFormat(real).parse(vlaue);
                            vlaue = format1.format(realDate);
                        }
                        msg=msg.replace(s,vlaue);
                        if(!vlaue.equals(s.toUpperCase())){
                            timeSet.add(vlaue);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }


        }
        replaceVo.setMsg(msg);
        replaceVo.setTimeSet(timeSet);
    }

    public static void main(String[] args) {

       String a= DateExpressionEngine.formatDateExpression("${yyyy-MM-dd HH:mm:ss,-1h}","2020030600");
       System.out.println(a);
    }

}

