package com.piesat.calculate.entity

import java.util.Date

import com.alibaba.fastjson.annotation.JSONField

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/17.
  */
class LogMessege extends BaseEntity{
   @BeanProperty
   var id:String=_
   @BeanProperty
   var key:String=_
   @BeanProperty
   @JSONField(format="yyyy-MM-dd HH:mm:ss")
   var ddatatime:Date=_
   @BeanProperty
   var station:String=_
   @BeanProperty
   var ddataId:String=_

}
