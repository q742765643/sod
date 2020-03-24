package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Data

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/24.
  */
@Data
class ProcessEntity {
   @BeanProperty
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var timestamp:Date=_
   @BeanProperty
   var hostname:String=_
   @BeanProperty
   var dataset:String=_
   @BeanProperty
   var cmdline:String=_
   @BeanProperty
   var cwd:String=_
   @BeanProperty
   var name:String=_
   @BeanProperty
   var pgid:String=_
   @BeanProperty
   var pid:String=_
   @BeanProperty
   var ppid:String=_
   @BeanProperty
   var state:String=_
   @BeanProperty
   var username:String=_
   @BeanProperty
   var startTime:String=_
   @BeanProperty
   var endTime:String=_




}
