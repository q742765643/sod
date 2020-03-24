package com.piesat.monitor.rpc.service

import com.piesat.monitor.dao.es.TestMapper
import com.piesat.monitor.entity.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.{List => JavaList}

/**
  * Created by zzj on 2020/3/19.
  */
@Service
class TestService @Autowired()(testMapper: TestMapper
                              ) {
   def list:JavaList[Test]={
      testMapper.list()
   }
}
