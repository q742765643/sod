package com.piesat.monitor.rpc.service.task

import java.util
import java.util.{HashMap, Map, List => JavaList}

import com.github.pagehelper.PageInfo
import com.piesat.monitor.dao.es.task.DiTaskExecuteMapper
import com.piesat.monitor.entity.task.{DiTaskExecute, DiTaskGroup, DiTaskStatistics}
import com.piesat.util.page.{PageBean, PageForm}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.PageUtil

import scala.collection.JavaConversions._


/**
  * Created by zzj on 2020/3/23.
  */
@Service
class DiTaskExecuteService  @Autowired()(diTaskExecuteMapper:DiTaskExecuteMapper)
{
  def findById(id:String):DiTaskExecute={
    diTaskExecuteMapper.findById(id)
  }

  def list(diTaskExecute:DiTaskExecute): JavaList[DiTaskExecute] = {
    var list =diTaskExecuteMapper.list(diTaskExecute)
    list
  }
  def groupbyTaskDuty(diTaskExecute:DiTaskExecute): JavaList[DiTaskStatistics] = {
    var list =diTaskExecuteMapper.groupbyTaskDuty(diTaskExecute)
    var diTaskStatisticss=new util.ArrayList[DiTaskStatistics]()
    var types:Array[String]=Array("失败","成功","未执行")
    types.foreach((f:String)=>{
      var diTaskStatistics=new DiTaskStatistics
      diTaskStatistics.backup=0
      diTaskStatistics.move=0
      diTaskStatistics.clear=0
      diTaskStatistics.taskState=f
      for(diTaskGroup:DiTaskGroup<-list){
        if(diTaskGroup.taskState.equals(f)&&diTaskGroup.taskDuty.equals("1")){
          diTaskStatistics.clear=diTaskGroup.count
        }
        if(diTaskGroup.taskState.equals(f)&&diTaskGroup.taskDuty.equals("2")){
          diTaskStatistics.backup=diTaskGroup.count
        }
        if(diTaskGroup.taskState.equals(f)&&diTaskGroup.taskDuty.equals("3")){
          diTaskStatistics.move=diTaskGroup.count
        }
      }
      diTaskStatisticss.add(diTaskStatistics)
    })
    var diTaskStatisticC=new DiTaskStatistics
    diTaskStatisticC.taskState="应执行"
    var backCount:Long=0
    var moveCount:Long=0
    var clearCount:Long=0
    for(diTaskStatistics:DiTaskStatistics<-diTaskStatisticss){
      backCount+=diTaskStatistics.backup
      moveCount+=diTaskStatistics.move
      clearCount+=diTaskStatistics.clear
    }
    diTaskStatisticC.backup=backCount
    diTaskStatisticC.move=moveCount
    diTaskStatisticC.clear=clearCount
    diTaskStatisticss.add(diTaskStatisticC)
    diTaskStatisticss

  }

  def page(pageForm: PageForm[DiTaskExecute]): PageBean[DiTaskExecute] = {
    PageUtil.startPage(pageForm.getCurrentPage, pageForm.getPageSize)
    var list = diTaskExecuteMapper.list(pageForm.getT)
    val pageInfo: PageInfo[DiTaskExecute] = new PageInfo[DiTaskExecute](list)
    val pageBean: PageBean[DiTaskExecute] = new PageBean[DiTaskExecute](pageInfo.getTotal, pageInfo.getPages, list)
    pageBean
  }

}
