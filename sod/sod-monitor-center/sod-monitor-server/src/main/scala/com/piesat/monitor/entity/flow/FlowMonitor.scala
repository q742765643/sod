package com.piesat.monitor.entity.flow

import java.beans.Transient
import java.util.Date

import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}
import io.swagger.annotations.ApiModelProperty
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/30.
  */
class FlowMonitor {
  @ApiModelProperty("id")
  @BeanProperty
  var id: String = _
  /**
    * 文件级资料名称
    */
  @ApiModelProperty("资料名称")
  @BeanProperty
  var name: String = _

  @ApiModelProperty("站号")
  @JsonProperty("IIiii")
  @BeanProperty
  var iiiii: String = _

  @ApiModelProperty("四级编码")
  @JsonProperty("DATA_TYPE")
  @BeanProperty
  var dataType: String = _


  @ApiModelProperty("资料时次")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var ddateTime: Date = _

  @ApiModelProperty("采集应收")
  @JsonProperty("collection_receivable")
  @BeanProperty
  var  collectionReceivable:Long=_

  @ApiModelProperty("采集实收")
  @JsonProperty("collection_realIncome")
  @BeanProperty
  var  collectionRealIncome:Long=_

  @ApiModelProperty("及时")
  @BeanProperty
  var timely:Long=_

  @ApiModelProperty("分发应收")
  @JsonProperty("distribute_receivable")
  @BeanProperty
  var  distributeReceivable:Long=_

  @ApiModelProperty("分发实收")
  @JsonProperty("distribute_realIncome")
  @BeanProperty
  var  distributeRealIncome:Long=_

  @ApiModelProperty("解码应收")
  @JsonProperty("put_receivable")
  @BeanProperty
  var  putReceivable:Long=_

  @ApiModelProperty("解码实收")
  @JsonProperty("put_realIncome")
  @BeanProperty
  var  putRealIncome:Long=_

  @ApiModelProperty("类型")
  @JsonProperty("material_type")
  @BeanProperty
  var  materialType:String=_

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
