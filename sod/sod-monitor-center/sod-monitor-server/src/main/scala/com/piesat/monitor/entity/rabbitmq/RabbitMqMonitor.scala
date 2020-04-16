package com.piesat.monitor.entity.rabbitmq

import java.beans.Transient
import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/1.
  */
@Data
class RabbitMqMonitor  {
  @ApiModelProperty("采集时间")
  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var timestamp:Date=_
  @ApiModelProperty("主机名")
  @BeanProperty
  var hostname:String=_
  @ApiModelProperty("地址")
  @BeanProperty
  var address:String=_

  @BeanProperty
  var queueName:String=_

  @BeanProperty
  var totalCount:Long=_

  @BeanProperty
  var readyCount:Long=_

  @BeanProperty
  var unacknowledgedCount:Long=_

  @BeanProperty
  var readyDetailsRate:Float=_

  @BeanProperty
  var totalDetailsRate:Float=_

  @BeanProperty
  var unacknowledgedDetailsRate:Float=_

  @ApiModelProperty("类型")
  @BeanProperty
  var dataset:String=_

  @BeanProperty
  @Transient
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var startTime:Date=_
  @BeanProperty
  @Transient
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var endTime:Date=_

  @Transient
  @ApiModelProperty("别名")
  @BeanProperty
  var alias:String=_
}
