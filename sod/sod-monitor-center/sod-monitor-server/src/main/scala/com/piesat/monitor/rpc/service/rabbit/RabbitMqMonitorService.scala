package com.piesat.monitor.rpc.service.rabbit

import java.util.{List => JavaList}

import com.piesat.monitor.dao.es.rabbitmq.RabbitMqMonitorMapper
import com.piesat.monitor.entity.rabbitmq.RabbitMqMonitor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Created by zzj on 2020/3/31.
  */
@Service
class RabbitMqMonitorService @Autowired()(rabbitMqMonitorMapper: RabbitMqMonitorMapper){
  def list(rabbitMqMonitor:RabbitMqMonitor):JavaList[RabbitMqMonitor]={
    rabbitMqMonitor.setDataset("rabbitmq.queue")
    var time=rabbitMqMonitorMapper.maxTime(rabbitMqMonitor);
    rabbitMqMonitor.setTimestamp(time)
    rabbitMqMonitorMapper.list(rabbitMqMonitor)
  }
}
