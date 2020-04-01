/*
package com.piesat.calculate.main

import java.text.SimpleDateFormat
import java.util.Properties

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.piesat.calculate.entity.{KafkaMessege, LogMessege, StationCount}
import com.piesat.calculate.function.{CountFunction, WindowResult}
import com.piesat.calculate.trigger.CountTrigger
import com.piesat.calculate.util.GrokUtil
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
import org.apache.flink.util.ExceptionUtils
import org.apache.http.HttpHost
import org.elasticsearch.ElasticsearchParseException
import org.elasticsearch.action.ActionRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.Requests
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException
import org.elasticsearch.common.xcontent.{XContentFactory, XContentType}

/**
  * Created by zzj on 2020/3/17.
  */
object StreamingKafka {
  var ZOOKEEPER_HOST = "10.211.55.7:2181"
  var KAFKA_BROKER = "10.211.55.7:9092"
  var KAFKA_GROUP = "hthtsod"

  def main(args: Array[String]): Unit = {
    //    获取Flink运行环境也就是入口点
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //    设置检查点时间为5秒
    env.enableCheckpointing(50000)
    //    设置检查模式  恰好一次
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //    设置检查点之间的最小暂停时间
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    //    设置检查点超时 60秒
    env.getCheckpointConfig.setCheckpointTimeout(60000)
    //    设置最大并发检查点
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    //    外部的检查点  保留撤销
    env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    //    没有设置检查点请自行添加
    //env.getStateBackend(new FsStateBackend("hdfs://master:9000/flink/checkpoints"))
    //    要消费的主题
    val topic = "test"
    //    kafka的配置信息
    val kafkaProps = new Properties()
    kafkaProps.setProperty("zookeeper.connect", ZOOKEEPER_HOST)
    kafkaProps.setProperty("bootstrap.servers", KAFKA_BROKER)
    kafkaProps.setProperty("group.id", KAFKA_GROUP)
    val outputTag = new OutputTag[LogMessege]("late_data") {}
    val consumer = new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), kafkaProps)
    val streamSinge = env.addSource(consumer).map(
      x => {
        var kafkaMessege: KafkaMessege = JSON.parseObject(x, classOf[KafkaMessege])
        kafkaMessege
      }
    ).map(x => {
      var result = new GrokUtil().getMesssge(x.message)
      var logMessege: LogMessege = JSON.parseObject(result, classOf[LogMessege])
      logMessege.key = logMessege.ddataId + "||" + logMessege.station
      logMessege
    })
    val httpHosts = new java.util.ArrayList[HttpHost]
    httpHosts.add(new HttpHost("10.211.55.7", 9200, "http")) //es的client通过http请求连接到es进行增删改查操作

    val esSinkBuilder = new ElasticsearchSink.Builder[LogMessege](
      httpHosts,
      new ElasticsearchSinkFunction[LogMessege] { //参数element就是上面清洗好的数据格式
        def createIndexRequest(element: LogMessege): IndexRequest = {
          return Requests.indexRequest()
            .index("htht5")
            .`type`("doc").id("1")
            .source(JSON.toJSONString(element,new SerializeConfig()),XContentType.JSON)
        }

        override def process(element: LogMessege, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          //requestIndexer.add(createUpdateRequest(element))
          requestIndexer.add(createIndexRequest(element))
        }
      }
    )
    esSinkBuilder.setFailureHandler(new ActionRequestFailureHandler() {
      @throws[Throwable]
      override def onFailure(actionRequest: ActionRequest, throwable: Throwable, i: Int, requestIndexer: RequestIndexer): Unit = {
        if (ExceptionUtils.findThrowable(throwable, classOf[EsRejectedExecutionException]).isPresent) {

        } else {
          print(throwable)
          throw throwable
        }
      }
    })

    streamSinge.addSink(esSinkBuilder.build())
    val esSinkBuilderCount = new ElasticsearchSink.Builder[StationCount](
      httpHosts,
      new ElasticsearchSinkFunction[StationCount] { //参数element就是上面清洗好的数据格式
        def createUpdateRequest(element: StationCount): UpdateRequest = {
          val json = new java.util.HashMap[String, String]

          var updateRequest: UpdateRequest = new UpdateRequest();
          updateRequest.index("htht");
          updateRequest.`type`("doc");
          updateRequest.id("1").docAsUpsert(true).scriptedUpsert(true);
          updateRequest.doc(JSON.toJSONString(element,new SerializeConfig()),XContentType.JSON)
          updateRequest
          /*return Requests.indexRequest()
             .index("htht")
             .`type`("location")
               .id("1")
             .source(json)*/
          /* return new UpdateRequest().index("htht")
              .`type`("location")
              .id("1").doc(json)*/
        }

        override def process(element: StationCount, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
             requestIndexer.add(createUpdateRequest(element))
        }
      }
    )
    var count = streamSinge
      .filter(x => {
        if (x != null) {
          true
        } else {
          false
        }
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[LogMessege](Time.minutes(1)) {
        override def extractTimestamp(element: LogMessege): Long = {
          var a = element.ddatatime.getTime
          return a
        }
      })
      .keyBy(_.key).timeWindow(Time.minutes(5))
      //自定义触发器
      .trigger(new CountTrigger(10, 5 * 1000))
      .sideOutputLateData(outputTag)
      //.trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(5)))
      .aggregate(new CountFunction(), new WindowResult())

    val output = count.getSideOutput(outputTag)
    // 过时数据可以存储在存储介质中，延后处理
    output.map(f => {
      println(s"过时数据：$f")
    })
    count.print()
    count.addSink(esSinkBuilderCount.build())

    //esSinkBuilderCount.setBulkFlushMaxActions(1)

    // print(JSON.toJSONString(b,new SerializeConfig()))
    /*stream.map(x=>{
      (x.station,1)
    }).keyBy(_._1).timeWindow(Time.minutes(5),Time.seconds(10)).sum(1).print()*/
    //stream.print()
    env.execute("StreamingKafka")

  }


}
*/
