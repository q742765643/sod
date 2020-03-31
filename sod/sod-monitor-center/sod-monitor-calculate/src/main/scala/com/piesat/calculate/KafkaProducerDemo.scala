package com.piesat.calculate

import java.util.{Date, Properties}

import com.fasterxml.jackson.databind.ObjectMapper
import com.piesat.calculate.entity.KafkaMessege
import com.piesat.calculate.entity.transfer.{StationLevelEntity, StationLevelFiledEntity}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

/**
  * Created by zzj on 2020/3/17.
  */
object KafkaProducerDemo {
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
    // 模拟一些数据并发送给kafka
    for (i <- 1 to 10) {
      var kafkaMessege: KafkaMessege = new KafkaMessege()
      var stationLevel: StationLevelEntity = new StationLevelEntity()
      var stationLevelFiled: StationLevelFiledEntity = new StationLevelFiledEntity();
      stationLevel.setName("文件级资料处理详细信息")
      stationLevel.setMessage("文件级资料处理详细信息")
      stationLevel.setStype("RT.CTS.STATION.DI")
      stationLevelFiled.setDataType("A.0001.0002.S003")
      stationLevelFiled.setDataType1("A.0001.0002.R003")
      stationLevelFiled.setTt("tt")
      stationLevelFiled.setDataUpdateFlag("000")
      stationLevelFiled.setReceive("A.0001.0002.R003")
      stationLevelFiled.setSend("A.0001.0002.S003")
      stationLevelFiled.setTranTime(new Date())
      stationLevelFiled.setDataTime(new Date(System.currentTimeMillis() - 2000))
      stationLevelFiled.setSystem("CTS")
      stationLevelFiled.setIiiii("11")
      stationLevelFiled.setProcessLink("1")
      stationLevelFiled.setProcessStartTime(new Date)
      stationLevelFiled.setProcessEndTime(new Date)
      stationLevelFiled.setFileNameO("aa.zip")
      stationLevelFiled.setFileNameN("aa.zip")
      stationLevelFiled.setProcessState("1")
      stationLevelFiled.setBusinessState("1")
      stationLevelFiled.setRecordTime(new Date())
      stationLevel.setStationLevelFiledEntity(stationLevelFiled)
      var messege = objectMapper.writeValueAsString(stationLevel)
      print(messege)
      kafkaMessege.message = "2020-03-16 17:00:13  [ main:6004 ] - [ DEBUG ]  " + messege
      // 得到返回值
      val rmd: RecordMetadata = producer.send(new ProducerRecord[String, String]("test", objectMapper.writeValueAsString(kafkaMessege))).get()
      println(rmd.toString)
      Thread.sleep(1000)
    }


    producer.close()
  }

}
