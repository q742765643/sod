package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/24.
  */
@Data
class CpuEntity {
   @ApiModelProperty("采集时间")
   @BeanProperty
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var timestamp:Date=_
   @ApiModelProperty("主机名")
   @BeanProperty
   var hostname:String=_
   @ApiModelProperty("类型")
   @BeanProperty
   var dataset:String=_
   @ApiModelProperty("cpu核数")
   @BeanProperty
   var cpuCores:Long=_
   @ApiModelProperty("用户空间占用CPU百分比")
   @BeanProperty
   var cpuUserPct:Float=_
   @ApiModelProperty("内核空间占用CPU百分比")
   @BeanProperty
   var cpuSystemPct:Float=_
   @ApiModelProperty("进程改变占用CPU百分比")
   @BeanProperty
   var cpuNicePct:Float=_
   @ApiModelProperty("空闲CPU百分比")
   @BeanProperty
   var cpuIdlePct:Float=_
   @ApiModelProperty("等待输入输出的CPU时间百分比")
   @BeanProperty
   var cpuIowaitPct:Float=_
   @ApiModelProperty("服务和处理软件中断所花费的CPU时间百分比")
   @BeanProperty
   var cpuSoftirqPct:Float=_
   @ApiModelProperty("服务和处理硬件中断所花费的CPU时间百分比")
   @BeanProperty
   var cpuIrqPct:Float=_
   @ApiModelProperty("cpu使用总的百分比")
   @BeanProperty
   var totalPct:Float=_
   @BeanProperty
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   var startTime:Date=_
   @BeanProperty
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var endTime:Date=_




}
