package com.piesat.monitor.entity.task

import java.beans.Transient
import java.util.Date

import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.{DateFormat, Document, Field, FieldType}
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/3.
  */
@Data
class DiTaskExecute{
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

  @BeanProperty
  var taskDuty:String = null

  /**计划开始时间*/
  @JsonProperty("START_TIME_S")
  @Field(`type` = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
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
  @JsonProperty("TASK_ERROR_TIME")
  @Field(`type` = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
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
  @JsonProperty("RECORD_TIME")
  @Field(`type` = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var recordTime:Date=_

  @BeanProperty
  @JsonProperty("SEND_PHYS")
  var sendPhys:String=_

  @BeanProperty
  @Id
  var id: String = _

  @Field(`type` = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var ddateTime: Date = _

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Transient
  @BeanProperty
  var startTime:Date=_

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Transient
  @BeanProperty
  var endTime:Date=_

  @Transient
  @BeanProperty
  var ltaskState:String=_
}
