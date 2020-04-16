package com.piesat.monitor.web.task

import java.util.{List => JavaList}

import com.piesat.monitor.entity.task.DiTaskConfiguration
import com.piesat.monitor.rpc.service.task.DiTaskConfigurationService
import com.piesat.util.ResultT
import com.piesat.util.page.{PageBean, PageForm}
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "任务配置监控接口", tags =  Array ("任务配置监控接口")  )
@RequestMapping(Array("/diTaskConfiguration"))
class DiTaskConfigurationController  @Autowired()(DiTaskConfigurationService: DiTaskConfigurationService){
  @GetMapping(Array("/page"))
  @ApiOperation(value = "分页查询任务配置监控接口", notes = "分页查询任务配置监控接口")
  def page(DiTaskConfiguration: DiTaskConfiguration,pageNum: Int, pageSize: Int): ResultT[PageBean[DiTaskConfiguration]]={
    var resultT = new ResultT[PageBean[DiTaskConfiguration]]
    val pageForm = new PageForm[DiTaskConfiguration](pageNum, pageSize, DiTaskConfiguration)
    var pageBean=DiTaskConfigurationService.page(pageForm)
    resultT.setData(pageBean)
    return  resultT

  }

  @GetMapping(Array("/findById"))
  @ApiOperation(value = "根据id查询任务配置监控接口", notes = "根据id查询任务配置监控接口")
  def findById(id:String):ResultT[DiTaskConfiguration]={
    var resultT = new ResultT[DiTaskConfiguration]()
    var DiTaskConfiguration=DiTaskConfigurationService.findById(id)
    resultT.setData(DiTaskConfiguration)
    return resultT

  }
  @GetMapping(Array("/list"))
  @ApiOperation(value = "查询全部任务配置监控接口", notes = "查询全部任务配置监控接口")
  def cpuList(DiTaskConfiguration: DiTaskConfiguration):ResultT[JavaList[DiTaskConfiguration]]= {
    var list = DiTaskConfigurationService.list(DiTaskConfiguration);
    var resultT = new ResultT[JavaList[DiTaskConfiguration]]()
    resultT.setData(list);
    return resultT
  }
  @GetMapping(Array("/selectAlias"))
  @ApiOperation(value = "查询别名", notes = "查询别名")
  def selectAlias:ResultT[JavaList[String]]={
    var list = DiTaskConfigurationService.selectAlias;
    var resultT = new ResultT[JavaList[String]]()
    resultT.setData(list);
    return resultT
  }

}
