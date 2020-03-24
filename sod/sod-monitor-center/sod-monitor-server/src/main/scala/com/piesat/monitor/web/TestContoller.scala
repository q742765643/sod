package com.piesat.monitor.web

import com.piesat.monitor.rpc.service.TestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}
import java.util.{List => JavaList}

import com.piesat.monitor.entity.Test
import com.piesat.util.ResultT
import io.swagger.annotations.Api
import org.apache.poi.ss.formula.functions.T

/**
  * Created by zzj on 2020/3/19.
  */
@RestController
@Api (value = "测试接口", tags =  Array ("测试接口")  )
@RequestMapping(Array("/test"))
class TestContoller @Autowired() (testService: TestService){

  @GetMapping(Array("/list"))
  def list():ResultT[JavaList[Test]]= {
    var list = testService.list;
    var resultT = new ResultT[JavaList[Test]]()
    resultT.setData(list);
    return resultT
  }

}
