package com.piesat.calculate.entity.task

import java.util.Date

import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}
import com.piesat.calculate.entity.BaseEntity

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/3.
  */
class DiTaskExecute extends BaseEntity{
  /**任务编号*/
  @BeanProperty
  @JsonProperty("TASK_ID")
  var taskId:String=_

  /**当前任务编号*/
  @BeanProperty
  @JsonProperty("CURRENT_TASK_ID")
  var currentTaskId:String=_

  /**任务名称*/
  @BeanProperty
  @JsonProperty("TASK_NAME")
  var  taskName:String=_

  /**业务系统*/
  @BeanProperty
  @JsonProperty("SYSTEM")
  var system:String=_

  /**产品标识*/
  @BeanProperty
  @JsonProperty("DATA_TYPE")
  var dataType:String=_

  /**计划开始时间*/
  @BeanProperty
  @JsonProperty("START_TIME_S")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var startTimeS:Date=_

  /**计划开始时间*/
  @BeanProperty
  @JsonProperty("START_TIME_L")
  var startTimeL:Long=_

  /**实际开始时间*/
  @BeanProperty
  @JsonProperty("START_TIME_A")
  var startTimeA:Long=_

  /**实际结束时间*/
  @BeanProperty
  @JsonProperty("END_TIME_A")
  var endTimeA:Long=_

  /**任务状态*/
  @BeanProperty
  @JsonProperty("TASK_STATE")
  var  taskState:String=_

  /**任务执行详情*/
  @BeanProperty
  @JsonProperty("TASK_DETAIL")
  var  taskDetail:String=_

  /**任务异常时间*/
  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @JsonProperty("TASK_ERROR_TIME")
  var taskErrorTime:Date=_

  /**任务异常状态说明*/
  @BeanProperty
  @JsonProperty("TASK_ERROR_DETAIL")
  var  taskErrorDetail:String=_

  /**任务异常原因说明*/
  @BeanProperty
  @JsonProperty("TASK_ERROR_REASON")
  var taskErrorReason:String=_

  /**记录时间*/
  @BeanProperty
  @JsonProperty("RECORD_TIME")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var recordTime:Date=_

  @BeanProperty
  @JsonProperty("SEND_PHYS")
  var sendPhys:String=_

  @BeanProperty
  var id: String = _

  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var ddateTime: Date = _


  @BeanProperty
  var taskDuty:String = null
}
