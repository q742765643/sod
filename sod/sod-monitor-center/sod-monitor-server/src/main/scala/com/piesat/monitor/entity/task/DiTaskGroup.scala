package com.piesat.monitor.entity.task

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/6.
  */
class DiTaskGroup {
  /**任务状态*/
  @BeanProperty
  @JsonProperty("TASK_STATE")
  var  taskState:String=_

  @BeanProperty
  var taskDuty:String = null

  @BeanProperty
  var count:Long = _
}
