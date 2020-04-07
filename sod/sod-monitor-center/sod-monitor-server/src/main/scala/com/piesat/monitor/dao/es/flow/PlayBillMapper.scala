package com.piesat.monitor.dao.es.flow

import java.util.{List => JavaList}

import com.piesat.monitor.entity.flow.PlayBill
import com.piesat.monitor.entity.kafka.KafkaConfig
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait PlayBillMapper {
  def list(playBill: PlayBill):JavaList[PlayBill]
  
}
