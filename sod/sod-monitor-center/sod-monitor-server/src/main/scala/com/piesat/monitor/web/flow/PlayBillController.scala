package com.piesat.monitor.web.flow

import java.util.{List => JavaList}

import com.piesat.monitor.entity.flow.PlayBill
import com.piesat.monitor.rpc.service.flow.PlayBillService
import com.piesat.util.ResultT
import com.piesat.util.page.{PageBean, PageForm}
import io.swagger.annotations.{Api, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "节目单接口", tags =  Array ("节目单接口")  )
@RequestMapping(Array("/playBill"))
class PlayBillController  @Autowired()(playBillService: PlayBillService){
  @GetMapping(Array("/page"))
  @ApiOperation(value = "分页查询节目单", notes = "分页查询节目单")
  def page(playBill: PlayBill,pageNum: Int, pageSize: Int): ResultT[PageBean[PlayBill]]={
    var resultT = new ResultT[PageBean[PlayBill]]
    val pageForm = new PageForm[PlayBill](pageNum, pageSize, playBill)
    var pageBean=playBillService.page(pageForm)
    resultT.setData(pageBean)
    return  resultT

  }
  @PostMapping(Array("/save"))
  @ApiOperation(value = "添加节目单", notes = "添加节目单")
  def save(@RequestBody
           playBill: PlayBill):ResultT[String]= {
    var resultT = new ResultT[String]()
    playBillService.save(playBill)

    return resultT
  }
  @PostMapping(Array("/update"))
  @ApiOperation(value = "修改节目单", notes = "修改节目单")
  def update(@RequestBody
             playBill: PlayBill):ResultT[String]= {
    var resultT = new ResultT[String]()
    playBillService.update(playBill)

    return resultT
  }
  @GetMapping(Array("/deleteById"))
  @ApiOperation(value = "根据id删除节目单", notes = "根据id删除节目单")
  def deleteById(id:String):ResultT[String]={
    var resultT = new ResultT[String]()
    playBillService.delete(id)
    return resultT

  }
  @GetMapping(Array("/findById"))
  @ApiOperation(value = "根据id查询节目单", notes = "根据id查询节目单")
  def findById(id:String):ResultT[PlayBill]={
    var resultT = new ResultT[PlayBill]()
    var playBill=playBillService.findById(id)
    resultT.setData(playBill)
    return resultT

  }
  @GetMapping(Array("/list"))
  @ApiOperation(value = "查询全部节目单", notes = "查询全部节目单")
  def cpuList(playBill: PlayBill):ResultT[JavaList[PlayBill]]= {
    var list = playBillService.list(playBill);
    var resultT = new ResultT[JavaList[PlayBill]]()
    resultT.setData(list);
    return resultT
  }


}
