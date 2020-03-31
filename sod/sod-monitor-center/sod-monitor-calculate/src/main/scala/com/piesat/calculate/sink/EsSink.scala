package com.piesat.calculate.sink

import java.text.SimpleDateFormat
import java.util.{ArrayList => JavaList}

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.fasterxml.jackson.databind.ObjectMapper
import com.piesat.calculate.entity.flow.FlowMonitor
import com.piesat.calculate.entity.{BaseEntity, StationCount}
import com.piesat.calculate.util.{EsUtil, MapUtil}
import com.piesat.calculate.util.config.SystemConfig
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.connectors.elasticsearch.{ActionRequestFailureHandler, ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.elasticsearch.action.ActionRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.Requests
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.common.xcontent.{XContentBuilder, XContentFactory, XContentType}

/**
  * Created by zzj on 2020/3/26.
  */
class EsSink[T] {
  def sinkAdd(indexName: String): ElasticsearchSink[T] = {
    var SystemConfig = new SystemConfig
    val httpHosts = SystemConfig.loadEsProperties()
    val esSinkBuilder = new ElasticsearchSink.Builder[T](
      httpHosts,
      new ElasticsearchSinkFunction[T] { //参数element就是上面清洗好的数据格式

        def createIndexRequest(element: T): IndexRequest = {
          var objectMapper = new ObjectMapper
          val entity = element.asInstanceOf[BaseEntity]
          var id = entity.id
          val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
          val date = dateFormat.format(entity.ddateTime)
          EsUtil.createIndex(indexName + "-" + date,element.asInstanceOf[Object])
          var map=MapUtil.objectToMapInsert(element)
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
        print(throwable)
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
