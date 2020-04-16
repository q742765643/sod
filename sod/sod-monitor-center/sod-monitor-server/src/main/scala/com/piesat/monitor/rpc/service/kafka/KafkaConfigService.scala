package com.piesat.monitor.rpc.service.kafka

import java.util.{Date, List => JavaList}

import com.github.pagehelper.PageInfo
import com.piesat.common.utils.IdUtils
import com.piesat.monitor.dao.es.kafka.KafkaConfigMapper
import com.piesat.monitor.entity.kafka.KafkaConfig
import com.piesat.monitor.repository.kafka.KafkaConfigDao
import com.piesat.util.page.{PageBean, PageForm}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.PageUtil


/**
  * Created by zzj on 2020/3/23.
  */
@Service
class KafkaConfigService  @Autowired()(kafkaConfigDao: KafkaConfigDao, kafkaConfigMapper:KafkaConfigMapper)
{
  def findById(id:String):KafkaConfig={
    kafkaConfigDao.findById(id).get()
  }
  def save(kafkaConfig:KafkaConfig):KafkaConfig={
    kafkaConfig.setId(
     IdUtils.simpleUUID
    )
    kafkaConfig.setCreateTime(new Date())
    kafkaConfigDao.save(kafkaConfig)

  }
  def update(kafkaConfig:KafkaConfig):KafkaConfig={
    kafkaConfigDao.save(kafkaConfig)
  }
  def list(kafkaConfig:KafkaConfig): JavaList[KafkaConfig] = {
    var list = kafkaConfigMapper.list(kafkaConfig)
    list
  }

  def page(pageForm: PageForm[KafkaConfig]): PageBean[KafkaConfig] = {
    PageUtil.startPage(pageForm.getCurrentPage, pageForm.getPageSize)
    var list = kafkaConfigMapper.list(pageForm.getT)
    val pageInfo: PageInfo[KafkaConfig] = new PageInfo[KafkaConfig](list)
    val pageBean: PageBean[KafkaConfig] = new PageBean[KafkaConfig](pageInfo.getTotal, pageInfo.getPages, list)
    pageBean
  }
  def delete(id:String):Unit={
    kafkaConfigDao.deleteById(id)
  }
  def selectAlias:JavaList[String]={
    kafkaConfigMapper.selectAlias
  }

}
