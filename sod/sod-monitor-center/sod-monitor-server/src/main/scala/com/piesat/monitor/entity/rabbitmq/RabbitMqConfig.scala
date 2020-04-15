package com.piesat.monitor.entity.rabbitmq

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
@Document(indexName = "rabbitmq_config",`type` = "doc")
class RabbitMqConfig {
  @Id
  @BeanProperty
  var id:String=_
  @ApiModelProperty("地址")
  @BeanProperty
  var address:String=_
  @ApiModelProperty("别名")
  @BeanProperty
  var alias:String=_

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
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var endTime:Date=_

}
