package com.piesat.calculate.sink

import java.text.SimpleDateFormat
import java.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.piesat.calculate.entity.{BaseEntity, LogMessege, StationCount}
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.connectors.elasticsearch.{ActionRequestFailureHandler, ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests
import org.elasticsearch.common.xcontent.{XContentFactory, XContentType}
import java.util.{ArrayList => JavaList}

import com.piesat.calculate.util.config.SystemConfig
import org.elasticsearch.action.ActionRequest
import org.elasticsearch.action.update.UpdateRequest

/**
  * Created by zzj on 2020/3/26.
  */
class EsSink[T] {
  def sinkAdd(indexName: String): ElasticsearchSink[T] = {
    var SystemConfig =new SystemConfig
    val httpHosts =SystemConfig.loadEsProperties()
    val esSinkBuilder = new ElasticsearchSink.Builder[T](
      httpHosts,
      new ElasticsearchSinkFunction[T] { //参数element就是上面清洗好的数据格式

        def createIndexRequest(element: T): IndexRequest = {

            val entity = element.asInstanceOf[BaseEntity]
            var id=entity.id
          val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
          val date = dateFormat.format(entity.ddatatime)
          return Requests.indexRequest()
            .index(indexName+"-"+date)
            .`type`("doc").id(id)
            .source(JSON.toJSONString(element, new SerializeConfig()), XContentType.JSON)
        }

        override def process(element: T, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createIndexRequest(element))
        }
      }
    )
    return esSinkBuilder.build()
}
  def sinkUpdate(indexName: String): ElasticsearchSink[StationCount] = {
    var SystemConfig =new SystemConfig
    val httpHosts =SystemConfig.loadEsProperties()
    val esSinkBuilder = new ElasticsearchSink.Builder[StationCount](
      httpHosts,
      new ElasticsearchSinkFunction[StationCount] { //参数element就是上面清洗好的数据格式

        def createUpdateRequest(element: StationCount): UpdateRequest = {
            val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
            val date = dateFormat.format(element.windowStart)
            var updateRequest: UpdateRequest = new UpdateRequest();
            updateRequest.index(indexName+"-"+date);
            updateRequest.`type`("doc");
            updateRequest.id(element.id).docAsUpsert(true).scriptedUpsert(true);
            updateRequest.doc(XContentFactory.jsonBuilder()
                        .startObject()
                    .field("data", element.count)
                     .endObject())

        }

        override def process(element: StationCount, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
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
    return esSinkBuilder.build()
  }
}
