package com.piesat.calculate.entity

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/27.
  */
abstract class BaseEntity extends Serializable {
  var id: String
  var ddateTime: Date


}
