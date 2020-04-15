package com.piesat.monitor.web.ssh

import com.github.pagehelper.PageHelper
import com.piesat.monitor.entity.Test
import com.piesat.monitor.entity.ssh.SshEntity
import com.piesat.monitor.rpc.service.ssh.SshService
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
@Api (value = "ssh管理接口", tags =  Array ("ssh管理接口")  )
@RequestMapping(Array("/ssh"))
class SshController  @Autowired()(sshService: SshService){
  @GetMapping(Array("/page"))
  @ApiOperation(value = "分页查询ssh", notes = "分页查询ssh")
  def page(sshEntity: SshEntity,pageNum: Int, pageSize: Int): ResultT[PageBean[SshEntity]]={
    var resultT = new ResultT[PageBean[SshEntity]]
    val pageForm = new PageForm[SshEntity](pageNum, pageSize, sshEntity)
    var pageBean=sshService.page(pageForm)
    resultT.setData(pageBean)
    return  resultT

  }
  @PostMapping(Array("/save"))
  @ApiOperation(value = "添加ssh", notes = "添加ssh")
  def save(@RequestBody
           sshEntity: SshEntity):ResultT[String]= {
    var resultT = new ResultT[String]()
    sshService.save(sshEntity)

    return resultT
  }
  @PostMapping(Array("/update"))
  @ApiOperation(value = "修改ssh", notes = "修改ssh")
  def update(@RequestBody
           sshEntity: SshEntity):ResultT[String]= {
    var resultT = new ResultT[String]()
    sshService.update(sshEntity)

    return resultT
  }
  @GetMapping(Array("/deleteById"))
  @ApiOperation(value = "根据id删除ssh", notes = "根据id删除ssh")
  def deleteById(id:String):ResultT[String]={
    var resultT = new ResultT[String]()
    sshService.delete(id)
    return resultT

  }
  @GetMapping(Array("/findById"))
  @ApiOperation(value = "根据id查询ssh", notes = "根据id查询ssh")
  def findById(id:String):ResultT[SshEntity]={
    var resultT = new ResultT[SshEntity]()
    var sshEntity=sshService.findById(id)
    resultT.setData(sshEntity)
    return resultT

  }
  @GetMapping(Array("/selectAlias"))
  @ApiOperation(value = "查询别名", notes = "查询别名")
  def selectAlias:ResultT[JavaList[String]]={
    var list = sshService.selectAlias;
    var resultT = new ResultT[JavaList[String]]()
    resultT.setData(list);
    return resultT
  }
}
