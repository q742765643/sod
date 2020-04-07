package com.piesat.monitor.rpc.service.quartz

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import com.piesat.monitor.rpc.service.task.DiTaskConfigurationService
import com.piesat.monitor.util.quartz.QuartzUtil
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.{DeleteQuery, IndexQuery}
import org.springframework.stereotype.Service

/**
  * Created by zzj on 2020/4/7.
  */
@Service
class DropIndexService @Autowired()(quartzUtil: QuartzUtil,
                                    elasticsearchTemplate:ElasticsearchTemplate){
  def execute(jobExecutionContext: JobExecutionContext): Unit ={
    this.delete()
  }
  def delete():Unit={
    val calendar = Calendar.getInstance
    calendar.setTime(new Date(System.currentTimeMillis()-86400*1000*20))
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND,0)
    val zero = calendar.getTime
    val sdf1 = new SimpleDateFormat("yyyy.MM.dd")
    var time=calendar.getTime
    for (i <- 1 to 16){
      var indexName:String="*-"+sdf1.format(time)
      elasticsearchTemplate.deleteIndex(indexName)
      time=new Date(time.getTime+86400*1000)
    }
  }
}
