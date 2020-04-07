package com.piesat.monitor.dao.es.kafka

import java.util.{Date, List => JavaList}

import com.piesat.monitor.entity.kafka.KafkaMonitor
import com.piesat.monitor.entity.system.FileEntity
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/3/24.
  */
@Mapper
trait KafkaMonitorMapper {
  def list(kafkaMonitor: KafkaMonitor):JavaList[KafkaMonitor]

  def maxTime(kafkaMonitor: KafkaMonitor):Date

}
