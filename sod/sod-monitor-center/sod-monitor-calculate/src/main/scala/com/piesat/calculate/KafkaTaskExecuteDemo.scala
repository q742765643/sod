package com.piesat.calculate

import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import com.fasterxml.jackson.databind.ObjectMapper
import com.piesat.calculate.entity.KafkaMessege
import com.piesat.calculate.entity.task.{DiTaskConfiguration, DiTaskExecute}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

/**
  * Created by zzj on 2020/3/17.
  */
object KafkaTaskExecuteDemo {
  def main(args: Array[String]): Unit = {
    val prop = new Properties
    // 指定请求的kafka集群列表
    prop.put("bootstrap.servers", "10.211.55.7:9092") // 指定响应方式
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
    var objectMapper = new ObjectMapper()
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    // 模拟一些数据并发送给kafka
    for (i <- 1 to 1) {
      var kafkaMessege: KafkaMessege = new KafkaMessege()
      var diTaskConfiguration: DiTaskExecute = new DiTaskExecute()
      diTaskConfiguration.currentTaskId="1111"
      diTaskConfiguration.taskId="AAAAA"
      diTaskConfiguration.system="SOD"
      diTaskConfiguration.taskName="测试"
      diTaskConfiguration.startTimeS=sdf.format(new Date(1586181600000l))
      diTaskConfiguration.startTimeL=1586181600000l
      diTaskConfiguration.startTimeA=111
      diTaskConfiguration.endTimeA=3111
      diTaskConfiguration.taskState="成功"
      diTaskConfiguration.taskDetail="1111111111"
      diTaskConfiguration.taskErrorTime=new Date()
      diTaskConfiguration.dataType="A.0001.0002.S003"
      diTaskConfiguration.sendPhys="HADB"
      diTaskConfiguration.taskErrorDetail="1111"
      diTaskConfiguration.taskErrorReason="222"
      diTaskConfiguration.recordTime=new Date()
      var messege = objectMapper.writeValueAsString(diTaskConfiguration)
      print(messege)
      kafkaMessege.message = "2020-03-16 17:00:13  [ main:6004 ] - [ DEBUG ]  " + messege
      // 得到返回值
      val rmd: RecordMetadata = producer.send(new ProducerRecord[String, String]("taskExecute", objectMapper.writeValueAsString(kafkaMessege))).get()
      println(rmd.toString)
      Thread.sleep(3000)
    }


    producer.close()
  }

}