package com.piesat.monitor.entity.flow

import java.beans.Transient
import java.util.Date

import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/30.
  */
class StationLevel {

  @BeanProperty
  var id: String=_
  /**
    * 类型 RT.CTS.FILE.DI
    */
  @ApiModelProperty("DI类型")
  @JsonProperty("type")
  @BeanProperty
  var stype: String = _
  /**
    * 文件级资料名称
    */
  @ApiModelProperty("资料名称")
  @BeanProperty
  var name: String = _
  /**
    * 文件级资料详细信息
    */
  @ApiModelProperty("资料详细信息")
  @BeanProperty
  var message: String = _

  @ApiModelProperty("是否重复 1表示重复")
  @BeanProperty
  var isCount: Int = _

  @ApiModelProperty("分组key")
  @BeanProperty
  var groupKey: String = _


  @ApiModelProperty("资料时间")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @BeanProperty
  var ddateTime: Date=_

  /**
    * 本环节处理后的资料编码
    */
  @ApiModelProperty("本环节处理后的资料编码")
  @JsonProperty("DATA_TYPE")
  @BeanProperty
  var dataType: String = _
  /**
    * 父代资料编码（若前后无变化同资料编码）
    */
  @ApiModelProperty("父代资料编码")
  @JsonProperty("DATA_TYPE_1")
  @BeanProperty
  var dataType1: String = _

  /**
    * 报告类别
    */
  @ApiModelProperty("报告类别")
  @JsonProperty("TT")
  @BeanProperty
  var tt: String = _

  /**
    * 台站号
    */
  @ApiModelProperty("台站号")
  @JsonProperty("IIiii")
  @BeanProperty
  var iiiii: String = _

  /**
    * 经度
    */
  @ApiModelProperty("经度")
  @JsonProperty("LONGTITUDE")
  @BeanProperty
  var longtitude: String = _

  /**
    * 纬度
    */
  @ApiModelProperty("纬度")
  @JsonProperty("LATITUDE")
  @BeanProperty
  var latitude: String = _

  /**
    * 高度
    */
  @ApiModelProperty("高度")
  @JsonProperty("HEIGHT")
  @BeanProperty
  var height: String = _

  /**
    * 资料更正标识
    */
  @ApiModelProperty("资料更正标识")
  @JsonProperty("DATA_UPDATE_FLAG")
  @BeanProperty
  var dataUpdateFlag: String = _

  /**
    * 上游系统编码
    */
  @ApiModelProperty("上游系统编码")
  @JsonProperty("RECEIVE")
  @BeanProperty
  var receive: String = _
  /**
    * 下游系统编码
    */
  @ApiModelProperty("下游系统编码")
  @JsonProperty("SEND")
  @BeanProperty
  var send: String = _
  /**
    * 资料生成传输时次
    */
  @ApiModelProperty("资料生成传输时次")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @JsonProperty("TRAN_TIME")
  @BeanProperty
  var tranTime: Date = _
  /**
    * 观测时次或预报时次
    */
  @ApiModelProperty("观测时次或预报时次")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @JsonProperty("DATA_TIME")
  @BeanProperty
  var dataTime: Date = _
  /**
    * 业务系统名称
    */
  @ApiModelProperty("业务系统名称")
  @JsonProperty("SYSTEM")
  @BeanProperty
  var system: String = _
  /**
    * 业务系统关键业务环节
    */
  @ApiModelProperty("业务系统关键业务环节")
  @JsonProperty("PROCESS_LINK")
  @BeanProperty
  var processLink: String = _
  /**
    * 业务环节开始处理时间
    */
  @ApiModelProperty("业务环节开始处理时间")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @JsonProperty("PROCESS_START_TIME")
  @BeanProperty
  var processStartTime: Date = _
  /**
    * 业务环节结束处理时间
    */
  @ApiModelProperty("业务环节结束处理时间")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @JsonProperty("PROCESS_END_TIME")
  @BeanProperty
  var processEndTime: Date = _
  /**
    * 处理前文件名
    */
  @ApiModelProperty("处理前文件名")
  @JsonProperty("FILE_NAME_O")
  @BeanProperty
  var fileNameO: String = _
  /**
    * 处理后文件名
    */
  @ApiModelProperty("处理后文件名")
  @JsonProperty("FILE_NAME_N")
  @BeanProperty
  var fileNameN: String = _
  /**
    * 每个业务环节处理的系统运行状态
    */
  @ApiModelProperty("每个业务环节处理的系统运行状态")
  @JsonProperty("PROCESS_STATE")
  @BeanProperty
  var processState: String = _
  /**
    * 业务环节处理的业务状态
    */
  @ApiModelProperty("业务环节处理的业务状态")
  @JsonProperty("BUSINESS_STATE")
  @BeanProperty
  var businessState: String = _
  /**
    * DI记录时间
    */
  @ApiModelProperty("DI记录时间")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @JsonProperty("RECORD_TIME")
  @BeanProperty
  var recordTime: Date = _

  @BeanProperty
  @Transient
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  var startTime:Date=_
  @BeanProperty
  @Transient
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
  var endTime:Date=_

}
