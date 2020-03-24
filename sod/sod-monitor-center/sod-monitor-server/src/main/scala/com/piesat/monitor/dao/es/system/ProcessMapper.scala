package com.piesat.monitor.dao.es.system

import java.util.{Date, List => JavaList}

import com.piesat.monitor.entity.system.{FileEntity, MemoryEntity, ProcessEntity}
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/3/24.
  */
@Mapper
trait ProcessMapper {
  def list(processEntity: ProcessEntity):JavaList[ProcessEntity]

  def maxTime(processEntity: ProcessEntity):Date
}
