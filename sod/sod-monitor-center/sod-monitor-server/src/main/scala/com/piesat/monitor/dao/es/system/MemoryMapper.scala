package com.piesat.monitor.dao.es.system

import com.piesat.monitor.entity.system.MemoryEntity
import org.apache.ibatis.annotations.Mapper
import java.util.{List => JavaList}

/**
  * Created by zzj on 2020/3/24.
  */
@Mapper
trait MemoryMapper {
  def list(memoryEntity: MemoryEntity):JavaList[MemoryEntity]
}
