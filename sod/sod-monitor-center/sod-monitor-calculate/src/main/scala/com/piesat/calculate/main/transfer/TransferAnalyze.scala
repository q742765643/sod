package com.piesat.calculate.main.transfer

import java.text.SimpleDateFormat
import java.util

import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import com.piesat.calculate.constant.FlowConstant
import com.piesat.calculate.entity.KafkaMessege
import com.piesat.calculate.entity.flow.FlowMonitor
import com.piesat.calculate.entity.transfer.{StationLevelEntity, StationLevelFiledEntity}
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
        var result = new GrokUtil().getMesssge(x.message).trim
        if (result.startsWith("DI_ALL_PROCESS=")) {
          //System.out.print(result)
          result = result.replace("DI_ALL_PROCESS=", "")
          var stationLevel: StationLevelEntity = objectMapper.readValue(result, classOf[StationLevelEntity])
          var stationLevelFiled: StationLevelFiledEntity = stationLevel.getStationLevelFiledEntity
          val timeFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
          val htimeFormat: SimpleDateFormat = new SimpleDateFormat("HH")

          var id: String = null
          var groupKey: String = null
          val filter = scala.collection.mutable.Map[String, Object]()
          if (stationLevelFiled.iiiii == null || "".equals(stationLevelFiled.iiiii)) {
            stationLevelFiled.iiiii = "0"
          }
          filter.put("fields.DATA_TYPE", stationLevelFiled.dataType)
          filter.put("fields.DATA_TIME", stationLevelFiled.dataTime)
          if (FlowConstant.RT_CTS_STATION_DI.equals(stationLevel.stype) || FlowConstant.RT_DPC_STATION_DI.equals(stationLevel.stype)) {
            id = stationLevelFiled.dataType + "_" + timeFormat.format(stationLevelFiled.dataTime) + "_" + stationLevelFiled.iiiii + "_" + stationLevelFiled.dataUpdateFlag
            groupKey = stationLevelFiled.dataType + "||" + stationLevelFiled.iiiii+"||"+htimeFormat.format(stationLevelFiled.dataTime)
            filter.put("fields.IIiii", stationLevelFiled.iiiii)
          }
          if (FlowConstant.RT_CTS_FILE_DI.equals(stationLevel.stype) || FlowConstant.RT_DPC_FILE_DI.equals(stationLevel.stype)) {
            id = stationLevelFiled.dataType + "_" + stationLevelFiled.fileNameN + "_" + stationLevelFiled.dataUpdateFlag
            filter.put("fields.FILE_NAME_N", stationLevelFiled.fileNameN)
            groupKey = stationLevelFiled.dataType + "||" + stationLevelFiled.iiiii+"||"+htimeFormat.format(stationLevelFiled.dataTime)
          }
          val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
          stationLevel.setDdateTime(stationLevelFiled.dataTime)
          stationLevel.setId(id)
          stationLevel.groupKey = groupKey
          val date = dateFormat.format(stationLevel.ddateTime)
          var l = EsUtil.getWhere("transfer_analyze-" + date, filter)
          System.out.print("第一步:"+l)
          if (l > 0) {
            System.out.print("ss===="+l)
            stationLevel.isCount = 1
          } else {
            stationLevel.isCount = 0
          }
          stationLevel
        } else {
          null
        }
      } catch {
        case ex: Exception => {
          ex.getStackTrace
          print(ex)
        };
          null
      }

    }).filter(x => {
      if (x != null && x.id != null) {
        System.out.println(x.groupKey)
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
          a
        }
      })
      .filter(x => {
        if (x.isCount == 0) {
          true
        } else {
          false
        }
      })
      .keyBy(_.groupKey) //.window(TumblingEventTimeWindows.of(Time.days(1)))
      .timeWindow(Time.days(1))
      //.timeWindow(Time.days(1))
      //自定义触发器
      //.trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(5)))
      //.window(new HtWindowAssigner)
      .trigger(new CountTransferTrigger(10, 5 * 1000))
      .sideOutputLateData(outputTag)
      .aggregate(new CountTransferFunction(), new WindowResult())

    val output = count.getSideOutput(outputTag)
    output.map(f => {
      println(s"过时数据：$f")
      try {
        val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
        val date = dateFormat.format(f.ddateTime)
        val timeFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
        var dataS = timeFormat.format(f.ddateTime)
        var dataDay = dataS.substring(0, 10)
        var oldMap = EsUtil.get("transfer_statistics-" + date, dataDay + "||" + f.groupKey)
        var objectMapper = new ObjectMapper
        var newMap: util.Map[String, Object] = new util.HashMap[String, Object]
        if (FlowConstant.RT_CTS_STATION_DI.equals(f.stype) || FlowConstant.RT_DPC_STATION_DI.equals(f.stype)) {
          var cc: String = String.valueOf(oldMap.get("collection_realIncome"))
          var collectionRealIncome: Long = cc.toLong + 1
          newMap.put("collection_realIncome", collectionRealIncome.asInstanceOf[Object])
        }
        if (FlowConstant.RT_CTS_FILE_DI.equals(f.stype) || FlowConstant.RT_DPC_FILE_DI.equals(f.stype)) {
          var pp: String = String.valueOf(oldMap.get("put_realIncome"))
          var putRealIncome: Long = pp.toLong + 1
          newMap.put("put_realIncome", putRealIncome.asInstanceOf[Object])
        }
        EsUtil.update("transfer_statistics-" + date, dataDay + "||" + f.groupKey, newMap)
      } catch {
        case ex: Exception => {
          print(ex)
        }
      }
    })
    count.print()
    count.addSink(new EsSink[FlowMonitor].sinkUpdate("transfer_statistics"))
    env.execute("TransferAnalyze")

  }


}
