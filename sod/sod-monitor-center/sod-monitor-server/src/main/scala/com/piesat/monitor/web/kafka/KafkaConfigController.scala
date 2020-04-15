package com.piesat.monitor.web.kafka

import java.util.{List => JavaList}

import com.piesat.monitor.entity.kafka.KafkaConfig
import com.piesat.monitor.rpc.service.kafka.KafkaConfigService
import com.piesat.util.ResultT
import com.piesat.util.page.{PageBean, PageForm}
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "Kafka配置接口", tags =  Array ("Kafka配置接口")  )
@RequestMapping(Array("/kafkaConfig"))
class KafkaConfigController  @Autowired()(kafkaConfigService: KafkaConfigService){
  @GetMapping(Array("/page"))
  @ApiOperation(value = "分页查询Kafka配置", notes = "分页查询Kafka配置")
  def page(kafkaConfig: KafkaConfig,pageNum: Int, pageSize: Int): ResultT[PageBean[KafkaConfig]]={
    var resultT = new ResultT[PageBean[KafkaConfig]]
    val pageForm = new PageForm[KafkaConfig](pageNum, pageSize, kafkaConfig)
    var pageBean=kafkaConfigService.page(pageForm)
    resultT.setData(pageBean)
    return  resultT

  }
  @PostMapping(Array("/save"))
  @ApiOperation(value = "添加Kafka配置", notes = "添加Kafka配置")
  def save(@RequestBody
           kafkaConfig: KafkaConfig):ResultT[String]= {
    var resultT = new ResultT[String]()
    kafkaConfigService.save(kafkaConfig)

    return resultT
  }
  @PostMapping(Array("/update"))
  @ApiOperation(value = "修改Kafka配置", notes = "修改Kafka配置")
  def update(@RequestBody
             kafkaConfig: KafkaConfig):ResultT[String]= {
    var resultT = new ResultT[String]()
    kafkaConfigService.update(kafkaConfig)

    return resultT
  }
  @GetMapping(Array("/deleteById"))
  @ApiOperation(value = "根据id删除Kafka配置", notes = "根据id删除Kafka配置")
  def deleteById(id:String):ResultT[String]={
    var resultT = new ResultT[String]()
    kafkaConfigService.delete(id)
    return resultT

  }
  @GetMapping(Array("/findById"))
  @ApiOperation(value = "根据id查询Kafka配置", notes = "根据id查询Kafka配置")
  def findById(id:String):ResultT[KafkaConfig]={
    var resultT = new ResultT[KafkaConfig]()
    var kafkaConfig=kafkaConfigService.findById(id)
    resultT.setData(kafkaConfig)
    return resultT

  }
  @GetMapping(Array("/list"))
  @ApiOperation(value = "查询全部Kafka配置", notes = "查询全部Kafka配置")
  def cpuList(kafkaConfig: KafkaConfig):ResultT[JavaList[KafkaConfig]]= {
    var list = kafkaConfigService.list(kafkaConfig);
    var resultT = new ResultT[JavaList[KafkaConfig]]()
    resultT.setData(list);
    return resultT
  }
  @GetMapping(Array("/selectAlias"))
  @ApiOperation(value = "查询别名", notes = "查询别名")
  def selectAlias:ResultT[JavaList[String]]={
    var list = kafkaConfigService.selectAlias;
    var resultT = new ResultT[JavaList[String]]()
    resultT.setData(list);
    return resultT
  }


}
