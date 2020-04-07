package com.piesat.monitor.entity.flow

import java.util.Date

import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.{DateFormat, Document, Field, FieldType}

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/26.
  */
@Data
@Document(indexName = "play_bill",`type` = "doc")
class PlayBill {
  @Id
  @BeanProperty
  var id:String=_
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


  @ApiModelProperty("采集应收")
  @JsonProperty("collection_receivable")
  @BeanProperty
  var  collectionReceivable:Long=_


  @ApiModelProperty("分发应收")
  @JsonProperty("distribute_receivable")
  @BeanProperty
  var  distributeReceivable:Long=_

  @ApiModelProperty("解密入库应收")
  @JsonProperty("put_receivable")
  @BeanProperty
  var  putReceivable:Long=_

  @ApiModelProperty("资料类型")
  @JsonProperty("material_type")
  @BeanProperty
  var  materialType:String=_

  @ApiModelProperty("创建时间")
  @BeanProperty
  @Field(`type` = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var createTime:Date=_

}
