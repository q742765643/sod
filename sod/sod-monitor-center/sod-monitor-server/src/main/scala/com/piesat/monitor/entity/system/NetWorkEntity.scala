package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/25.
  */
@Data
class NetWorkEntity {
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
  @ApiModelProperty("网卡入口包大小 单位字节")
  @BeanProperty
  var inBytes:Long=_
  @ApiModelProperty("网卡出口包大小 单位字节")
  @BeanProperty
  var outBytes:Long=_
  @ApiModelProperty("网卡入口包数量")
  @BeanProperty
  var inPackets:Long=_
  @ApiModelProperty("网卡出口网卡包数量")
  @BeanProperty
  var outPackets:Long=_
  @BeanProperty
  var sumInBytes:Long=_
  @BeanProperty
  var sumOutBytes:Long=_
  @BeanProperty
  var inSpeed:Float=_
  @BeanProperty
  var outSpeed:Float=_
  @BeanProperty
  var transferredIn:Long=_
  @BeanProperty
  var transferredOut:Long=_
  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var startTime:Date=_
  @BeanProperty
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var endTime:Date=_
}
