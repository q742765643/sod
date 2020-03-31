package com.piesat.calculate.entity

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/17.
  */
class StationCount extends BaseEntity {
  @BeanProperty
  var key: String = _
  @BeanProperty
  var count: Long = _
  @BeanProperty
  var windowEnd: Long = _

  @BeanProperty
  var windowStart: Long = _

  @BeanProperty
  var id: String = _
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var ddateTime: Date = _


  override def toString = s"StationCount($id, $key, $count, $windowEnd, $windowStart)"
}
