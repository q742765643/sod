package com.piesat.monitor.dao.es.ssh

import com.piesat.monitor.entity.system.CpuEntity
import org.apache.ibatis.annotations.Mapper
import java.util.{List => JavaList}

import com.piesat.monitor.entity.ssh.SshEntity

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait SshMapper {
  def list(sshEntity: SshEntity):JavaList[SshEntity]

  def selectAlias:JavaList[String]


}
