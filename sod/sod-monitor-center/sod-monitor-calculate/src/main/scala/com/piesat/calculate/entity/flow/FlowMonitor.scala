package com.piesat.calculate.entity.flow

import java.util.Date

import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/30.
  */
class FlowMonitor {
  /**
    * 文件级资料名称
    */
  @BeanProperty
  var name: String = _
  @JsonProperty("IIiii")
  @BeanProperty
  var iiiii: String = _
  @JsonProperty("DATA_TYPE")
  @BeanProperty
  var dataType: String = _
  @BeanProperty
  var id: String = _
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var ddateTime: Date = _
  @JsonProperty("collection_receivable")
  @BeanProperty
  var  collectionReceivable:Long=_

  @JsonProperty("collection_realIncome")
  @BeanProperty
  var  collectionRealIncome:Long=_
  @BeanProperty
  var timely:Long=_

  @JsonProperty("distribute_receivable")
  @BeanProperty
  var  distributeReceivable:Long=_

  @JsonProperty("distribute_realIncome")
  @BeanProperty
  var  distributeRealIncome:Long=_

  @JsonProperty("put_receivable")
  @BeanProperty
  var  putReceivable:Long=_

  @JsonProperty("put_realIncome")
  @BeanProperty
  var  putRealIncome:Long=_

  @JsonProperty("material_type")
  @BeanProperty
  var  materialType:String=_

  override def toString = s"FlowMonitor($name, $iiiii, $dataType, $id, $ddateTime, $collectionReceivable, $collectionRealIncome, $timely, $distributeReceivable, $distributeRealIncome, $putReceivable, $putRealIncome)"
}
