package com.piesat.monitor.dao.es.flow

import java.util.{List => JavaList}

import com.piesat.monitor.entity.flow.{PlayBill, StationLevel}
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait StationLevelMapper {
  def list(stationLevel: StationLevel):JavaList[StationLevel]

  def findById(id:String):StationLevel

}
