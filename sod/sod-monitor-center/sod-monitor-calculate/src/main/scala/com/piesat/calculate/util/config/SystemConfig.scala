package com.piesat.calculate.util.config

import java.util.{Properties, ArrayList => JavaList}

import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.http.HttpHost

/**
  * Created by zzj on 2020/3/27.
  */
class SystemConfig {
  def loadEnv() = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //    设置检查点时间为5秒
    env.enableCheckpointing(5000)
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

    env
    //    没有设置检查点请自行添加
    //env.getStateBackend(new FsStateBackend("hdfs://master:9000/flink/checkpoints"))

  }

  def loadKafkaProperties(path: String): Properties = {
    var systemConfig: Properties = loadProperties("application.properties")
    var ownConfig: Properties = loadProperties(path)
    val kafkaProps = new Properties()
    //kafkaProps.setProperty("zookeeper.connect", systemConfig.getProperty("ht.zookeeper.host"))
    kafkaProps.setProperty("bootstrap.servers", systemConfig.getProperty("ht.kafka.bootstrap-servers"))
    kafkaProps.setProperty("group.id", ownConfig.getProperty("ht.kafka.group.id"))
    kafkaProps
  }

  def loadTopic(path: String): String = {
    var ownConfig: Properties = loadProperties(path)
    ownConfig.getProperty("ht.kafka.topic")
  }

  def loadProperties(path: String): Properties = {
    val properties = new Properties()
    val in = this.getClass.getClassLoader.getResourceAsStream(path) //文件要放到resource文件夹下
    properties.load(in)
    properties
  }

  def loadEsProperties(): JavaList[HttpHost] = {
    val httpHosts = new java.util.ArrayList[HttpHost]
    var systemConfig: Properties = loadProperties("application.properties")
    var es = systemConfig.getProperty("ht.elasticsearch.cluster-nodes")
    es.split(",").foreach(s => {
      var x: Array[String] = s.split(":")
      var httpHost = new HttpHost(x.apply(0), x.apply(1).toInt, "http")
      httpHosts.add(httpHost)
    })
    httpHosts

  }

}
