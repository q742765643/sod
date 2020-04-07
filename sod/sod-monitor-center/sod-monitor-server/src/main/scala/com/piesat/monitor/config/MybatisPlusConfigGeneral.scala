package com.piesat.monitor.config


import javax.sql.DataSource

import com.alibaba.druid.pool.DruidDataSource
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.annotation.MapperScan
import org.mybatis.spring.boot.autoconfigure.MybatisProperties
import org.mybatis.spring.{SqlSessionFactoryBean, SqlSessionTemplate}
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager

/**
  * Created by zzj on 2020/3/19.
  */
//@Configuration
//@MapperScan(basePackages = Array("com.piesat.monitor.dao.general"), sqlSessionTemplateRef = "generalSqlSessionTemplate")
class MybatisPlusConfigGeneral { //general数据源

  @Bean(name = Array("generalDataSource"))
  @ConfigurationProperties(prefix = "spring.datasource.general")
  def  generalDataSource() :DataSource={
    return new DruidDataSource;
  }
  @Bean(Array("generalSqlSessionFactory"))
  @throws[Exception]
  def generalSqlSessionFactory(@Qualifier("generalDataSource") dataSource: DataSource): SqlSessionFactory = {
    val sqlSessionFactory = new SqlSessionFactoryBean
    sqlSessionFactory.setDataSource(dataSource)
    var resource:Array[Resource]= new PathMatchingResourcePatternResolver().getResources("classpath*:general/**.xml")
    //sqlSessionFactory.setConfiguration(mybatisProperties.getConfiguration)
    sqlSessionFactory.setMapperLocations(resource:_*)
    sqlSessionFactory.getObject
  }

  //事务支持
  @Bean(name = Array("generalTransactionManager")) def generalTransactionManager(@Qualifier("generalDataSource") dataSource: DataSource) = new DataSourceTransactionManager(dataSource)

  @Bean(name = Array("generalSqlSessionTemplate")) def generalSqlSessionTemplate(@Qualifier("generalSqlSessionFactory") sqlSessionFactory: SqlSessionFactory) = new SqlSessionTemplate(sqlSessionFactory)
}

