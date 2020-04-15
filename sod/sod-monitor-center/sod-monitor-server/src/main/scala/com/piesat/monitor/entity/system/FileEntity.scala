package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/24.
  */
@Data
class FileEntity {
   @ApiModelProperty("采集时间")
   @BeanProperty
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var timestamp:Date=_
   @ApiModelProperty("主机名")
   @BeanProperty
   var hostname:String=_
   @ApiModelProperty("类型")
   @BeanProperty
   var dataset:String=_
   @ApiModelProperty("文件系统设备名")
   @BeanProperty
   var deviceName:String=_
   @ApiModelProperty("磁盘类型")
   @BeanProperty
   var ftype:String=_
   @ApiModelProperty("安装点")
   @BeanProperty
   var mountPoint:String=_
   @ApiModelProperty("可用磁盘空间（以字节为单位）")
   @BeanProperty
   var free:Long=_
   @ApiModelProperty("总磁盘空间（以字节为单位）")
   @BeanProperty
   var total:Long=_
   @ApiModelProperty("使用的磁盘空间（以字节为单位）")
   @BeanProperty
   var usedBytes:Long=_
   @ApiModelProperty("已用磁盘空间的百分比")
   @BeanProperty
   var usedPct:Float=_
   @BeanProperty
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var startTime:Date=_
   @BeanProperty
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var endTime:Date=_


}
