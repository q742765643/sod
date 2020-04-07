package com.piesat.calculate.windows

import java.text.SimpleDateFormat
import java.util
import java.util.{Calendar, Collections, Date}

import org.apache.flink.api.common.ExecutionConfig
import org.apache.flink.api.common.typeutils.TypeSerializer
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner
import org.apache.flink.streaming.api.windowing.triggers.{EventTimeTrigger, Trigger}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

/**
  * Created by zzj on 2020/4/3.
  */
class HtWindowAssigner[T] extends WindowAssigner[T, TimeWindow] {
  override def isEventTime: Boolean = true

  override def getDefaultTrigger(streamExecutionEnvironment: StreamExecutionEnvironment): Trigger[T, TimeWindow] =
    EventTimeTrigger.create().asInstanceOf[Trigger[T, TimeWindow]]

  override def assignWindows(t: T, l: Long, windowAssignerContext: WindowAssigner.WindowAssignerContext): util.Collection[TimeWindow] = {
    import java.util.Calendar
    val calendar = Calendar.getInstance
    calendar.setTime(new Date())
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND,0)
    val zero = calendar.getTime
    var start=zero.getTime
    var end=zero.getTime+86400*1000
    //分配窗口
    Collections.singletonList(new TimeWindow(start, end))

  }

  override def getWindowSerializer(executionConfig: ExecutionConfig): TypeSerializer[TimeWindow] = new TimeWindow.Serializer
}
