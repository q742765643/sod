package com.piesat.calculate

import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.piesat.calculate.entity.{KafkaMessege, LogMessege}
import com.piesat.calculate.main.StreamingKafka.ZOOKEEPER_HOST
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

/**
  * Created by zzj on 2020/3/17.
  */
object KafkaProducerDemo {
  def main(args: Array[String]): Unit = {
    val prop = new Properties
    // 指定请求的kafka集群列表
    prop.put("bootstrap.servers", "10.211.55.7:9092")// 指定响应方式
    prop.setProperty("zookeeper.connect", "10.211.55.7:2181")

    //prop.put("acks", "0")
    prop.put("acks", "all")
    // 请求失败重试次数
    //prop.put("retries", "3")
    // 指定key的序列化方式, key是用于存放数据对应的offset
    prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    // 指定value的序列化方式
    prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    // 配置超时时间
    prop.put("request.timeout.ms", "60000")
    //prop.put("batch.size", "16384")
    //prop.put("linger.ms", "1")
    //prop.put("buffer.memory", "33554432")

    // 得到生产者的实例
    val producer = new KafkaProducer[String, String](prop)

    // 模拟一些数据并发送给kafka
    for (i <- 1 to 10) {
      var kafkaMessege:KafkaMessege=new KafkaMessege()
      var logMessege:LogMessege=new LogMessege();
      logMessege.ddataId="A.00001"
      logMessege.ddatatime=new Date(System.currentTimeMillis()-2000)
      logMessege.station="自动站"
      val conf = new SerializeConfig()

      var messege=JSON.toJSONString(logMessege,conf)
      print(messege)
      kafkaMessege.message="2020-03-16 17:00:13  [ main:6004 ] - [ DEBUG ]  {'ddataId':'A.00001','ddatatime':'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()-2000)+"','station':'自动站2'}"
      // 得到返回值
      val rmd: RecordMetadata = producer.send(new ProducerRecord[String, String]("test", JSON.toJSONString(kafkaMessege,conf))).get()
      println(rmd.toString)
    }
    for (i <- 1 to 1) {
      var kafkaMessege:KafkaMessege=new KafkaMessege()
      var logMessege:LogMessege=new LogMessege();
      logMessege.ddataId="A.00002"
      logMessege.ddatatime=new Date(System.currentTimeMillis()-2000)
      logMessege.station="自动站"
      val conf = new SerializeConfig()

      var messege=JSON.toJSONString(logMessege,conf)
      print(messege)
      kafkaMessege.message="2020-03-16 17:00:13  [ main:6004 ] - [ DEBUG ]  {'ddataId':'A.00001','ddatatime':'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()-2000)+"','station':'自动站'}"
      // 得到返回值
      val rmd: RecordMetadata = producer.send(new ProducerRecord[String, String]("test", JSON.toJSONString(kafkaMessege,conf))).get()
      println(rmd.toString)
    }

    producer.close()
  }

}
