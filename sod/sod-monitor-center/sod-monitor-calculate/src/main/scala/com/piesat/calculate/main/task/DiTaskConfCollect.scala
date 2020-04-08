package com.piesat.calculate.main.task

import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import com.piesat.calculate.entity.KafkaMessege
import com.piesat.calculate.entity.task.DiTaskConfiguration
import com.piesat.calculate.sink.EsSink
import com.piesat.calculate.util.GrokUtil
import com.piesat.calculate.util.config.SystemConfig
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

/**
  * Created by zzj on 2020/4/3.
  */
object DiTaskConfCollect {
  def main(args: Array[String]): Unit = {
    var SystemConfig = new SystemConfig
    val env = SystemConfig.loadEnv()
    val topic = SystemConfig.loadTopic("config/DiTaskConfCollect.properties")
    //    kafka的配置信息
    val kafkaProps = SystemConfig.loadKafkaProperties("config/DiTaskConfCollect.properties")
    //    外部的检查点  保留撤销

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
        if (result.startsWith("DI_TASK_CONFIG=")) {
          result = result.replace("DI_TASK_CONFIG=", "")
          var diTaskConfiguration: DiTaskConfiguration = objectMapper.readValue(result, classOf[DiTaskConfiguration])
          diTaskConfiguration.id = diTaskConfiguration.taskId
          diTaskConfiguration
        } else {
          null
        }

      } catch {
        case ex: Exception => {
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
    streamSinge.addSink(new EsSink[DiTaskConfiguration].sinkAddTask("di_task_configuration"))
    env.execute("DiTaskConfCollect")

  }
}
