package com.piesat.calculate.function.transfer

import com.piesat.calculate.constant.FlowConstant
import com.piesat.calculate.entity.statistics.FlowStatistics
import com.piesat.calculate.entity.transfer.StationLevelEntity
import org.apache.flink.api.common.functions.AggregateFunction

/**
  * Created by zzj on 2020/3/17.
  */
class CountTransferFunction extends AggregateFunction[StationLevelEntity, FlowStatistics, FlowStatistics] {
  override def createAccumulator(): FlowStatistics = new FlowStatistics

  override def merge(acc: FlowStatistics, acc1: FlowStatistics): FlowStatistics = {
    acc.collectionRealIncome = acc.collectionRealIncome + acc1.collectionRealIncome
    acc.distributeRealIncome = acc.distributeRealIncome + acc1.distributeRealIncome
    acc.putRealIncome = acc.putRealIncome + acc1.putRealIncome
    acc.timely = acc.timely + acc1.timely
    acc
  }

  override def getResult(acc: FlowStatistics): FlowStatistics = acc

  override def add(in: StationLevelEntity, acc: FlowStatistics): FlowStatistics = {
    if (FlowConstant.RT_CTS_STATION_DI.equals(in.stype)) {
      acc.collectionRealIncome = acc.collectionRealIncome + 1
      if (in.stationLevelFiledEntity.processStartTime.getTime - System.currentTimeMillis() < 5 * 1000 * 60) {
        acc.timely = acc.timely + 1
      }
    }
    if (FlowConstant.RT_CTS_FILE_DI.equals(in.stype)) {
      acc.collectionRealIncome = acc.collectionRealIncome + 1
      if (in.stationLevelFiledEntity.processStartTime.getTime - System.currentTimeMillis() <5 * 1000 * 60) {
        acc.timely = acc.timely + 1
      }
    }
    if (FlowConstant.RT_DPC_STATION_DI.equals(in.stype)) {
      acc.putRealIncome = acc.putRealIncome + 1
    }
    if (FlowConstant.RT_DPC_FILE_DI.equals(in.stype)) {
      acc.putRealIncome = acc.putRealIncome + 1
    }
    acc.iiiii = in.stationLevelFiledEntity.iiiii
    acc.ddateTime=in.ddateTime
    acc.dataType=in.stationLevelFiledEntity.dataType
    acc.id=in.groupKey
    acc.sType=in.stype
    acc.name=acc.name
    acc
  }

}
