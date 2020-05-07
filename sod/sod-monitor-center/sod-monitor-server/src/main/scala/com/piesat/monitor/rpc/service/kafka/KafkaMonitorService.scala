package com.piesat.monitor.rpc.service.kafka

import java.util

import com.piesat.monitor.dao.es.kafka.{KafkaConfigMapper, KafkaMonitorMapper}
import com.piesat.monitor.dao.es.system.FileMapper
import com.piesat.monitor.entity.system.FileEntity
import org.springframework.beans.factory.annotation.Autowired
import java.util.{Date, List => JavaList}

import com.piesat.monitor.entity.kafka.{KafkaConfig, KafkaMonitor}
import com.piesat.monitor.entity.rabbitmq.RabbitMqMonitor
import org.springframework.stereotype.Service
import org.util.DateUtil

/**
  * Created by zzj on 2020/3/31.
  */
@Service
class KafkaMonitorService @Autowired()(kafkaMonitorMapper: KafkaMonitorMapper,
                                       kafkaConfigMapper:KafkaConfigMapper){
  def list(kafkaMonitor:KafkaMonitor):JavaList[KafkaMonitor]={
    kafkaMonitor.setDataset("kafka.consumergroup")
    var list:JavaList[KafkaMonitor]=new util.ArrayList[KafkaMonitor]


    if(null!=kafkaMonitor.alias && !"".equals(kafkaMonitor.alias)){
      var kafakConfig=new KafkaConfig
      kafakConfig.setAlias(kafkaMonitor.alias)
      var kafkaConfigs1:JavaList[KafkaConfig]=kafkaConfigMapper.list(kafakConfig);
      if(null!=kafkaConfigs1 && kafkaConfigs1.size()>0){
        kafakConfig.setAddress(kafkaConfigs1.get(0).address)
        var groupIds=kafkaConfigMapper.selectGroupId(kafakConfig)
        kafkaMonitor.setGroupIds(groupIds)
        var ads:Array[String]=kafkaConfigs1.get(0).address.split(",")
        kafkaMonitor.setAddresss(util.Arrays.asList(ads:_*))
        var map:util.Map[String,Date]=DateUtil.getStartAndEnd()
        kafkaMonitor.startTime=map.get("startTime")
        kafkaMonitor.endTime=map.get("endTime")
        var time=kafkaMonitorMapper.maxTime(kafkaMonitor);
        if(null==time){
          return list
        }
        kafkaMonitor.setTimestamp(time)
        list=kafkaMonitorMapper.list(kafkaMonitor)
      }


    }
    list
  }
}
