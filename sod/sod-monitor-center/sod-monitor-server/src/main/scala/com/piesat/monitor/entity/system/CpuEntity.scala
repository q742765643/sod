package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Data

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/24.
  */
@Data
class CpuEntity {
   @BeanProperty
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var timestamp:Date=_
   @BeanProperty
   var hostname:String=_
   @BeanProperty
   var dataset:String=_
   @BeanProperty
   var cpuCores:Long=_
   @BeanProperty
   var cpuUserPct:Float=_
   @BeanProperty
   var cpuSystemPct:Float=_
   @BeanProperty
   var cpuNicePct:Float=_
   @BeanProperty
   var cpuIdlePct:Float=_
   @BeanProperty
   var cpuIowaitPct:Float=_
   @BeanProperty
   var cpuSoftirqPct:Float=_
   @BeanProperty
   var cpuIrqPct:Float=_
   @BeanProperty
   var startTime:String=_
   @BeanProperty
   var endTime:String=_




}
