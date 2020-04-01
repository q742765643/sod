package com.piesat.calculate.main.transfer

import java.text.SimpleDateFormat

import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import com.piesat.calculate.constant.FlowConstant
import com.piesat.calculate.entity.flow.FlowMonitor
import com.piesat.calculate.entity.transfer.{StationLevelEntity, StationLevelFiledEntity}
import com.piesat.calculate.entity.{KafkaMessege, StationCount}
import com.piesat.calculate.function.WindowResult
import com.piesat.calculate.function.transfer.CountTransferFunction
import com.piesat.calculate.sink.EsSink
import com.piesat.calculate.trigger.transfer.CountTransferTrigger
import com.piesat.calculate.util.config.SystemConfig
import com.piesat.calculate.util.{EsUtil, GrokUtil}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

/**
  * Created by zzj on 2020/3/17.
  */
object TransferAnalyze {


  def main(args: Array[String]): Unit = {

    var SystemConfig = new SystemConfig
    val env = SystemConfig.loadEnv()
    val topic = SystemConfig.loadTopic("config/TransferAnalyze.properties")
    //    kafka的配置信息
    val kafkaProps = SystemConfig.loadKafkaProperties("config/TransferAnalyze.properties")
    //    外部的检查点  保留撤销

    val outputTag = new OutputTag[StationLevelEntity]("late_data") {}
    val consumer = new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), kafkaProps)
    val streamSinge = env.addSource(consumer).map(
      x => {
        var kafkaMessege: KafkaMessege = JSON.parseObject(x, classOf[KafkaMessege])
        kafkaMessege
      }
    ).map(x => {
      try {
        var objectMapper = new ObjectMapper()
        var result = new GrokUtil().getMesssge(x.message)
        var stationLevel: StationLevelEntity = objectMapper.readValue(result, classOf[StationLevelEntity])
        var stationLevelFiled: StationLevelFiledEntity = stationLevel.getStationLevelFiledEntity
        val timeFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
        var id: String = null
        var groupKey: String = null
        val filter=scala.collection.mutable.Map[String,Object]()
        filter.put("fields.DATA_TYPE", stationLevelFiled.dataType)
        filter.put("fields.DATA_TIME", timeFormat.format(stationLevelFiled.dataTime))
        if (FlowConstant.RT_CTS_STATION_DI.equals(stationLevel.stype)) {
          id = stationLevelFiled.dataType + "_" + timeFormat.format(stationLevelFiled.dataTime) + "_" + stationLevelFiled.iiiii + "_" + stationLevelFiled.dataUpdateFlag
          groupKey = stationLevelFiled.dataType + "_" + stationLevelFiled.iiiii
          filter.put("fields.IIiii", stationLevelFiled.iiiii)
        }
        if (FlowConstant.RT_CTS_FILE_DI.equals(stationLevel.stype)) {
          id = stationLevelFiled.dataType + "_" + stationLevelFiled.fileNameN + "_" + stationLevelFiled.dataUpdateFlag
          filter.put("fields.FILE_NAME_N", stationLevelFiled.fileNameN)
          groupKey = stationLevelFiled.dataType + "_" + timeFormat.format(stationLevelFiled.dataTime).substring(0, 10)
        }
        val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
        stationLevel.setDdateTime(stationLevelFiled.dataTime)
        stationLevel.setId(id)
        stationLevel.groupKey = groupKey
        val date = dateFormat.format(stationLevel.ddateTime)
        var l = EsUtil.getWhere("transfer_analyze-" + date, filter)
        if (l > 0) {
          stationLevel.isCount = 1
        } else {
          stationLevel.isCount = 0
        }
        stationLevel
      } catch {
        case ex: Exception => {
          print(ex)
        };
          null
      }

    }).filter(x => {
      if (x != null && x.isCount == 0 && x.id != null) {
        true
      } else {
        false
      }
    })
    streamSinge.addSink(new EsSink[StationLevelEntity].sinkAdd("transfer_analyze"))
    var count = streamSinge
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[StationLevelEntity](Time.minutes(1)) {
        override def extractTimestamp(element: StationLevelEntity): Long = {
          var a = element.ddateTime.getTime
          return a
        }
      })
      .keyBy(_.groupKey).timeWindow(Time.days(1))
      //自定义触发器
      .trigger(new CountTransferTrigger(10, 5 * 1000))
      .sideOutputLateData(outputTag)
      .aggregate(new CountTransferFunction(), new WindowResult())

    val output = count.getSideOutput(outputTag)
    output.map(f => {
      println(s"过时数据：$f")
    })
    count.print()
    count.addSink(new EsSink[FlowMonitor].sinkUpdate("transfer_statistics"))
    env.execute("TransferAnalyze")

  }


}
