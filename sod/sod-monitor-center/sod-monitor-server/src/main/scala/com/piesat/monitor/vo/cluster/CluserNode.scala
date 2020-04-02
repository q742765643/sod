package com.piesat.monitor.vo.cluster

import io.swagger.annotations.ApiModelProperty

import scala.beans.BeanProperty

/**
  * Created by zzj on 2020/4/2.
  */
class CluserNode {
  @ApiModelProperty("ip")
  @BeanProperty
  var ip:String=_
  @ApiModelProperty("cpu使用总的百分比")
  @BeanProperty
  var cpuPct:Float=_

  @ApiModelProperty("实际使用的内存百分比")
  @BeanProperty
  var memoryPct:Float=_

  @ApiModelProperty("磁盘使用率百分比")
  @BeanProperty
  var diskPct:Float=_

  @ApiModelProperty("进程是否正常 1正常")
  @BeanProperty
  var processIsNormal:Int=_

  @ApiModelProperty("磁盘是否正常 1正常")
  @BeanProperty
  var diskIsNormal:Int=_

  @ApiModelProperty("网络是否正常 1正常")
  @BeanProperty
  var netWorkIsNormal:Int=_

  @ApiModelProperty("总体是否正常 1正常")
  @BeanProperty
  var IsNormal:Int=_

}
