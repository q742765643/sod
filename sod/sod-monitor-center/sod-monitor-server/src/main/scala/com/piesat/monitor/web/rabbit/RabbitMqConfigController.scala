package com.piesat.monitor.web.rabbit

import com.piesat.monitor.entity.rabbitmq.RabbitMqConfig
import com.piesat.monitor.rpc.service.rabbit.RabbitMqConfigService
import com.piesat.util.ResultT
import com.piesat.util.page.{PageBean, PageForm}
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._
import java.util.{List => JavaList}

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "rabbitMq配置管理接口", tags =  Array ("rabbitMq配置管理接口")  )
@RequestMapping(Array("/rabbitMqConfig"))
class RabbitMqConfigController  @Autowired()(rabbitMqConfigService: RabbitMqConfigService){
  @GetMapping(Array("/page"))
  @ApiOperation(value = "分页查询rabbitMq配置", notes = "分页查询rabbitMq配置")
  def page(rabbitMqConfig: RabbitMqConfig,pageNum: Int, pageSize: Int): ResultT[PageBean[RabbitMqConfig]]={
    var resultT = new ResultT[PageBean[RabbitMqConfig]]
    val pageForm = new PageForm[RabbitMqConfig](pageNum, pageSize, rabbitMqConfig)
    var pageBean=rabbitMqConfigService.page(pageForm)
    resultT.setData(pageBean)
    return  resultT

  }
  @PostMapping(Array("/save"))
  @ApiOperation(value = "添加rabbitMq配置", notes = "添加rabbitMq配置")
  def save(@RequestBody
           rabbitMqConfig: RabbitMqConfig):ResultT[String]= {
    var resultT = new ResultT[String]()
    rabbitMqConfigService.save(rabbitMqConfig)

    return resultT
  }
  @PostMapping(Array("/update"))
  @ApiOperation(value = "修改rabbitMq配置", notes = "修改rabbitMq配置")
  def update(@RequestBody
           rabbitMqConfig: RabbitMqConfig):ResultT[String]= {
    var resultT = new ResultT[String]()
    rabbitMqConfigService.update(rabbitMqConfig)

    return resultT
  }
  @GetMapping(Array("/deleteById"))
  @ApiOperation(value = "根据id删除rabbitMq配置", notes = "根据id删除rabbitMq配置")
  def deleteById(id:String):ResultT[String]={
    var resultT = new ResultT[String]()
    rabbitMqConfigService.delete(id)
    return resultT

  }
  @GetMapping(Array("/findById"))
  @ApiOperation(value = "根据id查询rabbitMq配置", notes = "根据id查询rabbitMq配置")
  def findById(id:String):ResultT[RabbitMqConfig]={
    var resultT = new ResultT[RabbitMqConfig]()
    var rabbitMqConfig=rabbitMqConfigService.findById(id)
    resultT.setData(rabbitMqConfig)
    return resultT

  }

  @GetMapping(Array("/selectAlias"))
  @ApiOperation(value = "查询别名", notes = "查询别名")
  def selectAlias:ResultT[JavaList[String]]={
    var list = rabbitMqConfigService.selectAlias;
    var resultT = new ResultT[JavaList[String]]()
    resultT.setData(list);
    return resultT
  }

}
