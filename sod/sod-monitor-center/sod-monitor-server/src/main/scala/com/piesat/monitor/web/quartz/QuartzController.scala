package com.piesat.monitor.web.quartz

import com.piesat.monitor.entity.ssh.SshEntity
import com.piesat.monitor.rpc.service.ssh.SshService
import com.piesat.monitor.util.quartz.QuartzUtil
import com.piesat.util.ResultT
import com.piesat.util.page.{PageBean, PageForm}
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "任务管理接口", tags =  Array ("任务管理接口")  )
@RequestMapping(Array("/quartz"))
class QuartzController  @Autowired()( quartzUtil: QuartzUtil) {
  @GetMapping(Array("/triggerJob"))
  @ApiOperation(value = "立即执行", notes = "立即执行")
  def triggerJob(jobName: String, jobGroup: String):ResultT[String]={
    var resultT=new ResultT[String]
    quartzUtil.triggerJob(jobName,jobGroup)
    return resultT
  }

}
