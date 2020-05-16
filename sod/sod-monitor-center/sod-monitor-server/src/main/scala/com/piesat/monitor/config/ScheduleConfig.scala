package com.piesat.monitor.config

import com.piesat.monitor.job.{DiTaskConfJob, DiUpdateJob, DropIndexJob}
import com.piesat.monitor.rpc.service.quartz.DiTaskConfService
import com.piesat.monitor.util.quartz.QuartzUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.{DisposableBean, InitializingBean}
import org.springframework.boot.{ApplicationArguments, ApplicationRunner}
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

/**
  * Created by zzj on 2020/4/6.
  */
@Component
class ScheduleConfig @Autowired()(quartzUtil: QuartzUtil) extends ApplicationRunner {
  override def run(applicationArguments: ApplicationArguments): Unit = {
    quartzUtil.addJob("任务监控","任务监控","0 1 0 1/1 * ?",classOf[DiTaskConfJob])
    quartzUtil.addJob("删除过期索引","删除过期索引","0 2 0 1/1 * ?",classOf[DropIndexJob])
    quartzUtil.addJob("DI更新","DI更新","0 */5 * * * ?",classOf[DiUpdateJob])
  }
}
