package com.piesat.monitor.repository.flow

import com.piesat.monitor.entity.flow.PlayBill
import com.piesat.monitor.entity.kafka.KafkaConfig
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
  * Created by zzj on 2020/4/1.
  */
trait PlayBillDao extends ElasticsearchRepository[PlayBill,String]{

}
