package com.piesat.monitor.repository.rabbit

import com.piesat.monitor.entity.rabbitmq.RabbitMqConfig
import com.piesat.monitor.entity.ssh.SshEntity
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
  * Created by zzj on 2020/3/23.
  */
trait RabbitMqConfigDao extends ElasticsearchRepository[RabbitMqConfig,String]{


}
