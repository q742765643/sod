package com.piesat.monitor.dao.es.task

import java.util.{List => JavaList}

import com.piesat.monitor.entity.task.{DiTaskConfiguration, DiTaskExecute, DiTaskGroup}
import org.apache.ibatis.annotations.Mapper

/**
  * Created by zzj on 2020/4/1.
  */
@Mapper
trait DiTaskExecuteMapper {
  def list(diTaskExecute: DiTaskExecute):JavaList[DiTaskExecute]

  def listExecute(diTaskExecute: DiTaskExecute):JavaList[DiTaskExecute]

  def findById(id:String):DiTaskExecute

  def groupbyTaskDuty(diTaskExecute: DiTaskExecute):JavaList[DiTaskGroup]

}
