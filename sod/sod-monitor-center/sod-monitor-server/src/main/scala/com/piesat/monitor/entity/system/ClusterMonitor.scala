package com.piesat.monitor.entity.system

import java.beans.Transient

import io.swagger.annotations.ApiModelProperty

import scala.beans.BeanProperty
import java.util.{Date, List => JavaList}

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat

/**
  * Created by zzj on 2020/4/2.
  */
class ClusterMonitor {

  @ApiModelProperty("ip")
  @BeanProperty
  var ip:String=_

  @ApiModelProperty("cpu使用总的百分比")
  @BeanProperty
  var totalCpuPct:Float=_

  @ApiModelProperty("实际使用的内存百分比")
  @BeanProperty
  var usedPct:Float=_

  @ApiModelProperty("磁盘使用率百分比")
  @BeanProperty
  var filePct:Float=_

  @ApiModelProperty("网络是否正常 1正常")
  @BeanProperty
  var netWork:Int=_

  @ApiModelProperty("进程是否正常 1正常")
  @BeanProperty
  var process:Int=_

  @ApiModelProperty("磁盘是否正常 1正常")
  @BeanProperty
  var fileSystem:Int=_

  @Transient
  @BeanProperty
  var ips:JavaList[String]=_

  @Transient
  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var startTime:Date=_

  @Transient
  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var endTime:Date=_

}
