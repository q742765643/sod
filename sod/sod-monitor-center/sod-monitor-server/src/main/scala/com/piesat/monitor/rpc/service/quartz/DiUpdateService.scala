package com.piesat.monitor.rpc.service.quartz

import java.text.SimpleDateFormat
import java.util.Date

import com.piesat.monitor.dao.es.task.DiTaskExecuteMapper
import com.piesat.monitor.entity.task.{DiTaskConfiguration, DiTaskExecute}
import com.piesat.monitor.rpc.service.task.DiTaskConfigurationService
import com.piesat.monitor.util.MapUtil
import com.piesat.monitor.util.quartz.QuartzUtil
import org.elasticsearch.client.Client
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.stereotype.Service

import scala.collection.JavaConversions._

/**
  * Created by zzj on 2020/4/7.
  */
@Service
class DiUpdateService @Autowired()(quartzUtil: QuartzUtil,
                                   elasticsearchTemplate:ElasticsearchTemplate,
                                   diTaskConfigurationService:DiTaskConfigurationService,
                                   diTaskExecuteMapper:DiTaskExecuteMapper) {
  def execute(jobExecutionContext: JobExecutionContext): Unit ={
    this.update()
  }

  def update():Unit= {
    val sdf1 = new SimpleDateFormat("yyyy.MM.dd")
    var monitor: DiTaskExecute = new DiTaskExecute
    monitor.setLtaskState("成功")
    var monitorList = diTaskExecuteMapper.list(monitor)
    var client:Client=elasticsearchTemplate.getClient
    val bulkRequest = client.prepareBulk
    var counter= 0
    for (di: DiTaskExecute <- monitorList) {
      var execute: DiTaskExecute = new DiTaskExecute
      execute.startTimeL=di.startTimeL
      execute.taskId=di.taskId
      var exeuteDi=diTaskExecuteMapper.listExecute(execute)
      if(exeuteDi!=null&&exeuteDi.size()>0){
        var diTaskExecute=exeuteDi.get(0)
        diTaskExecute.id=di.id
        diTaskExecute.taskDuty=di.taskDuty
        var indexName="di_task_monitor-"+sdf1.format(new Date(diTaskExecute.startTimeL))
        bulkRequest.add(client.prepareUpdate(indexName, "doc",diTaskExecute.id).setDoc(MapUtil.objectToMapInsert(diTaskExecute)))
        if (counter != 0 && counter % 1000 == 0) {
          bulkRequest.execute.actionGet
        }
      }
      counter+=1

    }
    var actions=bulkRequest.numberOfActions()
    if(actions>0){
      bulkRequest.execute.actionGet
    }
  }


}
