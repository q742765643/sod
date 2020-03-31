package com.piesat.calculate.entity

import java.util.Date

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/17.
  */
class LogMessege extends BaseEntity {
  @BeanProperty
  var key: String = _
  @BeanProperty
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  var ddatatime: Date = _
  @BeanProperty
  var station: String = _
  @BeanProperty
  var ddataId: String = _
  @BeanProperty
  var id: String = _
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var ddateTime: Date = _

}
