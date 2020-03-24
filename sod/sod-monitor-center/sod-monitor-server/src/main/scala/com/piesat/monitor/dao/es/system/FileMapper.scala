package com.piesat.monitor.dao.es.system

import com.piesat.monitor.entity.system.{FileEntity, MemoryEntity}
import org.apache.ibatis.annotations.Mapper
import java.util.{Date, List => JavaList}

/**
  * Created by zzj on 2020/3/24.
  */
@Mapper
trait FileMapper {
  def list(fileEntity: FileEntity):JavaList[FileEntity]

  def maxTime(fileEntity: FileEntity):Date

}
