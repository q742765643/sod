package com.piesat.monitor.rpc.service.system

import java.util

import com.piesat.monitor.dao.es.system.CpuMapper
import com.piesat.monitor.entity.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.{Date, List => JavaList}

import com.piesat.monitor.entity.system.CpuEntity
import org.util.DateUtil

/**
  * Created by zzj on 2020/3/24.
  */
@Service
class CpuService @Autowired()(cpuMapper: CpuMapper){

  def list(cpuEntity: CpuEntity):JavaList[CpuEntity]={
      var map:util.Map[String,Date]=DateUtil.getStartAndEnd10()
      cpuEntity.startTime=map.get("startTime")
      cpuEntity.endTime=map.get("endTime")
      cpuEntity.setDataset("system.cpu")
      cpuMapper.list(cpuEntity)
  }

}
