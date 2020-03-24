package com.piesat.monitor.web.ssh

import com.piesat.monitor.entity.Test
import com.piesat.monitor.entity.ssh.SshEntity
import com.piesat.monitor.rpc.service.ssh.SshService
import com.piesat.util.ResultT
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
  * Created by zzj on 2020/3/23.
  */
@RestController
@Api (value = "ssh管理接口", tags =  Array ("ssh管理接口")  )
@RequestMapping(Array("/ssh"))
class SshController  @Autowired()(sshService: SshService){
  @PostMapping(Array("/save"))
  def list(@RequestBody
           sshEntity: SshEntity):ResultT[String]= {
    var resultT = new ResultT[String]()
    sshService.save(sshEntity)
    return resultT
  }

}
