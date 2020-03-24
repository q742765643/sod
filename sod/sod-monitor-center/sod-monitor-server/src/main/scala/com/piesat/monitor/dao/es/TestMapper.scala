package com.piesat.monitor.dao.es

import org.apache.ibatis.annotations.Mapper
import java.util.{List => JavaList}

import com.piesat.monitor.entity.Test

/**
  * Created by zzj on 2020/3/19.
  */
@Mapper
trait TestMapper {

  def list():JavaList[Test]

}
