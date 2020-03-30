package com.piesat.calculate.function

import com.piesat.calculate.entity.{LogMessege, StationCount, SumVo}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
  * Created by zzj on 2020/3/17.
  */
class WindowResult extends  WindowFunction[SumVo, StationCount, String, TimeWindow] {
  override def apply(id: String, window: TimeWindow, input: Iterable[SumVo], out: Collector[StationCount]): Unit = {
    val sumVo = input.iterator.next()
    var stationCount:StationCount=new StationCount();
    stationCount.windowStart=window.getStart
    stationCount.windowEnd=window.getEnd
    stationCount.count=sumVo.quote
    stationCount.id=id
    out.collect(stationCount)
  }

}
