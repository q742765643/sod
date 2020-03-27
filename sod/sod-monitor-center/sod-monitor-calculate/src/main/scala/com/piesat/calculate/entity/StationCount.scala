package com.piesat.calculate.entity

import java.util.Date

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/17.
  */
class StationCount  extends BaseEntity{
  @BeanProperty
  var id:String=_
  @BeanProperty
  var key:String=_
  @BeanProperty
  var count : Long = _
  @BeanProperty
  var windowEnd:Long = _

  @BeanProperty
  var windowStart:Long = _

  var ddatatime:Date=_


  override def toString = s"StationCount($id, $key, $count, $windowEnd, $windowStart)"
}
