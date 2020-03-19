package com.piesat.monitor.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-19 10:11
 **/
@Configuration
@MapperScan(basePackages = "com.piesat.monitor.dao.general", sqlSessionTemplateRef = "generalSqlSessionTemplate")
public class MybatisPlusConfigGeneral {
    //general数据源
    @Bean("generalSqlSessionFactory")
    public SqlSessionFactory generalSqlSessionFactory(@Qualifier("generalDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:general/*.xml"));
        return sqlSessionFactory.getObject();
    }

    //事务支持
    @Bean(name = "generalTransactionManager")
    public DataSourceTransactionManager generalTransactionManager(@Qualifier("generalDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "generalSqlSessionTemplate")
    public SqlSessionTemplate generalSqlSessionTemplate(@Qualifier("generalSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

