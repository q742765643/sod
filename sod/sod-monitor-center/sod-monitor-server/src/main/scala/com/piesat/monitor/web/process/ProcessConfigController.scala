package com.piesat.monitor.web.process

import com.piesat.monitor.entity.process.ProcessConfig
import com.piesat.monitor.entity.ssh.SshEntity
import com.piesat.monitor.rpc.service.process.ProcessConfigService
import com.piesat.monitor.rpc.service.ssh.SshService
import com.piesat.monitor.rpc.service.system.ProcessService
import com.piesat.util.ResultT
import com.piesat.util.page.{PageBean, PageForm}
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._
import java.util.{List => JavaList}

import com.piesat.monitor.entity.system.CpuEntity

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "进程接口", tags =  Array ("进程接口")  )
@RequestMapping(Array("/processConfig"))
class ProcessConfigController  @Autowired()(processConfigService: ProcessConfigService){
  @GetMapping(Array("/page"))
  @ApiOperation(value = "分页查询进程", notes = "分页查询进程")
  def page(processConfig: ProcessConfig,pageNum: Int, pageSize: Int): ResultT[PageBean[ProcessConfig]]={
    var resultT = new ResultT[PageBean[ProcessConfig]]
    val pageForm = new PageForm[ProcessConfig](pageNum, pageSize, processConfig)
    var pageBean=processConfigService.page(pageForm)
    resultT.setData(pageBean)
    return  resultT

  }
  @PostMapping(Array("/save"))
  @ApiOperation(value = "添加进程", notes = "添加进程")
  def save(@RequestBody
           processConfig: ProcessConfig):ResultT[String]= {
    var resultT = new ResultT[String]()
    processConfigService.save(processConfig)

    return resultT
  }
  @PostMapping(Array("/update"))
  @ApiOperation(value = "修改进程", notes = "修改进程")
  def update(@RequestBody
             processConfig: ProcessConfig):ResultT[String]= {
    var resultT = new ResultT[String]()
    processConfigService.update(processConfig)

    return resultT
  }
  @GetMapping(Array("/deleteById"))
  @ApiOperation(value = "根据id删除进程", notes = "根据id删除进程")
  def deleteById(id:String):ResultT[String]={
    var resultT = new ResultT[String]()
    processConfigService.delete(id)
    return resultT

  }
  @GetMapping(Array("/findById"))
  @ApiOperation(value = "根据id查询进程", notes = "根据id查询进程")
  def findById(id:String):ResultT[ProcessConfig]={
    var resultT = new ResultT[ProcessConfig]()
    var processConfig=processConfigService.findById(id)
    resultT.setData(processConfig)
    return resultT

  }
  @GetMapping(Array("/list"))
  @ApiOperation(value = "查询全部进程", notes = "查询全部进程")
  def cpuList(processConfig: ProcessConfig):ResultT[JavaList[ProcessConfig]]= {
    var list = processConfigService.list(processConfig);
    var resultT = new ResultT[JavaList[ProcessConfig]]()
    resultT.setData(list);
    return resultT
  }


}
