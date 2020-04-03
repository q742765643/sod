package com.piesat.monitor.entity.kafka

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.{DateFormat, Document, Field, FieldType}

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/1.
  */
@Document(indexName = "kafka_config",`type` = "doc")
class KafkaConfig {

  @Id
  @BeanProperty
  var id:String=_

  @ApiModelProperty("创建时间")
  @BeanProperty
  @Field(`type` = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var createTime:Date=_

  @ApiModelProperty("kafka地址")
  @BeanProperty
  var address:String=_

  @ApiModelProperty("分组")
  @BeanProperty
  var groupId:String=_

  @ApiModelProperty("别名")
  @BeanProperty
  var alias:String=_


}
