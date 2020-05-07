package com.piesat.monitor.entity.kafka

import java.beans.Transient
import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty

import scala.beans.BeanProperty
import java.util.{List => JavaList}

import org.springframework.format.annotation.DateTimeFormat

/**
  * Created by zzj on 2020/3/31.
  */
class KafkaMonitor {
  @ApiModelProperty("采集时间")
  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var timestamp:Date=_
  @ApiModelProperty("主机名")
  @BeanProperty
  var hostname:String=_
  @ApiModelProperty("kafka地址")
  @BeanProperty
  var address:String=_
  @ApiModelProperty("类型")
  @BeanProperty
  var dataset:String=_
  @ApiModelProperty("队列名称")
  @BeanProperty
  var topic:String=_
  @ApiModelProperty("积压数")
  @BeanProperty
  var consumerLag:Long=_
  @ApiModelProperty("分组")
  @BeanProperty
  var groupId:String=_
  @BeanProperty
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var startTime:Date=_
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var endTime:Date=_
  @BeanProperty
  var groupIds:JavaList[String]=_
  @BeanProperty
  var addresss:JavaList[String]=_
  @Transient
  @ApiModelProperty("别名")
  @BeanProperty
  var alias:String=_

}
