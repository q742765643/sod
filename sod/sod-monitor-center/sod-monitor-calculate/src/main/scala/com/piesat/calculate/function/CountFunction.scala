package com.piesat.calculate.function

import com.piesat.calculate.entity.{LogMessege, StationCount}
import org.apache.flink.api.common.functions.{AggregateFunction, FlatMapFunction}
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala._

/**
  * Created by zzj on 2020/3/17.
  */
class CountFunction extends AggregateFunction[LogMessege,Long,Long]{
    override def createAccumulator(): Long = 0L

    override def merge(acc: Long, acc1: Long): Long = acc + acc1

    override def getResult(acc: Long): Long = acc

    override def add(in: LogMessege, acc: Long): Long = acc + 1

}
