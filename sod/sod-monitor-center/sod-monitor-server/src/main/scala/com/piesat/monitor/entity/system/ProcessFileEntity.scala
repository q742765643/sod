package com.piesat.monitor.entity.system

import java.util.Date

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/3/24.
  */
@Data
class ProcessFileEntity {

   @ApiModelProperty("别名")
   @BeanProperty
   @JsonProperty("args")
   var cwd:Array[String]=_

   @ApiModelProperty("别名")
   @BeanProperty
   @JsonProperty("name")
   var name:String=_

   @ApiModelProperty("pid")
   @BeanProperty
   @JsonProperty("pgid")
   var pgid:String=_

   @ApiModelProperty("pid")
   @BeanProperty
   @JsonProperty("pid")
   var pid:String=_

   @ApiModelProperty("ppid")
   @JsonProperty("ppid")
   @BeanProperty
   var ppid:String=_







}
