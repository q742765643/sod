package com.piesat.calculate.util
import java.util.{ArrayList => JavaList}

import java.text.SimpleDateFormat
import java.util
import java.util.{Date, Map}
import java.util.function.BiConsumer

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.piesat.calculate.entity.LogMessege
import com.piesat.calculate.util.config.SystemConfig
import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.admin.indices.get.GetIndexRequest
import org.elasticsearch.action.bulk.{BackoffPolicy, BulkProcessor, BulkRequest, BulkResponse}
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.{Requests, RestClient, RestHighLevelClient}
import org.elasticsearch.common.unit.{ByteSizeUnit, ByteSizeValue, TimeValue}
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.index.query.{QueryBuilders, TermQueryBuilder}
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.common.settings.Settings
import java.io.IOException
/**
  * Created by zzj on 2020/3/29.
  */
object EsUtil {


  //Java High Level REST Client （高版本client）
  var restHighLevelClient: RestHighLevelClient = null
  private var restClient: RestClient = null
  private var bulkProcessor: BulkProcessor = null

  def main(args: Array[String]): Unit = {
    val timeFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
    val filter=scala.collection.mutable.Map[String,Object]()
    var a="2020-03-30 15:47:35:508";
    filter.put("event.dataset","system.process")
    //get("htht*", "A.00001||自动站2")
    getWhere("metricbeat-*", filter)




  }

  def add(indexRequest: IndexRequest) = {
    bulk().add(indexRequest)
  }

  def update(updateRequest: UpdateRequest) = {

    bulk().add(updateRequest)
  }

  def bulk(): BulkProcessor = {
    if (restHighLevelClient == null) {
      buildClient()
    }
    if (bulkProcessor != null) {
      return bulkProcessor
    } else {
      getBulk()
    }
    //BulkProcessorUtil.bulk()


  }

  protected def buildClient() {
    var SystemConfig = new SystemConfig
    val httpHosts = SystemConfig.loadEsProperties()
    //val credentialsProvider = new BasicCredentialsProvider
    //credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password))
    var client = RestClient.builder(httpHosts.get(0))
    restClient = client.build()
    restHighLevelClient = new RestHighLevelClient(client)
  }

  def getBulk(): BulkProcessor = {
    val listener = new BulkProcessor.Listener() {
      override def beforeBulk(executionId: Long, request: BulkRequest): Unit = {
      }

      override
      def afterBulk(executionId: Long, request: BulkRequest, response: BulkResponse): Unit = {
      }

      override
      def afterBulk(executionId: Long, request: BulkRequest, failure: Throwable): Unit = {
      }
    }
    val consumer = new BiConsumer[BulkRequest, ActionListener[BulkResponse]]() {
      override def accept(t: BulkRequest, u: ActionListener[BulkResponse]): Unit = {
        restHighLevelClient.bulkAsync(t, u)
      }
    }
    val bulkProcessor = BulkProcessor.builder(consumer, listener)
      .setBulkActions(1)
      .setBulkSize(new ByteSizeValue(5L, ByteSizeUnit.MB))
      .setConcurrentRequests(0)
      .setFlushInterval(TimeValue.timeValueSeconds(5L))
      // 重试3次，间隔1s
      .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3))
      .build
    bulkProcessor
  }

  def get(indexName: String, id: String): util.Map[String,Object] = {
    try {
      if (restHighLevelClient == null) {
        buildClient()
      }
      val getRequest = new GetRequest(indexName, "doc", id)
      val getResponse = restHighLevelClient.get(getRequest)
      var result = getResponse.getSourceAsMap
      result
    } catch {
      case ex: Exception => {
        println(ex)
      }
        null
    }
    //result.get()
  }

  def getWhere(indexName: String, map:scala.collection.mutable.Map[String,Object]):Int= {
    if (restHighLevelClient == null) {
      buildClient()
    }
    /*    val boolBuilder = QueryBuilders.boolQuery
        val sourceBuilder = new SearchSourceBuilder
        val matchQueryBuilder = QueryBuilders.matchQuery("id", id) //这里可以根据字段进行搜索，must表示符合条件的，相反的mustnot表示不符合条件的
        // RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("fields_timestamp"); //新建range条件
        // rangeQueryBuilder.gte("2019-03-21T08:24:37.873Z"); //开始时间
        // rangeQueryBuilder.lte("2019-03-21T08:24:37.873Z"); //结束时间
        // boolBuilder.must(rangeQueryBuilder);
        //boolBuilder.must(matchQueryBuilder)
        boolBuilder.should(QueryBuilders.matchPhraseQuery("id", id))

       sourceBuilder.query(boolBuilder) //设置查询，可以是任何类型的QueryBuilder。

           sourceBuilder.from(0) //设置确定结果要从哪个索引开始搜索的from选项，默认为0

           sourceBuilder.size(100) //设置确定搜素命中返回数的size选项，默认为10
        val searchRequest = new SearchRequest(indexName) //索引
        searchRequest.types("doc") //类型

        searchRequest.source(sourceBuilder)
        val response = restHighLevelClient.search(searchRequest)
        val hits = response.getHits*/

    val boolQueryBuilder = QueryBuilders.boolQuery

    for((k,v)<-map){
      val termQueryBuilder = QueryBuilders.matchPhraseQuery(k, v)
      //val termQueryBuilder:TermQueryBuilder= new TermQueryBuilder(k,v)
      boolQueryBuilder.filter(termQueryBuilder)
    }

    try {
      val searchRequest = new SearchRequest()
      searchRequest.indices(indexName)
      val searchSourceBuilder = new SearchSourceBuilder
      searchSourceBuilder.query(boolQueryBuilder)
      searchRequest.source(searchSourceBuilder)
      var response = restHighLevelClient.search(searchRequest)
      var result = response.getHits.getHits
      /*for(a<-result){
        print(a.getSourceAsMap)
      }*/
      if (result.length > 0) {
        return 1
      } else {
        return 0
      }
    } catch {
      case ex:Exception =>{
        print(ex)
      }
        return 0
    }
  }
  def getWhereReslut(indexName: String, map:scala.collection.mutable.Map[String,Object]):JavaList[util.Map[String,Object]]= {
    var list=new util.ArrayList[util.Map[String,Object]]()
    if (restHighLevelClient == null) {
      buildClient()
    }
    val boolQueryBuilder = QueryBuilders.boolQuery

    for((k,v)<-map){
      val termQueryBuilder = QueryBuilders.matchPhraseQuery(k, v)
      boolQueryBuilder.filter(termQueryBuilder)
    }

    try {
      val searchRequest = new SearchRequest()
      searchRequest.indices(indexName)
      val searchSourceBuilder = new SearchSourceBuilder
      searchSourceBuilder.query(boolQueryBuilder)
      searchRequest.source(searchSourceBuilder)
      var response = restHighLevelClient.search(searchRequest)
      var result = response.getHits.getHits
      for(re<-result){
        list.add(re.getSourceAsMap)
      }
     list
    } catch {
      case ex:Exception =>{
        print(ex)
      }
        list
    }
  }


  def createIndex(indexName:String,element:Object): Unit = {
    if (restHighLevelClient == null) {
      buildClient()
    }
    val getIndexRequest = new GetIndexRequest
    getIndexRequest.indices(indexName)
    if(!restHighLevelClient.indices().exists(getIndexRequest)) {
      val request = new CreateIndexRequest(indexName)

      buildSetting(request)
      var map: util.Map[String, Object] = MapUtil.objectToMap(element)
      val properties: util.Map[String, Object] = new java.util.HashMap[String, Object]
      properties.put("properties", map)
      val doc: util.Map[String, Object] = new java.util.HashMap[String, Object]
      doc.put("doc", properties)
      request.mapping("doc", doc)
      try {
        restHighLevelClient.indices.create(request)
      } catch {
        case ex:Exception=>{
          print(ex)
        }
      }
    }
  }

  //设置分片
  def buildSetting(request: CreateIndexRequest): Unit = {
    request.settings(Settings.builder.put("index.number_of_shards", 3).put("index.number_of_replicas", 2))
  }
  def save(indexName:String,id:String,map:util.Map[String,Object]): Unit ={
    if (restHighLevelClient == null) {
      buildClient()
    }
    var request = new BulkRequest();
    request.add(new IndexRequest(indexName, "doc", id).source(map));
    restHighLevelClient.bulk(request)
  }
  def update(indexName:String,id:String,map:util.Map[String,Object]): Unit ={
    if (restHighLevelClient == null) {
      buildClient()
    }
    var request = new BulkRequest();
    request.add(new UpdateRequest().index(indexName).`type`("doc").id(id).doc(map));
    restHighLevelClient.bulk(request)
  }


}
