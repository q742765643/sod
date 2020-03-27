package com.piesat.calculate.main

import java.text.SimpleDateFormat
import java.util.Properties

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.piesat.calculate.entity.{KafkaMessege, LogMessege, StationCount}
import com.piesat.calculate.function.{CountFunction, WindowResult}
import com.piesat.calculate.sink.EsSink
import com.piesat.calculate.trigger.CountTrigger
import com.piesat.calculate.util.GrokUtil
import com.piesat.calculate.util.config.SystemConfig
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.connectors.elasticsearch.{ActionRequestFailureHandler, ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.http.HttpHost
import org.elasticsearch.action.ActionRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.Requests
import org.elasticsearch.common.xcontent.XContentType

/**
  * Created by zzj on 2020/3/17.
  */
object StreamingKafka1 {


  def main(args: Array[String]): Unit = {

    var SystemConfig =new SystemConfig
    val env=SystemConfig.loadEnv()
    val topic =SystemConfig.loadTopic("config/1111.properties")
    //    kafka的配置信息
    val kafkaProps=SystemConfig.loadKafkaProperties("config/1111.properties")
    //    外部的检查点  保留撤销

    val outputTag = new OutputTag[LogMessege]("late_data") {}
    val consumer = new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), kafkaProps)
    val streamSinge = env.addSource(consumer).map(
      x => {
        var kafkaMessege: KafkaMessege = JSON.parseObject(x, classOf[KafkaMessege])
        kafkaMessege
      }
    ).map(x => {
      try {
        var result = new GrokUtil().getMesssge(x.message)
        var logMessege:LogMessege = JSON.parseObject(result, classOf[LogMessege])
         logMessege.key = logMessege.ddataId + "||" + logMessege.station
          logMessege.id = logMessege.ddataId + "||" + logMessege.station
          logMessege
      } catch {
        case ex:Exception=>{
          print(ex)
        };
        null
      }

    }).filter(x => {
      if (x != null) {
        true
      } else {
        false
      }
    })

    streamSinge.addSink(new EsSink[LogMessege].sinkAdd("htht"))
    var count = streamSinge
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[LogMessege](Time.minutes(1)) {
        override def extractTimestamp(element: LogMessege): Long = {
          var a = element.ddatatime.getTime
          return a
        }
      })
      .keyBy(_.id).timeWindow(Time.days(1))
      //自定义触发器
      .trigger(new CountTrigger(10, 5 * 1000))
      .sideOutputLateData(outputTag)
      .aggregate(new CountFunction(), new WindowResult())

    val output = count.getSideOutput(outputTag)
    output.map(f => {
      println(s"过时数据：$f")
    })
    count.print()
    count.addSink(new EsSink[StationCount].sinkUpdate("htht3"))
    env.execute("StreamingKafka1")

  }


}
