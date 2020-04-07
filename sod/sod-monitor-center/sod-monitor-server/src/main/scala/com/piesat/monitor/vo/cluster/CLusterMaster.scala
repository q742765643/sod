package com.piesat.monitor.vo.cluster
import java.util.{List => JavaList}

import io.swagger.annotations.ApiModelProperty

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/2.
  */
class CLusterMaster {
  @ApiModelProperty("节点详情")
  @BeanProperty
  var cluserNode:JavaList[CluserNode]=_

  @ApiModelProperty("磁盘使用率百分比")
  @BeanProperty
  var diskPct:Float=_

  @ApiModelProperty("正常个数")
  @BeanProperty
  var normalNum:Int=_

  @ApiModelProperty("异常个数")
  @BeanProperty
  var abnormalNum:Int=_
}
