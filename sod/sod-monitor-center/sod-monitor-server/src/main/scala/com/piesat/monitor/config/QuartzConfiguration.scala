package com.piesat.monitor.config

import org.quartz.Scheduler
import org.quartz.spi.{TriggerFiredBundle}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.quartz.{AdaptableJobFactory, SchedulerFactoryBean}
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import java.io.IOException
import java.util.Properties
/**
  * Created by zzj on 2020/4/4.
  */
@Configuration
class QuartzConfiguration @Autowired()(jobFactory:JobFactory) {




  @Bean(name = Array("SchedulerFactory"))
  @throws[IOException]
  def schedulerFactoryBean: SchedulerFactoryBean = {
    val factory = new SchedulerFactoryBean
    factory.setJobFactory(jobFactory)
    factory.setQuartzProperties(quartzProperties)
    factory
  }

  @Bean
  @throws[IOException]
  def quartzProperties: Properties = {
    val propertiesFactoryBean = new PropertiesFactoryBean
    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"))
    propertiesFactoryBean.afterPropertiesSet()
    propertiesFactoryBean.getObject
  }

  import org.springframework.context.annotation.Bean
  import java.io.IOException

  @Bean(name = Array("scheduler"))
  @throws[IOException]
  def scheduler: Scheduler = schedulerFactoryBean.getScheduler
}
