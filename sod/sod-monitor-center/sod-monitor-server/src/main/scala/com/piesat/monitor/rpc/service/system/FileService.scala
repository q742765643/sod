package com.piesat.monitor.rpc.service.system

import java.util.{List => JavaList}

import com.piesat.monitor.dao.es.system.{CpuMapper, FileMapper}
import com.piesat.monitor.entity.system.{CpuEntity, FileEntity}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by zzj on 2020/3/24.
  */
@Service
class FileService @Autowired()(fileMapper: FileMapper){

  def list(fileEntity: FileEntity):JavaList[FileEntity]={
      fileEntity.setDataset("system.filesystem")
      var time=fileMapper.maxTime(fileEntity);
      fileEntity.setTimestamp(time)
      fileMapper.list(fileEntity)
  }

}
