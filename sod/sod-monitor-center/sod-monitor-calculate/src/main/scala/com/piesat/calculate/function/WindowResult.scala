package com.piesat.calculate.function

import com.piesat.calculate.entity.{LogMessege, StationCount}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
  * Created by zzj on 2020/3/17.
  */
class WindowResult extends  WindowFunction[Long, StationCount, String, TimeWindow] {
  override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[StationCount]): Unit = {
    val count = input.iterator.next()
    var stationCount:StationCount=new StationCount();
    stationCount.windowEnd=window.getEnd
    stationCount.count=count
    stationCount.key=key
    out.collect(stationCount)
  }

}
