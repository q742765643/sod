package com.piesat.calculate.entity.task

import java.util.Date

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}
import com.piesat.calculate.entity.BaseEntity

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/3.
  */
class DiTaskConfiguration extends BaseEntity{
  /**
    * 任务编号
    */
  @BeanProperty
  var taskId: String = null
  /**
    * 任务所属系统
    */
  @BeanProperty
  var system:String = null
  /**
    * 任务名称
    */
  @BeanProperty
  var taskName:String = null
  /**
    * 任务类型 1.清除,2.备份,3.迁移
    */
  @BeanProperty
  var taskDuty:String = null
  /**
    * cron表达式
    */
  @BeanProperty
  var taskCron:String = null
  /**
    * 超时时间 分钟
    */
  @BeanProperty
  var overtime:Int = 0
  /**
    *重试次数
    */
  @BeanProperty
  var taskMaxTime:Int = 0

  /**
    * 任务执行时间偏移量
    */
  @BeanProperty
  var offset:Int = 0
  /**
    * 告警时间间隔
    */
  @BeanProperty
  var alarmtime:Int = 0

  /**
    * 是否告警 0 不告警 1告警
    */
  @BeanProperty
  var isAlarm:Int = 0

  /**
    * 四级编码
    */
  @BeanProperty
  var dataType:String = null

  /**
    * 物理库名称
    */
  @BeanProperty
  @JsonProperty("SEND_PHYS")
  var sendPhys:String = null

  /**
    * 环节
    */
  @BeanProperty
  var handleProcess:Int = 0
  /**
    * 样本个树
    */
  @BeanProperty
  var src:Int = 0
  /**
    * 算法名称
    */
  @BeanProperty
  @JsonProperty("PROD_SYS")
  var prodSys:String = null

  @BeanProperty
  var isDelete:Int = 0

  @BeanProperty
  var id: String = _

  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var ddateTime: Date = _
}
