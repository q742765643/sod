package com.piesat.schedule.client.util;

import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.vo.ReplaceVo;
import com.piesat.schedule.util.DateExpressionEngine;
import com.piesat.util.ResultT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

    public static List<String> extractMessageByRegular(String msg) {

        List<String> list = new ArrayList<String>();
        if(StringUtils.isNotNullString(msg)){
            Matcher m = PATTERN.matcher(msg);
            while (m.find()) {
                list.add(m.group());
            }
        }

        return list;
    }

    public static void getIndexOf(ReplaceVo replaceVo, ResultT<String> resultT) {
        String msg = replaceVo.getMsg();
        List<String> list = extractMessageByRegular(replaceVo.getMsg());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashSet timeSet = new HashSet();
        try {
            if (list.size() > 0) {
                for (String s : list) {
                    if (s.toUpperCase().indexOf("DATABASEID") != -1) {
                        msg = msg.replace(s, replaceVo.getDatabaseId());
                    } else if (s.toUpperCase().indexOf("DATACLASSID") != -1) {
                        msg = msg.replace(s, replaceVo.getDataClassId());
                    } else if (s.toUpperCase().indexOf("DDATAID") != -1) {
                        msg = msg.replace(s, replaceVo.getDdataId());
                    } else if (s.toUpperCase().indexOf("YYYY") != -1) {
                        String date = format.format(new Date(replaceVo.getBackupTime()));
                        String time = "";
                        String vlaue = DateExpressionEngine.formatDateExpression("$" + s, date);
                        if (s.split(",").length > 1) {
                            String real = s.replace("{", "").replace("}", "").split(",")[0];
                            Date realDate = new SimpleDateFormat(real).parse(vlaue);
                            if (!vlaue.equals(s.toUpperCase())) {
                                timeSet.add(realDate.getTime());
                            }
                            vlaue = format1.format(realDate);
                        }
                        msg = msg.replace(s, vlaue);


                    }
                }


            }
            replaceVo.setMsg(msg);
            replaceVo.setTimeSet(timeSet);
        } catch (ParseException e) {
            resultT.setCode(301);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String fileNameEl = "HADB--USR_SOD.DMIN_DATA_BACKUP_TASK----A.0010.0001.M006--\\w[a-z0-9]*.[1-9]\\d*.zip";
            Pattern pattern = Pattern.compile(fileNameEl);

            Matcher m = pattern.matcher("HADB--USR_SOD.DMIN_DATA_BACKUP_TASK--20190414--A.0010.0001.M006--645ededa270cb1baa6653fdccf43197f.1.zip");
            String str = "";
            if (m.find()) {
                str = m.group(1);
            }
            System.out.println(str);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}

