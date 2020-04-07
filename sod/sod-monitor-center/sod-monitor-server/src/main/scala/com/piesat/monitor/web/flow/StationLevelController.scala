package com.piesat.monitor.web.flow

import java.util.{List => JavaList}

import com.piesat.monitor.entity.flow.StationLevel
import com.piesat.monitor.rpc.service.flow.StationLevelService
import com.piesat.util.ResultT
import com.piesat.util.page.{PageBean, PageForm}
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "全流程详细记录接口", tags =  Array ("全流程详细记录接口")  )
@RequestMapping(Array("/stationLevel"))
class StationLevelController  @Autowired()(stationLevelService: StationLevelService){
  @GetMapping(Array("/page"))
  @ApiOperation(value = "分页查询全流程详细记录", notes = "分页查询全流程详细记录")
  def page(stationLevel: StationLevel,pageNum: Int, pageSize: Int): ResultT[PageBean[StationLevel]]={
    var resultT = new ResultT[PageBean[StationLevel]]
    val pageForm = new PageForm[StationLevel](pageNum, pageSize, stationLevel)
    var pageBean=stationLevelService.page(pageForm)
    resultT.setData(pageBean)
    return  resultT

  }

  @GetMapping(Array("/findById"))
  @ApiOperation(value = "根据id查询全流程详细记录", notes = "根据id查询全流程详细记录")
  def findById(id:String):ResultT[StationLevel]={
    var resultT = new ResultT[StationLevel]()
    var stationLevel=stationLevelService.findById(id)
    resultT.setData(stationLevel)
    return resultT

  }
  @GetMapping(Array("/list"))
  @ApiOperation(value = "查询全部全流程详细记录", notes = "查询全部全流程详细记录")
  def cpuList(stationLevel: StationLevel):ResultT[JavaList[StationLevel]]= {
    var list = stationLevelService.list(stationLevel);
    var resultT = new ResultT[JavaList[StationLevel]]()
    resultT.setData(list);
    return resultT
  }


}
