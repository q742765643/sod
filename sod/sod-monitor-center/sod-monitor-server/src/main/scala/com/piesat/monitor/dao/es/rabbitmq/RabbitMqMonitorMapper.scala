package com.piesat.monitor.dao.es.rabbitmq

import java.util.{Date, List => JavaList}

import com.piesat.monitor.entity.kafka.KafkaMonitor
import com.piesat.monitor.entity.rabbitmq.{RabbitMqConfig, RabbitMqMonitor}
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait RabbitMqMonitorMapper {
  def list(rabbitMqMonitor: RabbitMqMonitor):JavaList[RabbitMqMonitor]

  def maxTime(rabbitMqMonitor: RabbitMqMonitor):Date


}
