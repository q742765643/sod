package com.piesat.calculate

import java.util.Properties

import com.fasterxml.jackson.databind.ObjectMapper
import com.piesat.calculate.entity.KafkaMessege
import com.piesat.calculate.entity.task.DiTaskConfiguration
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

/**
  * Created by zzj on 2020/3/17.
  */
object KafkaTaskDemo {
  def main(args: Array[String]): Unit = {
    val prop = new Properties
    // 指定请求的kafka集群列表
    prop.put("bootstrap.servers", "meteo-cloud1:9092,meteo-cloud2:9092,meteo-cloud3:9092") // 指定响应方式

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
    // 模拟一些数据并发送给kafka
    for (i <- 1 to 1000) {
      var kafkaMessege: KafkaMessege = new KafkaMessege()
      var diTaskConfiguration: DiTaskConfiguration = new DiTaskConfiguration()
      diTaskConfiguration.taskId="AAAAA"
      diTaskConfiguration.system="SOD"
      diTaskConfiguration.taskName="测试"
      diTaskConfiguration.taskDuty="1"
      diTaskConfiguration.taskCron="0 0 */1 * * ?"
      diTaskConfiguration.overtime=20
      diTaskConfiguration.taskMaxTime=3
      diTaskConfiguration.offset=11
      diTaskConfiguration.alarmtime=120
      diTaskConfiguration.isAlarm=1
      diTaskConfiguration.dataType="A.0001.0002.S003"
      diTaskConfiguration.sendPhys="HADB"
      diTaskConfiguration.isDelete=0
      var messege = objectMapper.writeValueAsString(diTaskConfiguration)
      print(messege)
      kafkaMessege.message = "2020-04-08 09:35:28.127  INFO 209960 --- [           main] c.p.s.client.ScheduleClientApplication   :DI_TASK_CONFIG=" + messege
      // 得到返回值
      val rmd: RecordMetadata = producer.send(new ProducerRecord[String, String]("ditaskconfcollect", objectMapper.writeValueAsString(kafkaMessege))).get()
      println(rmd.toString)
      //Thread.sleep(3000)
    }


    producer.close()
  }

}
