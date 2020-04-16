package com.piesat.monitor.dao.es.rabbitmq

import java.util.{List => JavaList}

import com.piesat.monitor.entity.process.ProcessConfig
import com.piesat.monitor.entity.rabbitmq.RabbitMqConfig
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait RabbitMqConfigMapper {
  def list(rabbitMqConfig: RabbitMqConfig):JavaList[RabbitMqConfig]

  def selectAlias:JavaList[String]


}
