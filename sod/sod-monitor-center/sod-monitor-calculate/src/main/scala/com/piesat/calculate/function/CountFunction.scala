package com.piesat.calculate.function

import com.piesat.calculate.entity.SumVo
import com.piesat.calculate.entity.transfer.StationLevelEntity
import org.apache.flink.api.common.functions.AggregateFunction

/**
  * Created by zzj on 2020/3/17.
  */
class CountFunction extends AggregateFunction[StationLevelEntity, SumVo, SumVo] {
  override def createAccumulator(): SumVo = new SumVo

  override def merge(acc: SumVo, acc1: SumVo): SumVo = {
    acc.quote = acc.quote + acc1.quote
    acc
  }

  override def getResult(acc: SumVo): SumVo = acc

  override def add(in: StationLevelEntity, acc: SumVo): SumVo = {
    if (in.isCount == 1) {
      acc.quote = acc.quote + 1
    }
    acc
  }

}
