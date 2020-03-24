package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Data

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/24.
  */
@Data
class FileEntity {
   @BeanProperty
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var timestamp:Date=_
   @BeanProperty
   var hostname:String=_
   @BeanProperty
   var dataset:String=_
   @BeanProperty
   var deviceName:String=_
   @BeanProperty
   var ftype:String=_
   @BeanProperty
   var mountPoint:String=_
   @BeanProperty
   var free:Long=_
   @BeanProperty
   var total:Long=_
   @BeanProperty
   var usedBytes:Long=_
   @BeanProperty
   var usedPct:Float=_
   @BeanProperty
   var startTime:String=_
   @BeanProperty
   var endTime:String=_


}
