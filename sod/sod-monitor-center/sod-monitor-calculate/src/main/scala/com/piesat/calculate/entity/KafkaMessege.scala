package com.piesat.calculate.entity

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/17.
  */
class KafkaMessege extends Serializable {
  @BeanProperty
  var timestamp: String = _
  @BeanProperty
  var metadata: String = _
  @BeanProperty
  var agent: String = _
  @BeanProperty
  var log: String = _
  @BeanProperty
  var message: String = _
  @BeanProperty
  var fields: String = _
  @BeanProperty
  var ecs: String = _
  @BeanProperty
  var host: String = _
}
