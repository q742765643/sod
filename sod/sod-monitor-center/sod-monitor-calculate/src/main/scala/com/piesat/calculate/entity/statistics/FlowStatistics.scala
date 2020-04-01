package com.piesat.calculate.entity.statistics

import java.util.Date

import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/30.
  */
class FlowStatistics extends Serializable{
  @BeanProperty
  var id: String = _
  @BeanProperty
  var name: String = _
  @BeanProperty
  var dataType: String = _
  @BeanProperty
  var sType: String = _
  @BeanProperty
  var  collectionRealIncome:Long=_
  @BeanProperty
  var timely:Long=_
  @BeanProperty
  var  distributeRealIncome:Long=_
  @BeanProperty
  var  putRealIncome:Long=_
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var ddateTime: Date = _
  @BeanProperty
  var iiiii: String = _
}
