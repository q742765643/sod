package com.piesat.monitor.entity.process

import java.beans.Transient
import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.{DateFormat, Document, Field, FieldType}
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/1.
  */
@Document(indexName = "process_config",`type` = "doc")
class ProcessConfig {
  @Id
  @BeanProperty
  var id:String=_
  @ApiModelProperty("ip")
  @BeanProperty
  var ip:String=_
  @BeanProperty
  var processName:String=_
  @ApiModelProperty("进程状态")
  @BeanProperty
  var processState:String=_
  @ApiModelProperty("采集时间")
  @BeanProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var timestamp:Date=_

  @Field(`type` = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var createTime:Date=_

  @BeanProperty
  @Transient
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var startTime:Date=_
  @BeanProperty
  @Transient
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var endTime:Date=_

}
