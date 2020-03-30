package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import lombok.Data

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/24.
  */
@Data
class ProcessEntity {
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
   @ApiModelProperty("启动进程的完整命令行")
   @BeanProperty
   var cmdline:String=_
   @ApiModelProperty("别名")
   @BeanProperty
   var cwd:String=_
   @ApiModelProperty("别名")
   @BeanProperty
   var name:String=_
   @ApiModelProperty("pgid")
   @BeanProperty
   var pgid:String=_
   @ApiModelProperty("pid")
   @BeanProperty
   var pid:String=_
   @ApiModelProperty("ppid")
   @BeanProperty
   var ppid:String=_
   @ApiModelProperty("进程状态")
   @BeanProperty
   var state:String=_
   @ApiModelProperty("启动用户")
   @BeanProperty
   var username:String=_
   @BeanProperty
   var startTime:String=_
   @BeanProperty
   var endTime:String=_




}
