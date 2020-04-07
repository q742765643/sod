package com.piesat.monitor.config

import com.piesat.monitor.job.DiTaskConfJob
import com.piesat.monitor.rpc.service.quartz.DiTaskConfService
import com.piesat.monitor.util.quartz.QuartzUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.{DisposableBean, InitializingBean}
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

/**
  * Created by zzj on 2020/4/6.
  */
@Component
class ScheduleConfig @Autowired()(quartzUtil: QuartzUtil) extends InitializingBean with DisposableBean {
  override def afterPropertiesSet(): Unit = {
    quartzUtil.addJob("任务监控","任务监控","0 0 0 1/1 * ?",classOf[DiTaskConfJob])
  }

  override def destroy(): Unit = {

  }
}
