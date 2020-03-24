package com.piesat.monitor.rpc.service.system

import java.util.{List => JavaList}

import com.piesat.monitor.dao.es.system.{FileMapper, ProcessMapper}
import com.piesat.monitor.entity.system.{FileEntity, ProcessEntity}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by zzj on 2020/3/24.
  */
@Service
class ProcessService @Autowired()(processMapper: ProcessMapper){

  def list(processEntity: ProcessEntity):JavaList[ProcessEntity]={
      processEntity.setDataset("system.process")
      var time=processMapper.maxTime(processEntity);
      processEntity.setTimestamp(time)
      processMapper.list(processEntity)
  }

}
