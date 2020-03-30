package com.piesat.monitor.dao.es.system

import java.util.{List => JavaList}

import com.piesat.monitor.entity.system.{CpuEntity, NetWorkEntity}
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/3/24.
  */
@Mapper
trait NetWorkMapper {

  def list(netWorkEntity: NetWorkEntity):JavaList[NetWorkEntity]


}
