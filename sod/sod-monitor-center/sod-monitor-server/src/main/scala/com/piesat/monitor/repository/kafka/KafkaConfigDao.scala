package com.piesat.monitor.repository.kafka

import com.piesat.monitor.entity.kafka.KafkaConfig
import com.piesat.monitor.entity.process.ProcessConfig
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
  * Created by zzj on 2020/4/1.
  */
trait KafkaConfigDao extends ElasticsearchRepository[KafkaConfig,String]{

}
