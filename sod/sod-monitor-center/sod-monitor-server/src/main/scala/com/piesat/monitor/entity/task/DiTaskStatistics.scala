package com.piesat.monitor.entity.task

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/6.
  */
class DiTaskStatistics {
  /**任务状态*/
  @BeanProperty
  var  taskState:String=_

  @BeanProperty
  var  backup:Long=_

  @BeanProperty
  var  move:Long=_


  @BeanProperty
  var  clear:Long=_
}
