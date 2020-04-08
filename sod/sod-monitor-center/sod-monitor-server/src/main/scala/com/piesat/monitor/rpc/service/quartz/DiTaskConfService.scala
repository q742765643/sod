package com.piesat.monitor.rpc.service.quartz

import java.text.SimpleDateFormat
import java.util

import org.quartz.{Job, JobExecutionContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.stereotype.Service
import java.util.{Calendar, Date, List => JavaList}

import com.fasterxml.jackson.databind.ObjectMapper
import com.piesat.common.utils.IdUtils
import com.piesat.monitor.entity.task.{DiTaskConfiguration, DiTaskExecute}
import com.piesat.monitor.rpc.service.task.DiTaskConfigurationService
import com.piesat.monitor.util.MapUtil
import com.piesat.monitor.util.quartz.QuartzUtil
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.springframework.data.elasticsearch.core.query.IndexQuery

import scala.collection.JavaConversions._

/**
  * Created by zzj on 2020/4/5.
  */
@Service
class DiTaskConfService @Autowired()(quartzUtil: QuartzUtil,
                                     elasticsearchTemplate:ElasticsearchTemplate,
                                     diTaskConfigurationService:DiTaskConfigurationService) {
  def execute(jobExecutionContext: JobExecutionContext): Unit ={
    this.insert()
  }

  def insert():Unit={
    val calendar = Calendar.getInstance
    calendar.setTime(new Date())
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND,0)
    val zero = calendar.getTime
    var end=zero.getTime+86400*1000
    var diTaskConfiguration=new DiTaskConfiguration
    var diTaskExecutes=new util.ArrayList[DiTaskExecute]
    var diTaskConfList=diTaskConfigurationService.list(diTaskConfiguration)
    for(diTaskConf:DiTaskConfiguration<-diTaskConfList){

      var newTime=zero
      val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val sdf1 = new SimpleDateFormat("yyyy.MM.dd")
      var indexName="di_task_monitor-"+sdf1.format(newTime)
      var flag = true
      newTime=new Date(newTime.getTime-1000)
      while(flag){
        var diTaskExecute=new DiTaskExecute
        diTaskExecute.taskId=diTaskConf.taskId
        diTaskExecute.taskName=diTaskConf.taskName
        diTaskExecute.dataType=diTaskConf.dataType
        diTaskExecute.sendPhys=diTaskConf.sendPhys
        diTaskExecute.system=diTaskConf.system
         newTime=quartzUtil.getNextTime(diTaskConf.taskCron,newTime)
        if(newTime.getTime>end-1000){
          flag=false
        }else{
          diTaskExecute.startTimeL=newTime.getTime
          diTaskExecute.startTimeS=newTime
          diTaskExecute.taskState="未执行"
          diTaskExecute.taskDuty=diTaskConf.taskDuty
          diTaskExecute.id=diTaskExecute.taskId+"_"+diTaskExecute.startTimeL
          diTaskExecutes.add(diTaskExecute)
        }

      }
      var client:Client=elasticsearchTemplate.getClient
      val bulkRequest = client.prepareBulk
      var counter= 0
      for (diTaskExecute:DiTaskExecute<-diTaskExecutes) {

        bulkRequest.add(client.prepareIndex(indexName, "doc",diTaskExecute.id).setSource(MapUtil.objectToMapInsert(diTaskExecute)))
        if (counter != 0 && counter % 1000 == 0) {
          bulkRequest.execute.actionGet
        }
        counter+=1;
      }
      var actions=bulkRequest.numberOfActions()
      if(actions>0){
        bulkRequest.execute.actionGet
      }

      /*var counter= 0
      var objectMapper=new ObjectMapper
      val queries = new util.ArrayList[IndexQuery]
      for (diTaskExecute:DiTaskExecute<-diTaskExecutes) {
        var indexQuery = new IndexQuery();
        diTaskExecute.id=diTaskExecute.taskId+"_"+diTaskExecute.startTimeL
        indexQuery.setId( diTaskExecute.id);
        indexQuery.setObject(MapUtil.objectToMapInsert(diTaskExecute));
        indexQuery.setIndexName(indexName);
        indexQuery.setType("doc");
        queries.add(indexQuery);
        //分批提交索引
        if (counter != 0 && counter % 1000 == 0) {
          elasticsearchTemplate.bulkIndex(queries);
          queries.clear();
        }
        counter+=1;
      }
      //不足批的索引最后不要忘记提交
      if (queries.size() > 0) {
        elasticsearchTemplate.bulkIndex(queries);
      }
      elasticsearchTemplate.refresh(indexName);*/

    }


  }
}
