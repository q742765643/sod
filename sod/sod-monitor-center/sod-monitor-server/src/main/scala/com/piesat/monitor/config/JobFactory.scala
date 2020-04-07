package com.piesat.monitor.config

import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.scheduling.quartz.AdaptableJobFactory
import org.springframework.stereotype.Component

/**
  * Created by zzj on 2020/4/6.
  */
@Component
class JobFactory @Autowired()(capableBeanFactory:AutowireCapableBeanFactory)extends AdaptableJobFactory{
  @throws[Exception]
  override protected def createJobInstance(bundle: TriggerFiredBundle): Object = {
    val jobInstance = super.createJobInstance(bundle)
    capableBeanFactory.autowireBean(jobInstance)
    jobInstance
  }

}
