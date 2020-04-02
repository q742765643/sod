package com.piesat.monitor.repository.process

import com.piesat.monitor.entity.ssh.SshEntity
import com.piesat.monitor.entity.system.ProcessEntity
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
  * Created by zzj on 2020/4/1.
  */
trait ProcessDao extends ElasticsearchRepository[ProcessEntity,String]{

}
