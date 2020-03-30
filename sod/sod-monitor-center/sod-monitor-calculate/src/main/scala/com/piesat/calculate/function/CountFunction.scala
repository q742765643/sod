package com.piesat.calculate.function

import com.piesat.calculate.entity.{LogMessege, StationCount, SumVo}
import org.apache.flink.api.common.functions.{AggregateFunction, FlatMapFunction}
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala._

/**
  * Created by zzj on 2020/3/17.
  */
class CountFunction extends AggregateFunction[LogMessege,SumVo,SumVo]{
    override def createAccumulator(): SumVo = new SumVo

    override def merge(acc: SumVo, acc1: SumVo): SumVo = {
        acc.quote=acc.quote + acc1.quote
        acc
    }

    override def getResult(acc: SumVo): SumVo = acc

    override def add(in: LogMessege, acc: SumVo): SumVo ={
        acc.quote=acc.quote + 1
        acc
    }

}
