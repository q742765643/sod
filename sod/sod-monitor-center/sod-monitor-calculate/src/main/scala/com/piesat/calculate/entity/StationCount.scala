package com.piesat.calculate.entity

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/17.
  */
class StationCount {

  @BeanProperty
  var key:String=_
  @BeanProperty
  var count : Long = _
  @BeanProperty
  var windowEnd:Long = _

  override def toString = s"StationCount(key=$key, count=$count, windowEnd=$windowEnd)"
}
