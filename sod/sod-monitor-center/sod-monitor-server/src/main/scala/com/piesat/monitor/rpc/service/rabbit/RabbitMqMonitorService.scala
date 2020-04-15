package com.piesat.monitor.rpc.service.rabbit

import java.util
import java.util.{Date, List => JavaList}

import com.piesat.monitor.dao.es.rabbitmq.{RabbitMqConfigMapper, RabbitMqMonitorMapper}
import com.piesat.monitor.entity.rabbitmq.{RabbitMqConfig, RabbitMqMonitor}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.DateUtil

/**
  * Created by zzj on 2020/3/31.
  */
@Service
class RabbitMqMonitorService @Autowired()(rabbitMqMonitorMapper: RabbitMqMonitorMapper, rabbitMqConfigMapper:RabbitMqConfigMapper){
  def list(rabbitMqMonitor:RabbitMqMonitor):JavaList[RabbitMqMonitor]={
    rabbitMqMonitor.setDataset("rabbitmq.queue")
    var list:JavaList[RabbitMqMonitor]=new util.ArrayList[RabbitMqMonitor]
    var rabbitMqConfig: RabbitMqConfig=new RabbitMqConfig
    if(null!=rabbitMqMonitor.alias && !"".equals(rabbitMqConfig.alias)){
      rabbitMqConfig.setAlias(rabbitMqMonitor.alias)
      var rabbitMqConfigList=rabbitMqConfigMapper.list(rabbitMqConfig)
      if(null!=rabbitMqConfigList && rabbitMqConfigList.size()>0){
        rabbitMqMonitor.address=rabbitMqConfigList.get(0).address
        var map:util.Map[String,Date]=DateUtil.getStartAndEnd()
        rabbitMqMonitor.startTime=map.get("startTime")
        rabbitMqMonitor.endTime=map.get("endTime")
        var time=rabbitMqMonitorMapper.maxTime(rabbitMqMonitor);
        if(null==time){
          return list
        }
        rabbitMqMonitor.setTimestamp(time)
        list=rabbitMqMonitorMapper.list(rabbitMqMonitor)
      }

    }
    list

  }
}
