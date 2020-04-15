package com.piesat.monitor.rpc.service.rabbit

import java.util.{Date, List => JavaList}

import com.github.pagehelper.PageInfo
import com.piesat.common.utils.IdUtils
import com.piesat.monitor.dao.es.rabbitmq.RabbitMqConfigMapper
import com.piesat.monitor.entity.rabbitmq.RabbitMqConfig
import com.piesat.monitor.repository.rabbit.RabbitMqConfigDao
import com.piesat.util.page.{PageBean, PageForm}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.PageUtil


/**
  * Created by zzj on 2020/3/23.
  */
@Service
class RabbitMqConfigService  @Autowired()(rabbitMqConfigDao: RabbitMqConfigDao, rabbitMqConfigMapper:RabbitMqConfigMapper)
{
  def findById(id:String):RabbitMqConfig={
    rabbitMqConfigDao.findById(id).get()
  }
  def save(rabbitMqConfig:RabbitMqConfig):RabbitMqConfig={
    rabbitMqConfig.setId(
     IdUtils.simpleUUID
    )
    rabbitMqConfig.setCreateTime(new Date())
    rabbitMqConfigDao.save(rabbitMqConfig)

  }
  def update(rabbitMqConfig:RabbitMqConfig):RabbitMqConfig={
    rabbitMqConfigDao.save(rabbitMqConfig)
  }
  def list(rabbitMqConfig:RabbitMqConfig): JavaList[RabbitMqConfig] = {
    var list =rabbitMqConfigMapper.list(rabbitMqConfig)
    list
  }


  def page(pageForm: PageForm[RabbitMqConfig]): PageBean[RabbitMqConfig] = {
    PageUtil.startPage(pageForm.getCurrentPage, pageForm.getPageSize)
    var list = rabbitMqConfigMapper.list(pageForm.getT)
    val pageInfo: PageInfo[RabbitMqConfig] = new PageInfo[RabbitMqConfig](list)
    val pageBean: PageBean[RabbitMqConfig] = new PageBean[RabbitMqConfig](pageInfo.getTotal, pageInfo.getPages, list)
    pageBean
  }
  def delete(id:String):Unit={
    rabbitMqConfigDao.deleteById(id)
  }
  def selectAlias:JavaList[String]={
    rabbitMqConfigMapper.selectAlias
  }

}
