package com.piesat.monitor.web.kafka

import com.piesat.monitor.entity.kafka.KafkaMonitor
import com.piesat.monitor.entity.system.CpuEntity
import com.piesat.monitor.rpc.service.kafka.KafkaMonitorService
import com.piesat.monitor.rpc.service.system.CpuService
import com.piesat.util.ResultT
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}
import java.util.{List => JavaList}

/**
  * Created by zzj on 2020/3/31.
  */
@RestController
@Api (value = "kafka监视接口", tags =  Array ("kafka监视接口")  )
@RequestMapping(Array("/kafkaMonitor"))
class KafkaMonitorController @Autowired()(kafkaMonitorService: KafkaMonitorService){
  @GetMapping(Array("/lag"))
  @ApiOperation(value = "kafka积压情况", notes = "kafka积压情况")
  def cpuList(kafkaMonitor:KafkaMonitor):ResultT[JavaList[KafkaMonitor]]= {
    var list = kafkaMonitorService.list(kafkaMonitor);
    var resultT = new ResultT[JavaList[KafkaMonitor]]()
    resultT.setData(list);
    return resultT
  }
}
