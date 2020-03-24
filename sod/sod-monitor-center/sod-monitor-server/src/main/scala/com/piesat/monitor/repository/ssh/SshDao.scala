package com.piesat.monitor.repository.ssh

import com.piesat.monitor.entity.ssh.SshEntity
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
  * Created by zzj on 2020/3/23.
  */
trait SshDao extends ElasticsearchRepository[SshEntity,String]{


}
