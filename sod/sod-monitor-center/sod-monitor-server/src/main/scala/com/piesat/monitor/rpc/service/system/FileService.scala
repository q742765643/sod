package com.piesat.monitor.rpc.service.system

import java.util
import java.util.{Date, List => JavaList}

import com.piesat.monitor.dao.es.system.{CpuMapper, FileMapper}
import com.piesat.monitor.entity.system.{CpuEntity, FileEntity}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.DateUtil

/**
  * Created by zzj on 2020/3/24.
  */
@Service
class FileService @Autowired()(fileMapper: FileMapper){

  def list(fileEntity: FileEntity):JavaList[FileEntity]={
      fileEntity.setDataset("system.filesystem")
      var map:util.Map[String,Date]=DateUtil.getStartAndEnd()
      fileEntity.startTime=map.get("startTime")
      fileEntity.endTime=map.get("endTime")
      var time=fileMapper.maxTime(fileEntity);
      var list:JavaList[FileEntity]=new util.ArrayList[FileEntity]()
      if(time!=null){
        fileEntity.setTimestamp(time)
        list=fileMapper.list(fileEntity)
      }
      list

  }

}
