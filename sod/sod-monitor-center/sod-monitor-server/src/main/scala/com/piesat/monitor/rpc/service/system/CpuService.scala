package com.piesat.monitor.rpc.service.system

import com.piesat.monitor.dao.es.system.CpuMapper
import com.piesat.monitor.entity.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.{Date, List => JavaList}

import com.piesat.monitor.entity.system.CpuEntity

/**
  * Created by zzj on 2020/3/24.
  */
@Service
class CpuService @Autowired()(cpuMapper: CpuMapper){

  def list(cpuEntity: CpuEntity):JavaList[CpuEntity]={
      cpuEntity.setDataset("system.cpu")
      cpuMapper.list(cpuEntity)
  }

}
