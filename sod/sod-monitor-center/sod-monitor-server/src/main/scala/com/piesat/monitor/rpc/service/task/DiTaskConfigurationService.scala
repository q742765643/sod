package com.piesat.monitor.rpc.service.task

import java.util.{Date, List => JavaList}

import com.github.pagehelper.PageInfo
import com.piesat.common.utils.IdUtils
import com.piesat.monitor.dao.es.task.DiTaskConfigurationMapper
import com.piesat.monitor.entity.task.DiTaskConfiguration
import com.piesat.util.page.{PageBean, PageForm}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.PageUtil


/**
  * Created by zzj on 2020/3/23.
  */
@Service
class DiTaskConfigurationService  @Autowired()(diTaskConfigurationMapper:DiTaskConfigurationMapper)
{
  def findById(id:String):DiTaskConfiguration={
    diTaskConfigurationMapper.findById(id)
  }

  def list(diTaskConfiguration:DiTaskConfiguration): JavaList[DiTaskConfiguration] = {
    var list =diTaskConfigurationMapper.list(diTaskConfiguration)
    list
  }


  def page(pageForm: PageForm[DiTaskConfiguration]): PageBean[DiTaskConfiguration] = {
    PageUtil.startPage(pageForm.getCurrentPage, pageForm.getPageSize)
    var list = diTaskConfigurationMapper.list(pageForm.getT)
    val pageInfo: PageInfo[DiTaskConfiguration] = new PageInfo[DiTaskConfiguration](list)
    val pageBean: PageBean[DiTaskConfiguration] = new PageBean[DiTaskConfiguration](pageInfo.getTotal, pageInfo.getPages, list)
    pageBean
  }

  def selectAlias:JavaList[String]={
    diTaskConfigurationMapper.selectAlias
  }


}
