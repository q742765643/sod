package com.piesat.schedule.client.util;

import com.piesat.schedule.client.vo.ReplaceVo;
import com.piesat.schedule.util.DateExpressionEngine;
import com.piesat.util.ResultT;
import org.apache.commons.io.FileUtils;

import java.io.File;
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

    public static List<String> extractMessageByRegular(String msg) {

        List<String> list = new ArrayList<String>();
        Matcher m = PATTERN.matcher(msg);
        while (m.find()) {
            list.add(m.group());
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
                    } else if (s.toUpperCase().indexOf("YYYY") != -1) {
                        String date = format.format(new Date(replaceVo.getBackupTime()));
                        String vlaue = DateExpressionEngine.formatDateExpression("$" + s, date);

                        if (s.split(",").length > 1) {
                            String real = s.replace("{", "").replace("}", "").split(",")[0];
                            Date realDate = new SimpleDateFormat(real).parse(vlaue);
                            vlaue = format1.format(realDate);
                        }
                        msg = msg.replace(s, vlaue);
                        if (!vlaue.equals(s.toUpperCase())) {
                            timeSet.add(vlaue);
                        }


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
            String srcDirPath = "/zzj/git/hthtsod/sod/sod-schedule-center/sod-schedule-client/src/main/java/com/piesat/schedule/client";
            // 转为UTF-8编码格式源码路径
            String utf8DirPath = "/zzj/git/hthtsod/fetl";

            // 获取所有java文件
            Collection<File> javaGbkFileCol = FileUtils.listFiles(new File(srcDirPath), new String[] { "java" }, true);

            for (File javaGbkFile : javaGbkFileCol) {
                // UTF8格式文件路径
                //String utf8FilePath = utf8DirPath + javaGbkFile.getAbsolutePath().substring(srcDirPath.length());
                // 使用GBK读取数据，然后用UTF-8写入数据
                //FileUtils.writeLines(new File(utf8FilePath), "UTF-8", FileUtils.readLines(javaGbkFile, "GBK"));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}

