package com.piesat.monitor.dao.es.system

import com.piesat.monitor.entity.system.{ClusterMonitor, CpuEntity}
import org.apache.ibatis.annotations.Mapper
import java.util.{List => JavaList}

/**
  * Created by zzj on 2020/4/2.
  */
@Mapper
trait ClusterMonitorMapper {
  def list(clusterMonitor: ClusterMonitor):JavaList[ClusterMonitor]

}
