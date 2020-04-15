package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import org.springframework.context.annotation.Bean
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/24.
  */
@Data
class MemoryEntity {
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
  @ApiModelProperty("实际使用的内存（以字节为单位）")
  @BeanProperty
  var usedBytes:Long=_
  @ApiModelProperty("实际可用内存（以字节为单位）")
  @BeanProperty
  var free:Long=_
  @ApiModelProperty("实际使用的内存百分比")
  @BeanProperty
  var usedPct:Float=_
  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var startTime:Date=_
  @BeanProperty
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var endTime:Date=_


}
