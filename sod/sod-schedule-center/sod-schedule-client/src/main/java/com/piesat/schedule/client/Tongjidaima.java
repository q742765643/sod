package com.piesat.schedule.client;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-17 16:41
 **/
public class Tongjidaima {
    private static int i;//代码总行数
    private static int j;//文件个数
    public static void main(String[] args) throws IOException {
       /* File file = new File("/zzj/git/cunguan/DataStoragePlatform_maven");//需要统计行数的文件夹路径
        traverseFiles(file);//调用递归方法查看.java文件，用于统计行数
        System.out.println("所写文件个数："+j);
        System.out.println("所写代码总行数："+i);*/
        List<Map<String,String>> map=new ArrayList<>();
        Map<String,String> map1=new HashMap<>();
        map1.put("name","create_time");
        map1.put("sort","asc");
        map.add(map1);
        String sql=JSON.toJSONString(map);
        Map<String,Object> map2=new HashMap<>();
        map2.put("orderBy",map);
        String bb=JSON.toJSONString(map2);
        System.out.println(bb);

    }
    public static void traverseFiles(File file) throws IOException{
        if(!file.exists()){//文件不存在
            return;
        }

        if(!file.isDirectory()){//判断是否为文件
            String filename = file.getName();
            if(filename.endsWith(".java")){//判断是否是.java文件
                j++;
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String string =null;
                while ((string = bufferedReader.readLine()) != null) {
                    i++;//读取行数
                }
            }else
                return;
        }

        File[] files =file.listFiles();//读取文件夹的子文件或子文件夹
        if (files == null || files.length == 0) {
            return;
        }

        for(File file2 : files){//如果是文件夹递归调用方法遍历文件
            traverseFiles(file2);
        }
    }
}