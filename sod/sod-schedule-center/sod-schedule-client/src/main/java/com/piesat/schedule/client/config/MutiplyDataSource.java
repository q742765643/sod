package com.piesat.schedule.client.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 10:49
 **/
@Configuration
public class MutiplyDataSource {
    @Bean(name = "dataSourcePrimary")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource(){
        return new DruidDataSource();
    }
    @Primary
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //配置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(primaryDataSource());
        DynamicDataSource._targetDataSources.put("primary",primaryDataSource());
        dynamicDataSource.setTargetDataSources(DynamicDataSource._targetDataSources);
        return dynamicDataSource;
    }
    /**
     * 配置@Transactional注解事务
     * @return
     */
    /*@Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }*/
}

