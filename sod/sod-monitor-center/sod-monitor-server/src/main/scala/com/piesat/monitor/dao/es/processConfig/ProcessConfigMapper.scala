package com.piesat.monitor.dao.es.processConfig

import java.util.{List => JavaList}

import com.piesat.monitor.entity.process.ProcessConfig
import com.piesat.monitor.entity.ssh.SshEntity
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait ProcessConfigMapper {
  def list(processConfig: ProcessConfig):JavaList[ProcessConfig]

}
