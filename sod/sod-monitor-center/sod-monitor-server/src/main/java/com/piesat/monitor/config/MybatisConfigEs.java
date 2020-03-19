package com.piesat.monitor.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-19 10:06
 **/
@Configuration
@MapperScan(basePackages = "com.piesat.monitor.dao.es", sqlSessionTemplateRef = "esSqlSessionTemplate")
public class MybatisConfigEs {
    //主数据源 es数据源
    @Primary
    @Bean("esSqlSessionFactory")
    public SqlSessionFactory esSqlSessionFactory(@Qualifier("esDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:es/*.xml"));
        return sqlSessionFactory.getObject();
    }

    @Primary
    @Bean(name = "esTransactionManager")
    public DataSourceTransactionManager esTransactionManager(@Qualifier("esDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "esSqlSessionTemplate")
    public SqlSessionTemplate esSqlSessionTemplate(@Qualifier("esSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

