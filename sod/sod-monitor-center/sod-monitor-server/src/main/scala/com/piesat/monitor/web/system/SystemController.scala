package com.piesat.monitor.web.system

import com.piesat.monitor.entity.Test
import com.piesat.util.ResultT
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}
import java.util.{List => JavaList}

import com.piesat.monitor.entity.system._
import com.piesat.monitor.rpc.service.system._
import org.springframework.beans.factory.annotation.Autowired

/**
  * Created by zzj on 2020/3/24.
  */
@RestController
@Api (value = "系统分监视接口", tags =  Array ("系统分监视接口")  )
@RequestMapping(Array("/system"))
class SystemController @Autowired()(cpuService: CpuService,
                                    memoryService: MemoryService,
                                    fileService:FileService,
                                    processService:ProcessService,
                                    netWorkService: NetWorkService) {
  @GetMapping(Array("/cpuUsage"))
  @ApiOperation(value = "查询时间范围内cpu使用率", notes = "查询时间范围内cpu使用率")
  def cpuList(cpuEntity:CpuEntity):ResultT[JavaList[CpuEntity]]= {
    var list = cpuService.list(cpuEntity);
    var resultT = new ResultT[JavaList[CpuEntity]]()
    resultT.setData(list);
    return resultT
  }
  @GetMapping(Array("/memoryUsage"))
  @ApiOperation(value = "查询时间范围内存使用率", notes = "查询时间范围内存使用率")
  def memoryList(memoryEntity: MemoryEntity):ResultT[JavaList[MemoryEntity]]= {
    var list = memoryService.list(memoryEntity);
    var resultT = new ResultT[JavaList[MemoryEntity]]()
    resultT.setData(list);
    return resultT
  }
  @GetMapping(Array("/fileUsage"))
  @ApiOperation(value = "查询磁盘使用信息", notes = "查询磁盘使用信息")
  def fileList(fileEntity: FileEntity):ResultT[JavaList[FileEntity]]= {
    var list = fileService.list(fileEntity);
    var resultT = new ResultT[JavaList[FileEntity]]()
    resultT.setData(list);
    return resultT
  }
  @GetMapping(Array("/processUsage"))
  @ApiOperation(value = "查询进程信息", notes = "查询进程信息")
  def processList(processEntity: ProcessEntity):ResultT[JavaList[ProcessEntity]]= {
    var list = processService.list(processEntity);
    var resultT = new ResultT[JavaList[ProcessEntity]]()
    resultT.setData(list);
    return resultT
  }
  @GetMapping(Array("/netWorkUsage"))
  @ApiOperation(value = "查询流量信息", notes = "查询流量信息")
  def netWorkList(netWorkEntity: NetWorkEntity):ResultT[JavaList[NetWorkEntity]]= {
    var list = netWorkService.list(netWorkEntity);
    var resultT = new ResultT[JavaList[NetWorkEntity]]()
    resultT.setData(list);
    return resultT
  }
}
