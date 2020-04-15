package com.piesat.monitor.dao.es.kafka

import java.util.{List => JavaList}

import com.piesat.monitor.entity.kafka.KafkaConfig
import com.piesat.monitor.entity.process.ProcessConfig
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait KafkaConfigMapper {
  def list(kafkaConfig: KafkaConfig):JavaList[KafkaConfig]

  def selectGroupId(kafkaConfig: KafkaConfig):JavaList[String]

  def selectAlias:JavaList[String]

}
