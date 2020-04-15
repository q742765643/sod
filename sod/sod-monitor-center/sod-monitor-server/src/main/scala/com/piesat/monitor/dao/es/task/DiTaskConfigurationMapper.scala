package com.piesat.monitor.dao.es.task

import java.util.{List => JavaList}

import com.piesat.monitor.entity.flow.FlowMonitor
import com.piesat.monitor.entity.task.DiTaskConfiguration
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait DiTaskConfigurationMapper {
  def list(diTaskConfiguration: DiTaskConfiguration):JavaList[DiTaskConfiguration]

  def findById(id:String):DiTaskConfiguration

  def selectAlias:JavaList[String]


}
