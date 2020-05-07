package com.piesat.calculate.sink

import java.text.SimpleDateFormat
import java.util.{Date, ArrayList => JavaList}

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.fasterxml.jackson.databind.ObjectMapper
import com.piesat.calculate.entity.flow.FlowMonitor
import com.piesat.calculate.entity.task.{DiTaskConfiguration, DiTaskExecute}
import com.piesat.calculate.entity.{BaseEntity, StationCount}
import com.piesat.calculate.util.{EsUtil, MapUtil}
import com.piesat.calculate.util.config.SystemConfig
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.connectors.elasticsearch.{ActionRequestFailureHandler, ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.elasticsearch.action.ActionRequest
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.Requests
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.common.xcontent.{XContentBuilder, XContentFactory, XContentType}

import scala.collection.JavaConverters._

/**
  * Created by zzj on 2020/3/26.
  */
class EsSink[T] {
  def sinkAddTaskExecute(indexName: String): ElasticsearchSink[DiTaskExecute] = {
    var SystemConfig = new SystemConfig
    val httpHosts = SystemConfig.loadEsProperties()
    val esSinkBuilder = new ElasticsearchSink.Builder[DiTaskExecute](
      httpHosts,
      new ElasticsearchSinkFunction[DiTaskExecute] { //参数element就是上面清洗好的数据格式

        def createIndexRequest(indexNameE:String,element: DiTaskExecute): IndexRequest = {
          var map=MapUtil.objectToMapInsert(element)
          return Requests.indexRequest()
            .index(indexNameE)
            .`type`("doc").id(element.id)
            .source(map)
        }
        def createUpdateRequest(element: DiTaskExecute,indexName:String,id:String): UpdateRequest = {
          var map=MapUtil.objectToMapInsert(element)
          return new UpdateRequest().index(indexName).`type`("doc").id(id).doc(map)
        }
        override def process(element: DiTaskExecute, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
          val date = dateFormat.format(element.startTimeL)
          requestIndexer.add(createIndexRequest(indexName+"-"+date,element))
          val filter=scala.collection.mutable.Map[String,Object]()

          var indexNameM:String="di_task_monitor-"+date
          filter.put("START_TIME_L", element.startTimeL.asInstanceOf[Object])
          filter.put("TASK_ID", element.getTaskId)
          var re=EsUtil.getWhereReslut(indexNameM,filter)

          if(re.size()>0){
            var res=re.get(0)
            var id:String=res.get("id").asInstanceOf[String]
            element.id=id
            element.taskDuty=res.get("taskDuty").asInstanceOf[String]
            requestIndexer.add(createUpdateRequest(element,indexNameM,id))
          }

        }
      }
    )
    esSinkBuilder.setBulkFlushMaxActions(1)
    esSinkBuilder.setBulkFlushInterval(TimeValue.timeValueSeconds(5L).getSeconds)
    esSinkBuilder.setBulkFlushBackoffRetries(3)
    esSinkBuilder.setBulkFlushBackoffDelay(2)
    esSinkBuilder.setBulkFlushBackoff(true)
    return esSinkBuilder.build()
  }
  def sinkAddTask(indexName: String): ElasticsearchSink[DiTaskConfiguration] = {
    var SystemConfig = new SystemConfig
    val httpHosts = SystemConfig.loadEsProperties()
    val esSinkBuilder = new ElasticsearchSink.Builder[DiTaskConfiguration](
      httpHosts,
      new ElasticsearchSinkFunction[DiTaskConfiguration] { //参数element就是上面清洗好的数据格式

        def createIndexRequest(element: DiTaskConfiguration): IndexRequest = {

          var map=MapUtil.objectToMapInsert(element)
          return Requests.indexRequest()
            .index(indexName)
            .`type`("doc").id(element.id)
            .source(map)
        }
        def deleteIndexRequest(element: DiTaskConfiguration): DeleteRequest = {

          var map=MapUtil.objectToMapInsert(element)
          return new DeleteRequest().index(indexName).`type`("doc").id(element.id)
        }

        override def process(element: DiTaskConfiguration, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          if(element.isDelete==1){
            requestIndexer.add(deleteIndexRequest(element))
          }else{
            requestIndexer.add(createIndexRequest(element))
          }
        }
      }
    )
    esSinkBuilder.setBulkFlushMaxActions(1)
    esSinkBuilder.setBulkFlushInterval(TimeValue.timeValueSeconds(5L).getSeconds)
    esSinkBuilder.setBulkFlushBackoffRetries(3)
    esSinkBuilder.setBulkFlushBackoffDelay(2)
    esSinkBuilder.setBulkFlushBackoff(true)
    return esSinkBuilder.build()
  }


  def sinkAdd(indexName: String): ElasticsearchSink[T] = {
    var SystemConfig = new SystemConfig
    val httpHosts = SystemConfig.loadEsProperties()
    val esSinkBuilder = new ElasticsearchSink.Builder[T](
      httpHosts,
      new ElasticsearchSinkFunction[T] { //参数element就是上面清洗好的数据格式

        def createIndexRequest(element: T): IndexRequest = {

          var objectMapper = new ObjectMapper
          val entity = element.asInstanceOf[BaseEntity]
          System.out.println("第二步:"+entity.id)
          var id = entity.id
          val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
          val date = dateFormat.format(entity.ddateTime)
          //EsUtil.createIndex(indexName + "-" + date,element.asInstanceOf[Object])
          var map=MapUtil.objectToMapInsert(element)
          System.out.print("第二步:"+id)
          return Requests.indexRequest()
            .index(indexName + "-" + date)
            .`type`("doc").id(id)
            .source(map)
        }

        override def process(element: T, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createIndexRequest(element))
        }
      }
    )
    esSinkBuilder.setBulkFlushMaxActions(1)
    esSinkBuilder.setBulkFlushInterval(TimeValue.timeValueSeconds(5L).getSeconds)
    esSinkBuilder.setBulkFlushBackoffRetries(3)
    esSinkBuilder.setBulkFlushBackoffDelay(2)
    esSinkBuilder.setBulkFlushBackoff(true)
    return esSinkBuilder.build()
  }

  def sinkUpdate(indexName: String): ElasticsearchSink[FlowMonitor] = {
    var SystemConfig = new SystemConfig
    val httpHosts = SystemConfig.loadEsProperties()
    val esSinkBuilder = new ElasticsearchSink.Builder[FlowMonitor](
      httpHosts,
      new ElasticsearchSinkFunction[FlowMonitor] { //参数element就是上面清洗好的数据格式

        def createUpdateRequest(element: FlowMonitor): UpdateRequest = {
          val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
          val date = dateFormat.format(element.ddateTime)
          var updateRequest: UpdateRequest = new UpdateRequest();
          updateRequest.index(indexName + "-" + date);
          updateRequest.`type`("doc");
          updateRequest.id(element.id);//.docAsUpsert(true).scriptedUpsert(true)
          updateRequest.doc(XContentFactory.jsonBuilder()
            .startObject()
            .field("collection_realIncome", element.collectionRealIncome)
            .field("put_realIncome",element.putRealIncome)
            .field("timely",element.timely)
            .field("ddateTime",element.ddateTime)
            .endObject())

        }

        override def process(element: FlowMonitor, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createUpdateRequest(element))
        }
      }
    )

    esSinkBuilder.setFailureHandler(new ActionRequestFailureHandler() {
      @throws[Throwable]
      override def onFailure(actionRequest: ActionRequest, throwable: Throwable, i: Int, requestIndexer: RequestIndexer): Unit = {

        if(throwable.getMessage.indexOf("document_missing_exception")>0){
          val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
          var update=actionRequest.asInstanceOf[UpdateRequest]
          var id=update.id()
          var indexName=update.index()
          var indexs:Array[String]=indexName.split("-")
          //var dateString=indexs.apply(indexs.length-1)
          //var dateTime=dateFormat.parse(dateString)
          var ids:Array[String]=id.split("\\|\\|")
          if(ids.length<3){
            return
          }
          var dataType=ids.apply(1)
          var ii=ids.apply(2)

          val filter=scala.collection.mutable.Map[String,Object]()
          filter.put("DATA_TYPE", dataType)
          if(!"9".equals(ii)){
            filter.put("IIiii",ii)
          }
          var mapPlayBills=EsUtil.getWhereReslut("play_bill",filter)
          if(mapPlayBills.size()==0){
            return
          }
          var mapPlayBill=mapPlayBills.get(0)
          if(mapPlayBill!=null){
            mapPlayBill.put("id",id)
            mapPlayBill.remove("createTime")
            var doc=update.doc().sourceAsMap()
            mapPlayBill.put("ddateTime",doc.get("ddateTime"))
            mapPlayBill.put("collection_realIncome",doc.get("collection_realIncome"))
            mapPlayBill.put("put_realIncome",doc.get("put_realIncome"))
            mapPlayBill.put("distribute_realIncome",0.asInstanceOf[Object])
            mapPlayBill.put("timely",doc.get("timely"))
            var indexRequest=Requests.indexRequest()
              .index(indexName)
              .`type`("doc").id(id)
              .source(mapPlayBill)

            requestIndexer.add(indexRequest)
          }


        }else{
          throw throwable
        }
      }
    })
    esSinkBuilder.setBulkFlushMaxActions(1)
    esSinkBuilder.setBulkFlushInterval(TimeValue.timeValueSeconds(5L).getSeconds)
    esSinkBuilder.setBulkFlushBackoffRetries(3)
    esSinkBuilder.setBulkFlushBackoffDelay(2)
    esSinkBuilder.setBulkFlushBackoff(true)
    return esSinkBuilder.build()
  }
}
