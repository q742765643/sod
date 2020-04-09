package com.piesat

import java.util.Properties

import com.alibaba.fastjson.JSON
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer


//case class ApacheLogEvent(ip: String, userId: String, eventTime: Long, method: String, url: String)
//case class UrlViewCount(url: String, windowEnd: Long, count: Long)
/**
  * Created by zzj on 2020/3/16.
  */
object StreamingKafkaClearB {
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

    val consumer = new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), kafkaProps)
    val stream = env.addSource(consumer).map(
      x => {
        JSON.parseObject(x)
      }
    ).map(x => {
      var a = x.getString("name")
    })
    // Time.seconds是设定延迟时间，要具体看数据本身的延迟程度；超出的可以通过窗口处理，再不行要通过SideOutput

    env.execute("StreamingKafkaClear")


  }
}
