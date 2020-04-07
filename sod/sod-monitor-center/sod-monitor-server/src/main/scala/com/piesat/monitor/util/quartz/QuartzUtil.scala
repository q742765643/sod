package com.piesat.monitor.util.quartz

import java.util
import java.util.{ArrayList, Date, List}

import lombok.extern.slf4j.Slf4j
import org.quartz._
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import org.quartz.CronScheduleBuilder
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.SchedulerException
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey
import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.support.CronSequenceGenerator
import org.springframework.stereotype.{Component, Service}

import scala.language.implicitConversions
import scala.collection.JavaConversions._

/**
  * Created by zzj on 2020/4/4.
  */
@Service
class QuartzUtil @Autowired()(scheduler:Scheduler)  {
  private val logger=Logger(LoggerFactory.getLogger(classOf[QuartzUtil]))



  @throws[SchedulerException]
  def checkExists(jobName: String, jobGroup: String): Boolean = {
    val triggerKey = TriggerKey.triggerKey(jobName, jobGroup)
    scheduler.checkExists(triggerKey)
  }



  @throws[SchedulerException]
  def addJob(jobName: String, jobGroup: String, cronExpression: String,jobClass:Class[_<:Job]): Boolean = { // TriggerKey : name + group
    val triggerKey = TriggerKey.triggerKey(jobName, jobGroup)
    val jobKey = new JobKey(jobName, jobGroup)
    if (checkExists(jobName, jobGroup)) {
      logger.info(">>>>>>>>> addJob fail, job already exist, jobGroup:{}, jobName:{}", jobGroup, jobName)
      return false
    }
    val cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing
    val cronTrigger = TriggerBuilder.newTrigger.withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build
    val jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build
    val date = scheduler.scheduleJob(jobDetail, cronTrigger)
    logger.info(">>>>>>>>>>> addJob success, jobDetail:{}, cronTrigger:{}, date:{}", jobDetail, cronTrigger, date)
    true
  }



  @throws[SchedulerException]
  def rescheduleJob(jobGroup: String, jobName: String, cronExpression: String): Boolean = {
    if (!checkExists(jobName, jobGroup)) {
      logger.info(">>>>>>>>>>> rescheduleJob fail, job not exists, JobGroup:{}, JobName:{}", jobGroup, jobName)
      return false
    }
    val triggerKey = TriggerKey.triggerKey(jobName, jobGroup)
    var oldTrigger = scheduler.getTrigger(triggerKey).asInstanceOf[CronTrigger]
    if (oldTrigger != null) { // avoid repeat
      val oldCron = oldTrigger.getCronExpression
      if (oldCron == cronExpression) return true
      val cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing
      oldTrigger = oldTrigger.getTriggerBuilder.withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build
      scheduler.rescheduleJob(triggerKey, oldTrigger)
    }
    else {
      val cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing
      val cronTrigger = TriggerBuilder.newTrigger.withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build
      val jobKey = new JobKey(jobName, jobGroup)
      val jobDetail = scheduler.getJobDetail(jobKey)
      val triggerSet = new  util.HashSet[Trigger]();
      triggerSet.add(cronTrigger)
      scheduler.scheduleJob(jobDetail, triggerSet, true)
    }
    logger.info(">>>>>>>>>>> resumeJob success, JobGroup:{}, JobName:{}", jobGroup, jobName)
    true
  }


  @throws[SchedulerException]
  def removeJob(jobName: String, jobGroup: String): Boolean = {
    val triggerKey = TriggerKey.triggerKey(jobName, jobGroup)
    var result = false
    if (checkExists(jobName, jobGroup)) {
      result = scheduler.unscheduleJob(triggerKey)
      logger.info(">>>>>>>>>>> removeJob, triggerKey:{}, result [{}]", triggerKey, result.asInstanceOf[Object])
    }
    true
  }


  @throws[SchedulerException]
  def pauseJob(jobName: String, jobGroup: String): Boolean = { // TriggerKey : name + group
    val triggerKey = TriggerKey.triggerKey(jobName, jobGroup)
    var result = false
    if (checkExists(jobName, jobGroup)) {
      scheduler.pauseTrigger(triggerKey)
      result = true
      logger.info(">>>>>>>>>>> pauseJob success, triggerKey:{}", triggerKey)
    }
    else logger.info(">>>>>>>>>>> pauseJob fail, triggerKey:{}", triggerKey)
    result
  }


  @throws[SchedulerException]
  def resumeJob(jobName: String, jobGroup: String): Boolean = { // TriggerKey : name + group
    val triggerKey = TriggerKey.triggerKey(jobName, jobGroup)
    var result = false
    if (checkExists(jobName, jobGroup)) {
      scheduler.resumeTrigger(triggerKey)
      result = true
      logger.info(">>>>>>>>>>> resumeJob success, triggerKey:{}", triggerKey)
    }
    else logger.info(">>>>>>>>>>> resumeJob fail, triggerKey:{}", triggerKey)
    result
  }



  @throws[SchedulerException]
  def triggerJob(jobName: String, jobGroup: String): Boolean = { // TriggerKey : name + group
    val jobKey = new JobKey(jobName, jobGroup)
    var result = false
    if (checkExists(jobName, jobGroup)) {
      scheduler.triggerJob(jobKey)
      result = true
      logger.info(">>>>>>>>>>> runJob success, jobKey:{}", jobKey)
    }
    else logger.info(">>>>>>>>>>> runJob fail, jobKey:{}", jobKey)
    result
  }


  def getNextTime(cronExpression: String,nextTimePoint: Date): Date = {
    val cronTimeList = new util.ArrayList[Date]
    try {
      val cronSequenceGenerator = new CronSequenceGenerator(cronExpression)
      var i = 0
      while ( {
        i < 1
      }) {
        var nextTime:Date = cronSequenceGenerator.next(nextTimePoint)
        cronTimeList.add(nextTime)
        i+=1
      }
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
    cronTimeList.get(0)
  }
}
