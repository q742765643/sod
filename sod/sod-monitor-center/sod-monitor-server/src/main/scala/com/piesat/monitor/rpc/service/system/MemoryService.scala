package com.piesat.monitor.rpc.service.system

import java.util
import java.util.{Date, List => JavaList}

import com.piesat.monitor.dao.es.system.{CpuMapper, MemoryMapper}
import com.piesat.monitor.entity.system.MemoryEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.DateUtil

/**
  * Created by zzj on 2020/3/24.
  */
@Service
class MemoryService @Autowired()(memoryMapper: MemoryMapper){

  def list(memoryEntity: MemoryEntity):JavaList[MemoryEntity]={
      memoryEntity.setDataset("system.memory")
      var map:util.Map[String,Date]=DateUtil.getStartAndEnd10()
      memoryEntity.startTime=map.get("startTime")
      memoryEntity.endTime=map.get("endTime")
      memoryMapper.list(memoryEntity)
  }

}
