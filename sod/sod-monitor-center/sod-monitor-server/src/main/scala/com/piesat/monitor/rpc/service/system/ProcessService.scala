package com.piesat.monitor.rpc.service.system

import java.util
import java.util.{Date, List => JavaList}

import com.piesat.monitor.dao.es.processConfig.ProcessConfigMapper
import com.piesat.monitor.dao.es.system.{FileMapper, ProcessMapper}
import com.piesat.monitor.entity.process.ProcessConfig
import com.piesat.monitor.entity.system.{FileEntity, ProcessEntity}
import com.piesat.monitor.repository.process.ProcessDao
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.elasticsearch.index.query.{BoolQueryBuilder, QueryBuilder, QueryBuilders}
import org.elasticsearch.search.sort.{FieldSortBuilder, SortOrder}
import org.springframework.data.domain.{Page, PageRequest}
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.util.DateUtil

import scala.collection.JavaConversions._


/**
  * Created by zzj on 2020/3/24.
  */
@Service
class ProcessService @Autowired()(processMapper: ProcessMapper,processDao:ProcessDao,
                                  processConifgMapper:ProcessConfigMapper){

  def list(processEntity: ProcessEntity):JavaList[ProcessEntity]={
      processEntity.setDataset("system.process")
      var map:util.Map[String,Date]=DateUtil.getStartAndEnd()

      processEntity.startTime=map.get("startTime")
      processEntity.endTime=map.get("endTime")
      var time=processMapper.maxTime(processEntity);

      processEntity.setTimestamp(time)
      processMapper.list(processEntity)
  }
  def checkProcess(ip:String):Boolean={
    try {
      var processConfig: ProcessConfig = new ProcessConfig
      processConfig.setIp(ip)
      var list: JavaList[ProcessConfig] = processConifgMapper.list(processConfig)
      var processEntity = new ProcessEntity
      processEntity.setHostname(processConfig.ip)
      var plist: JavaList[ProcessEntity] = this.list(processEntity)
      for (p: ProcessConfig <- list) {
        for (pl: ProcessEntity <- plist) {
          if (pl.cmdline.indexOf(p.processName) > 0) {
            p.processState = "1"
            p.timestamp = pl.timestamp
          }
        }
      }
      var flag: Boolean = true
      for (p: ProcessConfig <- list) {
        if (!p.processState.equals("1")) {
          flag = false
        }
      }
      flag
    } catch {
      case ex:Exception =>{
        print(ex)
      }
        false
    }

  }
  def listProcess(processConfig:ProcessConfig):JavaList[ProcessConfig]={
      var list:JavaList[ProcessConfig]=processConifgMapper.list(processConfig)
      var processEntity=new ProcessEntity
       processEntity.setHostname(processConfig.ip)
      var plist:JavaList[ProcessEntity]=this.list(processEntity)
      for(p:ProcessConfig<- list){
         for(pl:ProcessEntity<- plist){
           if(pl.cmdline.indexOf(p.processName)>0){
              p.processState="1"
              p.timestamp=pl.timestamp
           }
         }
        if("".equals(p.processState)){
            p.processState="0"
            p.timestamp=new Date()

        }
      }

      list

  }
  def selectAll(pageSize:Int,pageNumber:Int,processEntity: ProcessEntity):Page[ProcessEntity]={
    var nativeSearch = new NativeSearchQueryBuilder()

    //创建Java查询 Api
    var builder = QueryBuilders.boolQuery();
    //创建filter，使用filter将所有条件串联起来
    var filter = builder.filter();

    if(processEntity != null && !StringUtils.isEmpty(processEntity.getDataset)) {
      //获取服务名称
      /**
        * 注：matchQuery：精确匹配
        */
      filter.add(QueryBuilders.matchQuery("event.dataset", processEntity.getDataset));
    }
    if(processEntity != null && null!=processEntity.getTimestamp) {
      /**
        * 注：matchQuery：精确匹配
        */
      //filter.add(QueryBuilders.matchQuery("@timestamp", processEntity.getTimestamp));
    }
    if(processEntity != null && null!=processEntity.getStartTime) {
      /**
        * 注：rangeQuery：范围匹配
        */
      filter.add(QueryBuilders.rangeQuery("@timestamp").gte(processEntity.getStartTime));
    }
    /*   if(logs != null && !StringUtils.isEmpty(logs.getLevel())) {
     String [] level = logs.getLevel().split(",");
     List<String> levels = Arrays.asList(level);
     /**
       * 注：termsQuery：第一个参数是字段名称，第二个参数是匹配的参数，一个字段匹配多个值（相当于或），字段后必须加keyword不然查不到数据
       */
     filter.add(QueryBuilders.termsQuery("", levels));
   }*/
    nativeSearch.withQuery(builder);


    /**
      * 注：termsQuery：字段排序
      */
    nativeSearch.withSort(new FieldSortBuilder("@timestamp").order(SortOrder.DESC));
    /**
      * 注：termsQuery：分页
      */
    nativeSearch.withPageable(PageRequest.of(pageNumber, pageSize))

    var page=processDao.search(nativeSearch.build())
    page
  }


}
