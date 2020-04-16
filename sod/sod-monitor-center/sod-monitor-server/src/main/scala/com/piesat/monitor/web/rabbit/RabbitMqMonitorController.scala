package com.piesat.monitor.web.rabbit

import java.util.{List => JavaList}

import com.piesat.monitor.entity.rabbitmq.RabbitMqMonitor
import com.piesat.monitor.rpc.service.rabbit.RabbitMqMonitorService
import com.piesat.util.ResultT
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestMapping, RestController}

/**
  * Created by zzj on 2020/3/31.
  */
@RestController
@Api (value = "rabbit监视接口", tags =  Array ("rabbit监视接口")  )
@RequestMapping(Array("/rabbitMqMonitor"))
class RabbitMqMonitorController @Autowired()(rabbitMqMonitorService: RabbitMqMonitorService){
  @PostMapping(Array("/lag"))
  @ApiOperation(value = "rabbit积压情况", notes = "rabbit积压情况")
  def cpuList(rabbitMqMonitor:RabbitMqMonitor):ResultT[JavaList[RabbitMqMonitor]]= {
    var list = rabbitMqMonitorService.list(rabbitMqMonitor);
    var resultT = new ResultT[JavaList[RabbitMqMonitor]]()
    resultT.setData(list);
    return resultT
  }
}
