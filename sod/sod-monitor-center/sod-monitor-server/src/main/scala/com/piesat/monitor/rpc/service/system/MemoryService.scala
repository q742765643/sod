package com.piesat.monitor.rpc.service.system

import java.util.{List => JavaList}

import com.piesat.monitor.dao.es.system.{CpuMapper, MemoryMapper}
import com.piesat.monitor.entity.system.MemoryEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by zzj on 2020/3/24.
  */
@Service
class MemoryService @Autowired()(memoryMapper: MemoryMapper){

  def list(memoryEntity: MemoryEntity):JavaList[MemoryEntity]={
      memoryEntity.setDataset("system.memory")
      memoryMapper.list(memoryEntity)
  }

}
