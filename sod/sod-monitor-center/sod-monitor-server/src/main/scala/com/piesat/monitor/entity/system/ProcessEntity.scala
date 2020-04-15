package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.{JsonFormat, JsonProperty}
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.{DateFormat, Document, Field, FieldType}
import org.springframework.format.annotation.DateTimeFormat

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/24.
  */
@Data
@Document(indexName = "metricbeat-7.6.1-#{ T(com.piesat.monitor.util.IndexUtil).getDateStr()}",`type` = "doc")
class ProcessEntity {
   @Id
   @BeanProperty
   var id:String=_

   @ApiModelProperty("采集时间")
   @BeanProperty
   @JsonProperty("@timestamp")
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var timestamp:Date=_

   @ApiModelProperty("主机名")
   @BeanProperty
   @JsonProperty("host.name")
   var hostname:String=_

   @ApiModelProperty("类型")
   @BeanProperty
   @JsonProperty("event.dataset")
   var dataset:String=_

   @ApiModelProperty("启动进程的完整命令行")
   @BeanProperty
   @JsonProperty("system.process.cmdline")
   var cmdline:String=_

   @ApiModelProperty("别名")
   @BeanProperty
   @JsonProperty("process.args")
   var cwd:String=_

   @ApiModelProperty("别名")
   @BeanProperty
   @JsonProperty("process.name")
   var name:String=_

   @ApiModelProperty("pid")
   @BeanProperty
   @JsonProperty("process.pgid")
   var pgid:String=_

   @ApiModelProperty("pid")
   @BeanProperty
   @JsonProperty("process.pid")
   var pid:String=_

   @ApiModelProperty("ppid")
   @JsonProperty("process.ppid")
   @BeanProperty
   var ppid:String=_

   @ApiModelProperty("进程状态")
   @BeanProperty
   @JsonProperty("system.process.state")
   var state:String=_
   @ApiModelProperty("启动用户")
   @BeanProperty
   @JsonProperty("user.name")
   var username:String=_
   @BeanProperty
   @JsonProperty("process")
   var processFileEntity:ProcessFileEntity=_

   @BeanProperty
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   var startTime:Date=_
   @BeanProperty
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
   var endTime:Date=_




}
