package com.piesat.monitor.dao.es.flow

import java.util.{List => JavaList}

import com.piesat.monitor.entity.flow.FlowMonitor
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait FlowMonitorMapper {
  def list(flowMonitor: FlowMonitor):JavaList[FlowMonitor]

  def findById(id:String):FlowMonitor

}
