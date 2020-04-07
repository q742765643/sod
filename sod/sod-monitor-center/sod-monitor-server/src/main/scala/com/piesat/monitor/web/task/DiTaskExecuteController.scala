package com.piesat.monitor.web.task

import java.util.{List => JavaList}

import com.piesat.monitor.entity.task.{DiTaskExecute, DiTaskStatistics}
import com.piesat.monitor.rpc.service.task.DiTaskExecuteService
import com.piesat.util.ResultT
import com.piesat.util.page.{PageBean, PageForm}
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "任务日志监控接口", tags =  Array ("任务日志监控接口")  )
@RequestMapping(Array("/diTaskExecute"))
class DiTaskExecuteController  @Autowired()(diTaskExecuteService: DiTaskExecuteService){
  @GetMapping(Array("/page"))
  @ApiOperation(value = "分页查询任务日志监控接口", notes = "分页查询任务日志监控接口")
  def page(diTaskExecute: DiTaskExecute,pageNum: Int, pageSize: Int): ResultT[PageBean[DiTaskExecute]]={
    var resultT = new ResultT[PageBean[DiTaskExecute]]
    val pageForm = new PageForm[DiTaskExecute](pageNum, pageSize, diTaskExecute)
    var pageBean=diTaskExecuteService.page(pageForm)
    resultT.setData(pageBean)
    return  resultT

  }

  @GetMapping(Array("/findById"))
  @ApiOperation(value = "根据id查询任务日志监控接口", notes = "根据id查询任务日志监控接口")
  def findById(id:String):ResultT[DiTaskExecute]={
    var resultT = new ResultT[DiTaskExecute]()
    var DiTaskExecute=diTaskExecuteService.findById(id)
    resultT.setData(DiTaskExecute)
    return resultT

  }
  @GetMapping(Array("/list"))
  @ApiOperation(value = "查询全部任务日志监控接口", notes = "查询全部任务日志监控接口")
  def cpuList(diTaskExecute: DiTaskExecute):ResultT[JavaList[DiTaskExecute]]= {
    var list = diTaskExecuteService.list(diTaskExecute);
    var resultT = new ResultT[JavaList[DiTaskExecute]]()
    resultT.setData(list);
    return resultT
  }
  @GetMapping(Array("/groupbyTaskDuty"))
  @ApiOperation(value = "日志统计接口", notes = "日志统计接口")
  def groupbyTaskDuty(diTaskExecute:DiTaskExecute): ResultT[JavaList[DiTaskStatistics]]={
    var list = diTaskExecuteService.groupbyTaskDuty(diTaskExecute);
    var resultT = new ResultT[JavaList[DiTaskStatistics]]()
    resultT.setData(list);
    return resultT
  }

}
