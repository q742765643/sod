package com.piesat.schedule.client;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.util.Md5Utils;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.util.ResultT;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        String aa="aaaaa--sss";
        String[] aaa=aa.split("--");
        System.out.println(aaa[0]);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.xugu.cloudjdbc.Driver");
        dataSource.setUrl("jdbc:xugu://10.20.64.167:5142/BABJ_MTDB?ips=10.20.64.168,10.20.64.169&char_set=utf8");
        dataSource.setUsername("usr_mmd");
        dataSource.setPassword("mmd_sunsheen");
        dataSource.setMinIdle(3);
        dataSource.setMaxActive(20);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setMaxWait(60000);
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setFailFast(true);
        dataSource.setConnectionErrorRetryAttempts(0);
        dataSource.setMaxOpenPreparedStatements(0);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(180);
        dataSource.setLogAbandoned(true);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setValidationQuery("select 1 ");
     /*   for(int i=0;i<1;i++){

            Connection connection=null;
            try {
                //Thread.sleep(1000);
                connection=dataSource.getConnection();
            } catch (Exception throwables) {
                if(connection==null){
                    System.out.println("ssss");
                }
            }
        }*/
        Connection connection = null;
        try {    //排除连接不上的错误
            Class.forName("com.gbase.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:gbase://10.20.64.29:5258/usr_sod?useOldAliasMetadataBehavior=true&rewriteBatchedStatements=true&connectTimeout=0&hostList=10.20.64.29,10.20.64.30,10.20.64.31&failoverEnable=true", "usr_mmd", "a");

        } catch (Exception e) {
            System.out.println(333);
        }finally {
            if(connection==null){
              System.out.println(111);
            }
            if(null!=connection){
                System.out.println(222);
                try {
                    connection.close();
                } catch (Exception e) {

                }
            }
        }

        dataSource.close();
        dataSource=null;
        ZipUtils.doCompress("/root/nas", "/root/nas.zip", new ResultT<>());
        String a=Md5Utils.getFileMD5String("/root/nas.zip",new ResultT<>());
        System.out.println(a);
        try {
            FileUtil.copyFile("/root/nas.zip", "/root/nas1.zip",new ResultT<>());
            FileUtil.delFile(new File("/root/nas.zip"),new ResultT<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true){

        }

        /*File file = new File("/zzj/git/hthtsod/sod");//需要统计行数的文件夹路径
        traverseFiles(file);//调用递归方法查看.java文件，用于统计行数
        System.out.println("所写文件个数："+j);
        System.out.println("所写代码总行数："+i);
        List<Map<String,String>> map=new ArrayList<>();
        Map<String,String> map1=new HashMap<>();
        map1.put("name","create_time");
        map1.put("sort","asc");
        map.add(map1);
        String sql=JSON.toJSONString(map);
        Map<String,Object> map2=new HashMap<>();
        map2.put("orderBy",map);
        String bb=JSON.toJSONString(map2);
        System.out.println(bb);*/

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
