package com.piesat.monitor.web.flow

import java.util.{List => JavaList}

import com.piesat.monitor.entity.flow.FlowMonitor
import com.piesat.monitor.rpc.service.flow.FlowMonitorService
import com.piesat.util.ResultT
import com.piesat.util.page.{PageBean, PageForm}
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "全流程监控接口", tags =  Array ("全流程监控接口")  )
@RequestMapping(Array("/flowMonitor"))
class FlowMonitorController  @Autowired()(flowMonitorService: FlowMonitorService){
  @GetMapping(Array("/page"))
  @ApiOperation(value = "分页查询全流程监控", notes = "分页查询全流程监控")
  def page(flowMonitor: FlowMonitor,pageNum: Int, pageSize: Int): ResultT[PageBean[FlowMonitor]]={
    var resultT = new ResultT[PageBean[FlowMonitor]]
    val pageForm = new PageForm[FlowMonitor](pageNum, pageSize, flowMonitor)
    var pageBean=flowMonitorService.page(pageForm)
    resultT.setData(pageBean)
    return  resultT

  }

  @GetMapping(Array("/findById"))
  @ApiOperation(value = "根据id查询全流程监控", notes = "根据id查询全流程监控")
  def findById(id:String):ResultT[FlowMonitor]={
    var resultT = new ResultT[FlowMonitor]()
    var flowMonitor=flowMonitorService.findById(id)
    resultT.setData(flowMonitor)
    return resultT

  }
  @GetMapping(Array("/list"))
  @ApiOperation(value = "查询全部全流程监控", notes = "查询全部全流程监控")
  def cpuList(flowMonitor: FlowMonitor):ResultT[JavaList[FlowMonitor]]= {
    var list = flowMonitorService.list(flowMonitor);
    var resultT = new ResultT[JavaList[FlowMonitor]]()
    resultT.setData(list);
    return resultT
  }


}
