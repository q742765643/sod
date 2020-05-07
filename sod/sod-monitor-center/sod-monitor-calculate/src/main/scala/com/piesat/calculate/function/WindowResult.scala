package com.piesat.calculate.function

import java.text.SimpleDateFormat

import com.piesat.calculate.entity.flow.FlowMonitor
import com.piesat.calculate.entity.statistics.FlowStatistics
import com.piesat.calculate.entity.{StationCount, SumVo}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
  * Created by zzj on 2020/3/17.
  */
class WindowResult extends WindowFunction[FlowStatistics, FlowMonitor, String, TimeWindow] {
  override def apply(id: String, window: TimeWindow, input: Iterable[FlowStatistics], out: Collector[FlowMonitor]): Unit = {
    val flowStatistics = input.iterator.next()
    var flowMonitor: FlowMonitor = new FlowMonitor();
    val timeFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
    var dataS=timeFormat.format(flowStatistics.ddateTime)
    var dataDay=dataS.substring(0,13)
    flowMonitor.id=dataDay+"||"+flowStatistics.id
    flowMonitor.ddateTime=timeFormat.parse(dataDay+":00:00:000");
    flowMonitor.collectionRealIncome=flowStatistics.collectionRealIncome
    flowMonitor.putRealIncome=flowStatistics.putRealIncome
    flowMonitor.dataType=flowStatistics.dataType
    flowMonitor.timely=flowStatistics.timely
    flowMonitor.name=flowStatistics.name
    out.collect(flowMonitor)
  }

}
