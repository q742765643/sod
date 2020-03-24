package com.piesat.monitor.config

import java.util
import javax.sql.DataSource

import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.{SqlSessionFactoryBean, SqlSessionTemplate}
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.{Bean, Configuration, Primary}
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import com.alibaba.druid.pool.DruidDataSource
import org.mybatis.spring.boot.autoconfigure.MybatisProperties



/**
  * Created by zzj on 2020/3/19.
  */

@Configuration
@MapperScan(basePackages =Array("com.piesat.monitor.dao.es"), sqlSessionTemplateRef = "esSqlSessionTemplate")
class MybatisConfigEs {
  @Primary
  @Bean(name = Array("esDataSource"))
  @ConfigurationProperties(prefix = "spring.datasource.es")
  def  esDataSource() :DataSource={
    return new DruidDataSource();
  }

  @Primary
  @Bean(Array("esSqlSessionFactory"))
  @throws[Exception]
  def esSqlSessionFactory(@Qualifier("esDataSource") dataSource: DataSource, mybatisProperties:MybatisProperties): SqlSessionFactory = {
    val sqlSessionFactory = new SqlSessionFactoryBean
    sqlSessionFactory.setDataSource(dataSource)
    var resource:Array[Resource]= new PathMatchingResourcePatternResolver().getResources("classpath*:es/**/*.xml")
    sqlSessionFactory.setMapperLocations(resource:_*)
    sqlSessionFactory.setConfiguration(mybatisProperties.getConfiguration)
    return sqlSessionFactory.getObject
  }


  @Primary
  @Bean(name = Array("esSqlSessionTemplate"))
  def esSqlSessionTemplate(@Qualifier("esSqlSessionFactory") sqlSessionFactory: SqlSessionFactory) :SqlSessionTemplate ={
    return new SqlSessionTemplate(sqlSessionFactory)
  }
}
