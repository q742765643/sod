package com.piesat.calculate.entity.transfer

import java.util.Date

import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}
import com.piesat.calculate.entity.BaseEntity

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/30.
  */
class StationLevelEntity extends BaseEntity {
  /**
    * 类型 RT.CTS.FILE.DI
    */
  @JsonProperty("type")
  @BeanProperty
  var stype: String = _
  /**
    * 文件级资料名称
    */
  @BeanProperty
  var name: String = _
  /**
    * 文件级资料详细信息
    */
  @BeanProperty
  var message: String = _

  @JsonProperty("fields")
  @BeanProperty
  var stationLevelFiledEntity: StationLevelFiledEntity = _

  @BeanProperty
  var isCount: Int = _

  @BeanProperty
  var groupKey: String = _

  @BeanProperty
  var id: String=_

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var ddateTime: Date=_

}
