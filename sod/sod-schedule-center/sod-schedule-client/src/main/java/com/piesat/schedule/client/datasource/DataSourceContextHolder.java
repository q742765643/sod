package com.piesat.schedule.client.datasource;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 10:10
 **/
@Slf4j
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDataSource(String dbType){
        if(null==dbType){
            log.info("切换到[元数据库]数据源");
        }else{
            log.info("切换到["+dbType+"]数据源");
        }
        contextHolder.set(dbType);
    }

    public static String getDataSource(){
        return contextHolder.get();
    }

    public static void clearDataSource(){
        contextHolder.remove();
    }
}

