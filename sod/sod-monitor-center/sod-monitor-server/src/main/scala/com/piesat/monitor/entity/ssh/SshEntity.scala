package com.piesat.monitor.entity.ssh

import java.util.Date

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}
import lombok.Data
import org.elasticsearch.search.DocValueFormat.DateTime
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations._

import scala.beans.BeanProperty


/**
  * Created by zzj on 2020/3/23.
  */
@Data
//@Document(indexName = "ssh-#{ T(com.piesat.monitor.util.IndexUtil).getDateStr()}",`type` = "ssh")
@Document(indexName = "ssh",`type` = "doc")
class SshEntity {

  @Id
  @BeanProperty
  var id:String=_
  @BeanProperty
  var ip:String=_

  @BeanProperty
  var port:Integer=_
  @BeanProperty
  var userName:String=_

  @BeanProperty
  var passWord:String=_

  @Field(`type` = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var createTime:Date=_

  @BeanProperty
  var hostName:String=_

  @BeanProperty
  var alias:String=_

}
