package com.piesat.monitor.rpc.service.kafka

import com.piesat.monitor.dao.es.kafka.{KafkaConfigMapper, KafkaMonitorMapper}
import com.piesat.monitor.dao.es.system.FileMapper
import com.piesat.monitor.entity.system.FileEntity
import org.springframework.beans.factory.annotation.Autowired
import java.util.{Date, List => JavaList}

import com.piesat.monitor.entity.kafka.{KafkaConfig, KafkaMonitor}
import org.springframework.stereotype.Service

/**
  * Created by zzj on 2020/3/31.
  */
@Service
class KafkaMonitorService @Autowired()(kafkaMonitorMapper: KafkaMonitorMapper,
                                       kafkaConfigMapper:KafkaConfigMapper){
  def list(kafkaMonitor:KafkaMonitor):JavaList[KafkaMonitor]={
    kafkaMonitor.setDataset("kafka.consumergroup")
    var time=kafkaMonitorMapper.maxTime(kafkaMonitor);
    kafkaMonitor.setTimestamp(time)
    if(null!=kafkaMonitor.address){
      var kafakConfig=new KafkaConfig
      kafakConfig.setAddress(kafkaMonitor.address)
      var groupIds=kafkaConfigMapper.selectGroupId(kafakConfig)
      kafkaMonitor.setGroupIds(groupIds)
    }
    kafkaMonitorMapper.list(kafkaMonitor)
  }
}
