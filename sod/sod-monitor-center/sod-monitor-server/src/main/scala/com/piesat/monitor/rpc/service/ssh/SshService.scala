package com.piesat.monitor.rpc.service.ssh

import com.piesat.common.utils.IdUtils
import com.piesat.monitor.entity.ssh.SshEntity
import com.piesat.monitor.repository.ssh.SshDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by zzj on 2020/3/23.
  */
@Service
class SshService  @Autowired()(sshDao: SshDao)
{
  def save(ssh:SshEntity):SshEntity={
    ssh.setId(
     IdUtils.simpleUUID
    )
    ssh.setId("1111")
     sshDao.save(ssh)

  }

}
